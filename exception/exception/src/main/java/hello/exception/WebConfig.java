package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import hello.exception.resolver.MyHandlerExceptionResolver;
import hello.exception.resolver.UserHandlerExceptionResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {



//    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> FilterRegistrationBean = new FilterRegistrationBean<>();
        FilterRegistrationBean.setFilter(new LogFilter());
        FilterRegistrationBean.setOrder(1);
        FilterRegistrationBean.addUrlPatterns("/");
        FilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.ERROR);
        /*
         * DispatcherType.REQUEST,DispatcherType.ERROR 만 지정하면
         * 두개의 요청시에만 필터가 동작을 하도록 되어있다.
         *
         * 디폴트는 request임임         */



        return FilterRegistrationBean;
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).order(1).addPathPatterns("/**").excludePathPatterns("/css/**","*.ico","/error","/error-page/**");
        /*
         * "/css/**","*.ico","/error" 이런 부분까지 로그를 남기면 너무 지저분하니까 !
         *
         *      ******** 인터셉터는 dispatchertype을 지정할 수 없다.
         *      ******** 그래서 "/error-page/**" 를 추가 하면 다 빼버려
         *      ******** 그러면 디스패처타입에 대해서 뭐 안건드리고도 할 수 있다.
         *
         */
    }
}
