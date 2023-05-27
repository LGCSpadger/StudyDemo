package com.test.silder_verify.utils;

import com.test.silder_verify.entity.Captcha;
import org.apache.commons.lang.math.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author kuangq
 * @title PuzzleCaptchaUtil
 * @description 拼图验证码工具类
 * @date 2020-11-12 15:51
 */
public class PuzzleCaptchaUtil {

    /*网络图片地址*/
    private final static String IMG_URL = "https://loyer.wang/view/ftp/wallpaper/%s.jpg";

    /*本地图片地址*/
    private final static String IMG_PATH = "E:/Temp/wallpaper/%s.jpg";

    /*缓存验证码，建议使用redis*/
    public final static Map<String, Integer> CACHE_CODE = new HashMap<>(1);

    /**
     * @param captcha
     * @return com.loyer.example.common.entity.ApiResult
     * @author kuangq
     * @description 获取验证码拼图（生成的抠图和带抠图阴影的大图及抠图坐标）
     * @date 2020-11-14 10:32
     */
    public static Object getCaptcha(Captcha captcha) {
        //参数校验
        checkCaptcha(captcha);
        //获取画布的宽高
        int canvasWidth = captcha.getCanvasWidth();
        int canvasHeight = captcha.getCanvasHeight();
        //获取阻塞块的宽高/半径
        int blockWidth = captcha.getBlockWidth();
        int blockHeight = captcha.getBlockHeight();
        int blockRadius = captcha.getBlockRadius();
        //获取资源图
        BufferedImage canvasImage = getBufferedImage(captcha.getPlace());
        //调整原图到指定大小
        canvasImage = imageResize(canvasImage, canvasWidth, canvasHeight);
        //随机生成阻塞块坐标
        int blockX = getNonceByRange(blockWidth, canvasWidth - blockWidth - 10);
        int blockY = getNonceByRange(10, canvasHeight - blockHeight + 1);
        //阻塞块
        BufferedImage blockImage = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_4BYTE_ABGR);
        //新建的图像根据轮廓图颜色赋值，源图生成遮罩
        cutByTemplate(canvasImage, blockImage, blockWidth, blockHeight, blockRadius, blockX, blockY);
        //缓存移动横坐标
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        CACHE_CODE.clear();
        CACHE_CODE.put(nonceStr, blockX);
        //设置返回参数
        captcha.setNonceStr(nonceStr);
        captcha.setBlockY(blockY);
        captcha.setBlockSrc(toBase64(blockImage, "png"));
        captcha.setCanvasSrc(toBase64(canvasImage, "png"));
        return captcha;
    }

    /**
     * @param captcha
     * @return void
     * @author kuangq
     * @description 入参校验设置默认值
     * @date 2020-11-14 10:49
     */
    private static void checkCaptcha(Captcha captcha) {
        //设置画布宽度默认值
        if (captcha.getCanvasWidth() == null) {
            captcha.setCanvasWidth(320);
        }
        //设置画布高度默认值
        if (captcha.getCanvasHeight() == null) {
            captcha.setCanvasHeight(155);
        }
        //设置阻塞块宽度默认值
        if (captcha.getBlockWidth() == null) {
            captcha.setBlockWidth(65);
        }
        //设置阻塞块高度默认值
        if (captcha.getBlockHeight() == null) {
            captcha.setBlockHeight(55);
        }
        //设置阻塞块凹凸半径默认值
        if (captcha.getBlockRadius() == null) {
            captcha.setBlockRadius(9);
        }
        //设置图片来源默认值
        if (captcha.getPlace() == null) {
            captcha.setPlace(0);
        }
    }

    /**
     * @param start
     * @param end
     * @return int
     * @author kuangq
     * @description 获取指定范围内的随机数
     * @date 2020-11-14 9:41
     */
    public static int getNonceByRange(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    /**
     * @param place
     * @return java.awt.image.BufferedImage
     * @author kuangq
     * @description 获取验证码资源图
     * @date 2020-11-14 10:48
     */
    private static BufferedImage getBufferedImage(Integer place) {
        try {
            //随机图片
            int nonce = getNonceByRange(0, 1000);
            //获取网络资源图片
            if (0 == place) {
                String imgUrl = String.format(IMG_URL, nonce);
                URL url = new URL(imgUrl);
                return ImageIO.read(url.openStream());
            }
            //获取本地图片
            else {
                String imgPath = String.format(IMG_PATH, nonce);
                File file = new File(imgPath);
                return ImageIO.read(file);
            }
        } catch (Exception e) {
            System.out.println("获取拼图资源失败");
            //异常处理
            return null;
        }
    }

    /**
     * @param bufferedImage
     * @param width
     * @param height
     * @return java.awt.image.BufferedImage
     * @author kuangq
     * @description 调整图片大小
     * @date 2020-11-14 9:41
     */
    public static BufferedImage imageResize(BufferedImage bufferedImage, int width, int height) {
        Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resultImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return resultImage;
    }

    /**
     * @param canvasImage
     * @param blockImage
     * @param blockWidth
     * @param blockHeight
     * @param blockRadius
     * @param blockX
     * @param blockY
     * @return void
     * @author kuangq
     * @description 抠图，并生成阻塞块
     * @date 2020-11-14 9:54
     */
    private static void cutByTemplate(BufferedImage canvasImage, BufferedImage blockImage, int blockWidth, int blockHeight, int blockRadius, int blockX, int blockY) {
        BufferedImage waterImage = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_4BYTE_ABGR);
        //阻塞块的轮廓图
        int[][] blockData = getBlockData(blockWidth, blockHeight, blockRadius);
        //创建阻塞块具体形状
        for (int i = 0; i < blockWidth; i++) {
            for (int j = 0; j < blockHeight; j++) {
                try {
                    //原图中对应位置变色处理
                    if (blockData[i][j] == 1) {
                        //背景设置为黑色
                        waterImage.setRGB(i, j, Color.BLACK.getRGB());
                        blockImage.setRGB(i, j, canvasImage.getRGB(blockX + i, blockY + j));
                        //轮廓设置为白色，取带像素和无像素的界点，判断该点是不是临界轮廓点
                        if (blockData[i + 1][j] == 0 || blockData[i][j + 1] == 0 || blockData[i - 1][j] == 0 || blockData[i][j - 1] == 0) {
                            blockImage.setRGB(i, j, Color.WHITE.getRGB());
                            waterImage.setRGB(i, j, Color.WHITE.getRGB());
                        }
                    }
                    //这里把背景设为透明
                    else {
                        blockImage.setRGB(i, j, Color.TRANSLUCENT);
                        waterImage.setRGB(i, j, Color.TRANSLUCENT);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    //防止数组下标越界异常
                }
            }
        }
        //在画布上添加阻塞块水印
        addBlockWatermark(canvasImage, waterImage, blockX, blockY);
    }

    /**
     * @param blockWidth
     * @param blockHeight
     * @param blockRadius
     * @return int[][]
     * @author kuangq
     * @description 构建拼图轮廓轨迹
     * @date 2020-11-14 9:53
     */
    private static int[][] getBlockData(int blockWidth, int blockHeight, int blockRadius) {
        int[][] data = new int[blockWidth][blockHeight];
        double po = Math.pow(blockRadius, 2);
        //随机生成两个圆的坐标，在4个方向上 随机找到2个方向添加凸/凹
        //凸/凹1
        int face1 = RandomUtils.nextInt(4);
        //凸/凹2
        int face2;
        //保证两个凸/凹不在同一位置
        do {
            face2 = RandomUtils.nextInt(4);
        } while (face1 == face2);
        //获取凸/凹起位置坐标
        int[] circle1 = getCircleCoords(face1, blockWidth, blockHeight, blockRadius);
        int[] circle2 = getCircleCoords(face2, blockWidth, blockHeight, blockRadius);
        //随机凸/凹类型
        int shape = getNonceByRange(0, 1);
        //圆的标准方程 (x-a)²+(y-b)²=r²,标识圆心（a,b）,半径为r的圆
        //计算需要的小图轮廓，用二维数组来表示，二维数组有两张值，0和1，其中0表示没有颜色，1有颜色
        for (int i = 0; i < blockWidth; i++) {
            for (int j = 0; j < blockHeight; j++) {
                data[i][j] = 0;
                //创建中间的方形区域
                if ((i >= blockRadius && i <= blockWidth - blockRadius && j >= blockRadius && j <= blockHeight - blockRadius)) {
                    data[i][j] = 1;
                }
                double d1 = Math.pow(i - Objects.requireNonNull(circle1)[0], 2) + Math.pow(j - circle1[1], 2);
                double d2 = Math.pow(i - Objects.requireNonNull(circle2)[0], 2) + Math.pow(j - circle2[1], 2);
                //创建两个凸/凹
                if (d1 <= po || d2 <= po) {
                    data[i][j] = shape;
                }
            }
        }
        return data;
    }

    /**
     * @param face
     * @param blockWidth
     * @param blockHeight
     * @param blockRadius
     * @return int[]
     * @author kuangq
     * @description 根据朝向获取圆心坐标
     * @date 2020-11-14 9:50
     */
    private static int[] getCircleCoords(int face, int blockWidth, int blockHeight, int blockRadius) {
        //上
        if (0 == face) {
            return new int[]{blockWidth / 2 - 1, blockRadius};
        }
        //左
        else if (1 == face) {
            return new int[]{blockRadius, blockHeight / 2 - 1};
        }
        //下
        else if (2 == face) {
            return new int[]{blockWidth / 2 - 1, blockHeight - blockRadius - 1};
        }
        //右
        else if (3 == face) {
            return new int[]{blockWidth - blockRadius - 1, blockHeight / 2 - 1};
        }
        return null;
    }

    /**
     * @param canvasImage
     * @param blockImage
     * @param x
     * @param y
     * @return void
     * @author kuangq
     * @description 在画布上添加阻塞块水印
     * @date 2020-11-14 11:18
     */
    private static void addBlockWatermark(BufferedImage canvasImage, BufferedImage blockImage, int x, int y) {
        Graphics2D graphics2D = canvasImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));
        graphics2D.drawImage(blockImage, x, y, null);
        graphics2D.dispose();
    }

    /**
     * @param bufferedImage
     * @param type
     * @return java.lang.String
     * @author kuangq
     * @description BufferedImage转BASE64
     * @date 2020-11-14 9:41
     */
    public static String toBase64(BufferedImage bufferedImage, String type) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type, byteArrayOutputStream);
            String base64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            return String.format("data:image/%s;base64,%s", type, base64);
        } catch (IOException e) {
            System.out.println("图片资源转换BASE64失败");
            //异常处理
            return null;
        }
    }
}
