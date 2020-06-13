package cn.vshop.security.core.social.qq.connect;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.social.support.FormMapHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模仿{@link FormMapHttpMessageConverter}并委托其为我们读写请求，
 * 我们处理读写前后的值映射问题
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/6/2 23:05
 */
public class StringMapHttpMessageConverter implements HttpMessageConverter<Map<String, String>> {

    private FormHttpMessageConverter delegate;

    public StringMapHttpMessageConverter(Charset defaultCharset) {
        delegate = new FormHttpMessageConverter();
        delegate.setCharset(defaultCharset);
        // unmodified
        List<MediaType> supportedMediaTypes = delegate.getSupportedMediaTypes();
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.addAll(supportedMediaTypes);
        mediaTypes.add(MediaType.TEXT_HTML);
        delegate.setSupportedMediaTypes(mediaTypes);
    }

    public StringMapHttpMessageConverter() {
        this(FormHttpMessageConverter.DEFAULT_CHARSET);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (!Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            // we can't read multipart
            if (!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA) &&
                    supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return delegate.getSupportedMediaTypes();
    }

    @Override
    public Map<String, String> read(Class<? extends Map<String, String>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MultiValueMap<String, String> lmvm = new LinkedMultiValueMap<>();
        @SuppressWarnings("unchecked")
        Class<MultiValueMap<String, String>> mvmClazz = (Class<MultiValueMap<String, String>>) lmvm.getClass();
        MultiValueMap<String, String> mvm = delegate.read(mvmClazz, inputMessage);
        return mvm.toSingleValueMap();
    }

    @Override
    public void write(Map<String, String> stringStringMap, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MultiValueMap mvm = null;
        if (stringStringMap instanceof MultiValueMap) {
            mvm = (MultiValueMap) stringStringMap;
        } else {
            mvm = new LinkedMultiValueMap<String, String>();
            mvm.setAll(stringStringMap);
        }
        delegate.write(mvm, contentType, outputMessage);
    }
}
