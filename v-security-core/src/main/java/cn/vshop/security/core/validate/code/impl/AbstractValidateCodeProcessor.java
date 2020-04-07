package cn.vshop.security.core.validate.code.impl;

import cn.vshop.security.core.properties.SecurityConstants;
import cn.vshop.security.core.validate.code.ValidateCode;
import cn.vshop.security.core.validate.code.ValidateCodeException;
import cn.vshop.security.core.validate.code.ValidateCodeGenerator;
import cn.vshop.security.core.validate.code.ValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * 校验码处理器,处理逻辑包括：
 * 1.校验码生成
 * 2.校验码保存
 * 3.校验码传递(抽象)
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/6 17:43
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;


    @Override
    public void create(ServletWebRequest request) throws Exception {
        // 生成校验码
        C validateCode = generate(request);
        // session中保存校验码
        save(request, validateCode);
        // (抽象)校验码传递
        send(request, validateCode);
    }

    /**
     * 生成校验码
     *
     * @param request
     * @return
     */
    private C generate(ServletWebRequest request) {
        // 从请求中获取校验码类型
        String type = getProcessorType(request);
        // 根据type取出校验码生成器
        for (String beanName : validateCodeGenerators.keySet()) {
            if (StringUtils.startsWith(beanName, type)) {
                return (C) validateCodeGenerators.get(beanName).generate(request);
            }
        }
        throw new ValidateCodeException("不支持生成" + type + "类型的验证码");
    }

    /**
     * 根据请求的url，获取校验码的类型
     *
     * @param request
     * @return
     */
    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX);
    }

    /**
     * 保存校验码
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        sessionStrategy.setAttribute(request, SESSION_KEY_PREFIX + getProcessorType(request).toUpperCase(), validateCode);
    }


    /**
     * 发送校验码，由子类实现
     *
     * @param request      经过spring封装的请求和响应
     * @param validateCode 自定义响应码
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

}
