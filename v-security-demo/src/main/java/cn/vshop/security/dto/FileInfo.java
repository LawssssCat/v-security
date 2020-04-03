package cn.vshop.security.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传成功后的信息
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/1 18:26
 */
@Getter
@Setter
public class FileInfo {

    public FileInfo(String path) {
        this.path = path;
    }

    private String path;

}
