package cn.vshop.security.core.validate.code;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 1:33
 */
@RestController
public class ValidateCodeController {

    /**
     * 校验码信息存入session中的key
     */
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    /**
     * Spring 的工具类，用以操作session
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成随机的验证码，并封装为 ImageCode
        ImageCode imageCode = createImageCode(request);
        // 获取session，并把键值对存入session
        sessionStrategy.setAttribute(
                // ServletWebRequest 是一个适配器（Adapter），把servlet封装成spring的WebRequest（继承了RequestAttributes）
                // 通过把请求传进来，sessionStrategy会从请求中获取session
                new ServletWebRequest(request),
                // 存入session中的key
                SESSION_KEY,
                // 存入session中的值
                imageCode);
        // javax的io工具包
        // 将BufferedImage以指定格式写入输出流中
        ImageIO.write(
                // 以BufferedImage类型的图片
                imageCode.getImage(),
                // 输出的图片格式
                "JPEG",
                // 输出流，输出到响应体中
                response.getOutputStream());

    }

    private Random random = new Random();

    /**
     * 生成验证码图片的逻辑代码
     *
     * @param request 请求
     * @return 将验证码图片封装为 ImageCode 返回
     */
    private ImageCode createImageCode(HttpServletRequest request) {
        int width = 67;
        int height = 23;
        // 图片的缓冲区(buffer)
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 图片的画笔
        // 产生Image对象的Graphics对象,该对象可以在图像上进行各种绘制操作
        Graphics graphics = image.getGraphics();
        /**
         * 背景颜色
         */
        // 设置画笔的颜色
        graphics.setColor(getRandColor(200, 250));
        // 用画笔填充图片
        graphics.fillRect(0, 0, width, height);

        // 绘制干扰线
        // 设置画笔的颜色
        graphics.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            // 起点坐标
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            // 终点坐标
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            // 用画笔画直线
            graphics.drawLine(x, y, xl, yl);
        }

        // 设置画笔的字体、粗细、大小
        graphics.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            // 生成随机字符
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            // 设置画笔颜色
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 画笔填充字符
            graphics.drawString(rand, 13 * i + 6, 16);
        }

        // 执行前面设置好的画笔脚本
        graphics.dispose();
        // 将图片、字符串、过期时间封装成ImageCode
        return new ImageCode(image, sRand, 60);
    }

    /**
     * 生成随机颜色
     *
     * @param fc frontcolor
     * @param bc backcolor
     * @return 颜色实体
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }


}
