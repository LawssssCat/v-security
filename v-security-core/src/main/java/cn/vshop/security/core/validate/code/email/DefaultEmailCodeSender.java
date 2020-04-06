package cn.vshop.security.core.validate.code.email;

import cn.vshop.security.core.properties.SecurityProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮箱发送类的实现
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 0:47
 */
@Slf4j
@Setter
public class DefaultEmailCodeSender implements EmailCodeSender {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 简单文本邮件
     */
    @Override
    public void send(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        //message.setSubject("登录验证码");
        // 邮件的内容
        message.setText(contentBild(code, securityProperties.getCode().getEmail().getExpireIn()));
        // 设置接收者邮箱
        message.setTo(to);
        // 设置发送者邮箱
        message.setFrom(from);
        // 发送
        mailSender.send(message);
    }

    /**
     * 生成邮箱信息
     */
    private String contentBild(String code, int expireIn) {
        StringBuilder sb = new StringBuilder();
        sb.append("您的验证码:");
        sb.append(code);
        sb.append(",有效时间:");
        sb.append(expireIn);
        sb.append("秒。");
        return sb.toString();
    }


}
