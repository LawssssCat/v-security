package cn.vshop.security.core.validate.code;

import cn.vshop.security.core.properties.SecurityProperties;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/5 17:30
 */
@Setter
public class ImageCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    private Random random = new Random();

    @Override
    public ImageCode createImageCode(HttpServletRequest request) {
        // 验证码图片宽度
        // 借助工具类，中request中获取
        int width = ServletRequestUtils.getIntParameter(
                // 从请求中分析width参数，从而获取width值
                request, "width",
                // 如果请求中没有值，就从配置中获取
                securityProperties.getCode().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(
                request, "height",
                securityProperties.getCode().getImage().getHeight());
        // 验证码长度
        int length = securityProperties.getCode().getImage().getLength();
        // 验证码有效时间
        int expiredIn = securityProperties.getCode().getImage().getExpireIn();

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
        for (int i = 0; i < length; i++) {
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
        return new ImageCode(image, sRand, expiredIn);
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
