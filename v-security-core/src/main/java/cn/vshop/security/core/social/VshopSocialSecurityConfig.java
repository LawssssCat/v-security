package cn.vshop.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/5/27 7:27
 */
public class VshopSocialSecurityConfig extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    public VshopSocialSecurityConfig(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        return (T) filter;
    }
}
