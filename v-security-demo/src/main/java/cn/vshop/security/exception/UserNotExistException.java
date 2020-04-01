package cn.vshop.security.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/31 14:21
 */
@Getter
@Setter
public class UserNotExistException extends ServiceException {

    private String id;

    public UserNotExistException(String message, String id) {
        super(message);
        this.id = id;
    }

}
