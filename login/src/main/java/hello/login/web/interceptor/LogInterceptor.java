package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/*
 * 인터셉터는 핸들러어댑터가 호출되기 전에 prehandle 메서드가 출력되며 false일 경우 다음 인터셉트로 넘어가지 않는다.
 * 그다음 핸들러가 호출되고 나면 posthandle메서드가 호출되는데 핸들러에서 예외가 발생하면 실행되지 않는다.
 *
 * after -- 메서드는 finally 구문 같이 예외가 발생하건 하지 않건 공통처리 할 수 있는 로직을 설정해주는것이다
 * 보통 렌더링이 다 끝난 시점에 메서드가 실행된다.
 *
 * 싱글톤임을 조심하자!
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

//
//    //인터셉터를 등록하는 방식
//    webMvcConfigrer을 상속해주고 ! 구현
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogInterceptor())
//                .order(1)
//                .addPathPatterns("/**") //모든 경로에 대해
//                .excludePathPatterns("/css/**","/*.ico","/error"); // 제외할 인터셉터
//    }
    /**
     * 정규식은 인터셉터 pdf에 스프링의 URL경로라고 검색 ㄱㄱㄱ
     */


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        // 이 uuid가 aftercompletion 메서드로 넘어가게 해주고 싶은데 싱글톤이라 클래스레벨이 생성해줘도 안됨(큰일남)
        
        request.setAttribute(LOG_ID, uuid);
        // 이렇게 넘겨주자 편법으로 ㅋㅋㅋ



        /*
         * Object handler 인자는 모든 핸들러 어
         * 댑터가 다 들어올 수 있도록 하기 위함
         * 핸들러가 메서드를 대신 실행해준 꼴
         */

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
        }

        /*
         * 대표적으로 @RequestMapping 을 사용하면 handlerMethod가 넘어오고
         * 정적리소스에 대한 것들은 ResourceHttpRequestHandler 가 넘어온다.
         */


        log.info("REQUEST [{}][{}][{}]",uuid,requestURI,handler);



        return true;
        //true를 받으면 이제 핸들러 호출되고 (handler)가호출 ..
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object logId = (String) request.getAttribute(LOG_ID);
        String requestURI = request.getRequestURI();

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        if(ex != null){
            log.error("afterCompletion error!!",ex);
        }

    }
}
