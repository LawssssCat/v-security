package cn.vshop.security.wiremock;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * 其实是一个 client，去连接我们刚运行器来的 wiremock，
 * 告诉服务器如何伪造我们的接口
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/2 23:30
 */
public class MockServer {

    public static void main(String[] args) throws IOException {
        // 声明wiremock服务器在哪
        configureFor("192.168.64.33", 8080);
        // 清空以前的配置(理解为初始化)
        removeAllMappings();

        // 请求id=1，返回01文件内容
        mock("/order/1", "01");
        // 请求id=2，返回02文件内容
        mock("/order/2", "02");

    }

    private static void mock(String url, String file) throws IOException {
        // 指定classpath下的路径资源
        ClassPathResource resource = new ClassPathResource("/mock/response/" + file + ".txt");
        // 读取指定路径的文件作为String
        String content = FileUtils.readFileToString(resource.getFile(), "UTF-8");

        // GET /order/1
        stubFor(get(urlPathEqualTo(url))
                // 响应
                .willReturn(aResponse().withBody(content).withStatus(200)));
    }

}
