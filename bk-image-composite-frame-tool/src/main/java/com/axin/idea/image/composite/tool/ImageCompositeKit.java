package com.axin.idea.image.composite.tool;

import com.axin.idea.image.composite.tool.model.FontMaterial;
import com.axin.idea.image.composite.tool.model.ImageCompositeException;
import com.axin.idea.image.composite.tool.model.ImageMaterial;
import com.axin.idea.image.composite.tool.model.ImageMaterialUnit;
import com.axin.idea.image.composite.tool.model.enumm.ImageType;
import com.axin.idea.image.composite.tool.model.enumm.MaterialType;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Axin
 * @summary 图片合成套件
 * @since 2021-01-15
 */
public class ImageCompositeKit {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ImageCompositeKit.class);

    /**
     * 图片合成 - 异常时返回空File
     *
     * @param imageMaterial
     * @return
     */
    public static File composite(ImageMaterial imageMaterial) {
        // 参数校验
        CompositeParamsUtils.verifyParams(imageMaterial);

        // 默认值初始化
        CompositeParamsUtils.defaultValueRefresh(imageMaterial);

        File imageComposite;
        List<File> tempFiles = null;
        try {
            // 重新刷新图片大小
            reSize(imageMaterial.getMaterialUnits());

            // 文字图片初始化
            tempFiles = imageInit(imageMaterial.getMaterialUnits());

            // 图像合成
            imageComposite = imageComposite(imageMaterial);
            return imageComposite;
        } catch (ImageCompositeException compositeException) {
            log.error("图片合成套件异常", compositeException);
        } finally {
            garbageFile(tempFiles);
        }
        return null;
    }

    /**
     * 将文字素材渲染
     *
     * @summary 图片初始化
     */
    private static List<File> imageInit(List<ImageMaterialUnit> imageMaterialUnits) {
        log.debug("image-composite: image init start...");

        List<File> tempFiles = new ArrayList<>();

        List<ImageMaterialUnit> wordsUnit = imageMaterialUnits.stream()
                        .filter(unit -> unit.getMaterialType().equals(MaterialType.文字元素))
                        .collect(Collectors.toList());
        if (wordsUnit == null || wordsUnit.size() == 0) {
            return tempFiles;
        }

        try {
            log.debug("渲染文字图片...");
            // 2. 渲染文字素材
            for (ImageMaterialUnit unit : wordsUnit) {
                // 文字配置项
                FontMaterial fontMaterial = unit.getFontMaterial();
                File wordPicFile = File.createTempFile(fontMaterial.getText(), ".png");
                // 文字素材生成图片
                ImageWordsUtils.createWordsImage(fontMaterial, wordPicFile);
                // 图片合成上下文填充文字图片文件
                unit.setFile(wordPicFile);
                tempFiles.add(wordPicFile);
            }
            log.debug("image-composite: image init end...");
            return tempFiles;
        } catch (Exception e) {
            log.error("渲染文字素材失败", e);
            throw new ImageCompositeException("渲染文字素材失败");
        }
    }

    /**
     * 图片合成
     *
     * @param imageMaterial
     * @return
     */
    private static File imageComposite(ImageMaterial imageMaterial) {
        log.debug("开始图片合成...");

        List<ImageMaterialUnit> materialUnits = imageMaterial.getMaterialUnits();

        // 模板素材单元
        ImageMaterialUnit templateUnit = materialUnits.stream()
                        .filter(unit -> unit.getMaterialType().equals(MaterialType.模板)).findFirst().get();

        List<ImageMaterialUnit> element = new ArrayList<>();

        // 非模板素材单元 文字元素最后合成会浮在图片上方
        element.addAll(materialUnits.stream().filter(unit -> unit.getMaterialType().equals(MaterialType.图片元素))
                        .collect(Collectors.toList()));
        element.addAll(materialUnits.stream().filter(unit -> unit.getMaterialType().equals(MaterialType.文字元素))
                        .collect(Collectors.toList()));

        try {
            Thumbnails.Builder<BufferedImage> template = Thumbnails.of(bufferedImageInit(templateUnit))
                            .size(templateUnit.getWidth(), templateUnit.getHeight());
            // 暂不支持透明度配置
            for (ImageMaterialUnit unit : element) {
                template.watermark(
                                (int enclosingWidth, int enclosingHeight, int width, int height,
                                                int insetLeft, int insetRight, int insetTop, int insetBottom) -> new Point(unit.getX(), unit.getY()),
                                bufferedImageInit(unit), 1f);
            }

            // 输出图片
            File tempImage = File.createTempFile("tempImage", "." + imageMaterial.getAfterCompositeImageType().getCode());
            template.outputFormat(imageMaterial.getAfterCompositeImageType().getCode())
                            .toFile(tempImage);

            log.debug("图像合成完毕，路径:{}", tempImage.getPath());
            return tempImage;
        } catch (Exception e) {
            log.error("图片合成异常", e);
            throw new ImageCompositeException("图片合成异常");
        }
    }

    /**
     * 图片大小重置
     */
    private static void reSize(List<ImageMaterialUnit> imageMaterialUnits) {
        log.debug("image-composite: image size refresh...");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (ImageMaterialUnit unit : imageMaterialUnits) {
                // 文字元素跳过
                if (unit.getMaterialType().equals(MaterialType.文字元素)) {
                    continue;
                }

                // 图片大小重置
                Thumbnails.of(bufferedImageInit(unit))
                                .size(unit.getWidth(), unit.getHeight())
                                .outputFormat(ImageType.PNG.getCode())
                                .toOutputStream(outputStream);

                BufferedImage reSizeImage = ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
                unit.setTempImage(reSizeImage);
                outputStream.reset();
            }
            log.debug("image-composite: image size refresh end...");
            return;
        } catch (Exception e) {
            log.error("图片大小重置失败!", e);
            throw new ImageCompositeException("图片大小重置失败!");
        }
    }

    /**
     * 根据 图片file 或者 图片url 获取 BufferedImage
     *
     * @param materialUnit
     * @return
     */
    private static BufferedImage bufferedImageInit(ImageMaterialUnit materialUnit) throws IOException {
        if (materialUnit.getTempImage() != null) {
            return materialUnit.getTempImage();
        }
        if (materialUnit.getFile() != null) {
            return ImageIO.read(materialUnit.getFile());
        }
        return ImageIO.read(new URL(materialUnit.getUrl()));
    }

    /**
     * 临时文件收集
     * 
     * @param tempFiles
     */
    private static void garbageFile(List<File> tempFiles) {
        log.debug("image-composite: garbage collect...");

        if (tempFiles == null || tempFiles.size() == 0) {
            return;
        }
        for (File tempFile : tempFiles) {
            if (tempFile == null) {
                continue;
            }
            tempFile.delete();
        }
    }

}
