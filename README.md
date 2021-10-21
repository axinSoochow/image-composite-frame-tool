# image-composite-frame-tool

## 图片合成工具包可以提供什么帮助？
如果你需要将多张图片素材按坐标位置合成一幅图，该工具包可以帮你简化这一操作。

## 具体使用入口

创建一个  ImageMaterial对象，按照参数释义赋值。

然后直接调用：File ImageCompositeKit.composite(ImageMaterial imageMaterial) 方法即获得一个File。

## 工具包对文字素材的生成 About words

image-composite-frame 工具首先根据传入的文字素材参数生成文字水印图片，再将图片合成到模板图片上。

文字样式目前只支持普通、加粗、斜体；

字体类型的参数是依赖执行程序的操作系统已安装的字体的，使用前请查询节点的操作系统是否安装了你传入的字体类型；

Ps:通过下方代码可获得操作系统已安装的字体信息

```
GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
String[] fontNames = e.getAvailableFontFamilyNames();
```

## 性能测试 Performance Testing

测试机硬件配置：Mac M1 Pro 2020款

### 测试场景一：2MB的模板图片+1-10个1.4MB的图片素材
测试程序会读取 至少20MB+77MB = 97MB大小的图片

![image](http://axin-soochow.oss-cn-hangzhou.aliyuncs.com/21-10/image2021-1-26_16-18-19.png)

![image](http://axin-soochow.oss-cn-hangzhou.aliyuncs.com/21-10/image2021-1-27_14-37-44.png)

从上图中可以看到测试时的堆内存使用情况，读取图片时会占用比较大的内存，频繁使用工具进行大图片合成会有GC Pause时间过长的风险。

### 测试场景二：2MB的模板图片+1-10个文字素材

![image](http://axin-soochow.oss-cn-hangzhou.aliyuncs.com/21-10/image2021-1-26_17-4-12.png)

![image](http://axin-soochow.oss-cn-hangzhou.aliyuncs.com/21-10/image2021-1-27_14-48-4.png)
