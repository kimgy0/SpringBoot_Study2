package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
        //요청이 들어와서 서블릿컨테이너가 만들어질 때 호출 됩니다.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST[{}][{}]",uuid,requestURI);
            chain.doFilter(request, response); // do filter 가 있으면 다음 필터를 호출, 없으면 서블릿으로
        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]",uuid,requestURI);

        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
        //요청이 들어와서 서블릿컨테이너가 소멸할 때 호출 됩니다.
    }


    /**
     *
     * 모든 로그에 같은 식별자 logback mdc 검색해보기
     *
     * 등록하는 방법
     *
     *
     * @Configuration
     * public class WebConfig {
     *     @Bean
     *     public FilterRegistrationBean logFilter(){
     *         FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
     *         filterFilterRegistrationBean.setFilter(new LogFilter());
     *         filterFilterRegistrationBean.setOrder(1);
     *         filterFilterRegistrationBean.addUrlPatterns("/*");
     *         return filterFilterRegistrationBean;
     *     }
     * }
     *
     *
     *
     */
}
