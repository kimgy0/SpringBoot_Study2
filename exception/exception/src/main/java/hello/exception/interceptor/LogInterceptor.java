package hello.exception.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID="logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID,uuid );

        log.info("Request [{}][{}][{}][{}]",uuid,request.getDispatcherType(),requestURI, handler);
        return true;
        /*
         * 여기서 예외!
         * 정상 출력
         */
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("{} = ",modelAndView);
        /*
         * 예외터지면 이거 호출안됨. 바로 after로 넘어감.
         */
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("Response [{}][{}][{}]",logId,request.getDispatcherType(),requestURI);
        if(ex!=null){
            log.error("error발생했습니다.");
        }
        /*
         * 예외가 터지는 컨트롤러로 보내면 이쪽이 활성화 되긴 되는데 error발생했다는 메세지가 뜸.
         */
    }
}
