package cn.vshop.security.core.validate.code;

import cn.vshop.security.core.properties.ValidateCodeType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 存储验证码类型和验证码类型处理器
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/4/7 19:56
 */
@Component
@Setter
@Getter
public class ValidateCodeProcessorHolder {

    /**
     * 搜集系统中所有{@link ValidateCodeProcessor}接口的实现
     * spring自动以（bean名:bean实体）形式注入
     */
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 遍历，返回名字以类型开头的处理器
     * 如：type=email，返回emailCodeProcessor
     *
     * @param type 校验码的类型
     * @return 返回名字以类型开头的处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        String name = type.toString().toLowerCase();
        if (MapUtils.isNotEmpty(validateCodeProcessors)) {
            for (String beanName : validateCodeProcessors.keySet()) {
                if (StringUtils.startsWith(beanName, name)) {
                    return validateCodeProcessors.get(beanName);
                }
            }
        }
        throw new ValidateCodeException("不支持" + type + "类型的校验");
    }
}
