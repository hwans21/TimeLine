package TwitchToYoutube.timeline.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /*
     * 로그인 인증 Interceptor 설정
     * */
    @Autowired
    AuthHandlerIntercepter authHandlerIntercepter;
    private final Logger log = LogManager.getLogger(this.getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerIntercepter)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/login","/loginStart","/img/**","/js/login.js");
    }

}