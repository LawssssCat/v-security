package cn.vshop.security.web.controller;

import cn.vshop.security.dto.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/4/1 17:55
 */
@Slf4j
@RequestMapping("/file")
@RestController
public class FileController {

    private final static String folder = FileController.class.getResource("").getPath();

    /**
     * 客户端 上传文件
     *
     * @param file 上传的文件
     * @return 文件的名字
     * @throws IOException
     */
    @PostMapping
    public FileInfo upload(@RequestBody MultipartFile file) throws IOException {
        // 文件上传名称
        log.info("name={}", file.getName());
        // 文件原始名称
        log.info("originalFilename={}", file.getOriginalFilename());
        // 文件的尺寸
        log.info("size={}", file.getSize());

        log.info("folder:{}", folder);
        String uuid = UUID.randomUUID().toString();
        File localFile = new File(folder, uuid + ".txt");

        file.transferTo(localFile);

        return new FileInfo(localFile.getAbsolutePath());
    }

    /**
     * 向客户端传输下载响应
     *
     * @param id 文件名
     * @param request 请求
     * @param response 响应
     */
    @GetMapping("/{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        try (
                // java 7 新语法，可以把要在finally中释放的资源放在try的括号中定义
                // try块结束后，资源自动释放(语法糖)
                FileInputStream in = new FileInputStream(new File(folder, id + ".txt"));
                ServletOutputStream out = response.getOutputStream();
        ) {
            // 告诉浏览器，这是下载文件响应
            response.setContentType("application/x-download");
            // 设置附件(attachment)的名字(即文件的默认名称)
            response.addHeader("Content-Disposition", "attachment;filename-test.txt");

            // 将输入流写入到输出流(即把文件的内容写入响应中)
            IOUtils.copy(in, out);
            out.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
