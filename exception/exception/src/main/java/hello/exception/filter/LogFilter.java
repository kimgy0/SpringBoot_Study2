package hello.exception.filter;

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
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        try{
            log.info("Request [{}][{}][{}]",uuid,request.getDispatcherType(),requestURI);
            /**
             *  request uri -> /error-ex 가 찍히는데 나중에 에러가 한번 더 터지고
             *  필터가 다시 호출되면
             *  request uri -> /error-404 를 호출출
             *  */

            /**
             * 처음 was에서 요청이 올때는 dispatchType = Request 이런식으로 오는데
             * 에러를 호출하고 다시 요청이 올때는 dispatchType = Error 로 나온다.
             *
             * 그외에도
             * FORWARD : RequestDispatcherServlet.FORWARD(request,response); 가 호출되면 들어옴
             * INCLUDE : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcherServlet.INCLUDE(request,response);
             * ASYNC :서블릿 비동기 호출 때
             */
            chain.doFilter(request, response);
        }catch (Exception e){
            throw e;
        }finally{
            log.info("Response [{}][{}][{}]",uuid,request.getDispatcherType(),requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
