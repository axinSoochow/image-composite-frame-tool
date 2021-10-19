package com.axin.idea.image.composite.tool;

import com.axin.idea.image.composite.tool.model.FontMaterial;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Axin
 * @summary 文字图片生成工具
 * @since 2021-01-19
 */
public class ImageWordsUtils {

    /**
     * 文字转图片
     *
     * @param file
     * @throws Exception
     */
    public static void createWordsImage(FontMaterial fontMaterial, File file) throws IOException {
        String text = fontMaterial.getText();
        String fontType = fontMaterial.getFontType();
        int style = fontMaterial.getFontStyle().getCode();
        int size = fontMaterial.getFontSize();
        Integer lineDistance = fontMaterial.getLineDistance();

        Font font = new Font(fontType, style, size);

        //获取在传入的文字样式下 的图片宽高
        int[] widthAndHeight = getWidthAndHeight(text, font, lineDistance);
        int width = widthAndHeight[0];
        int height = widthAndHeight[1];

        // 创建图片
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();
        //设置透明  start
        buffImg = gd.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        gd = buffImg.createGraphics();
        //抗锯齿
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gd.setFont(font);
        //目前只支持黑色
        gd.setColor(Color.black);
        //输出文字（中文横向居中）
        gd.drawString(text, 0, font.getSize() + lineDistance);
        gd.dispose();
        // 输出png图片
        ImageIO.write(buffImg, "png", file);
    }


    /**
     * 获取文字图片的宽高
     * @param text
     * @param font
     * @param lineDistance
     * @return
     */
    private static int[] getWidthAndHeight(String text, Font font, Integer lineDistance) {
        Rectangle2D r = font.getStringBounds(text, new FontRenderContext(
                AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());
        int width = (int) Math.ceil(r.getWidth());
        int height = unitHeight + lineDistance;
        // 斜体时边角会因为width不够宽而被截掉 故width多一点预留空间
        return new int[]{width + 5, height};
    }


}
