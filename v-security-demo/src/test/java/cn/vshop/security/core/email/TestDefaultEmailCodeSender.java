package cn.vshop.security.core.email;

import cn.vshop.security.core.validate.code.email.EmailCodeSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 11:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDefaultEmailCodeSender {
    @Autowired
    private EmailCodeSender emailCodeSender ;

    @Test
    public void sendTest() {
        emailCodeSender.send("1191693505@qq.com", "哈喽");
    }

}
