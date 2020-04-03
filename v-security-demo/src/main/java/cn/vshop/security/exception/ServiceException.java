package cn.vshop.security.exception;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/30 14:57
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
