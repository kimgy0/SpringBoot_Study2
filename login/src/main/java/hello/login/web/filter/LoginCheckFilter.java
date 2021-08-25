package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }

    private static final String[] whiteList = {"/","/members/add","/login","/css/*"};
    //로그인이 되어지지 않아도 들어갈 수 있는 페이지

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 {}",requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("미인증 사용자 요청 {}",requestURI);
                    //로그인 REDIRECT
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI);
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;

        } finally {
            log.info("인증 필터 종료");
        }


    }


    /**
     * 화이트 리스트의 경우 인증 체크를 하지 않는다.
     */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI );
    }


//    @Override
//    public void destroy() {
//
//    }
}
