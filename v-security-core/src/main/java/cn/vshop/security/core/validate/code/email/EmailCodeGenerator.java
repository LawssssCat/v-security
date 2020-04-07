package cn.vshop.security.core.validate.code.email;

import cn.vshop.security.core.properties.SecurityProperties;
import cn.vshop.security.core.validate.code.ValidateCode;
import cn.vshop.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Random;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 11:33
 */
public class EmailCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        return new ValidateCode(getCode(), securityProperties.getCode().getEmail().getExpireIn());
    }

    private Random random = new Random();

    private String getCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < securityProperties.getCode().getEmail().getLength(); i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
