package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        try{
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();

                /**
                 * 만약 illegalArgumentException 이 날라오게 되면 이건 서버탓이 아닌
                 * 400 에러 !
                 * 유저가 잘못했다고 예외를 먹어버리고 400에러를 sendError를 호출합니다.
                 * 그다음에 아무것도 없는 modelAndView를 반환하면 사용자에게는 정상흐름으로 모델뷰를 반환하게 되고
                 * 결국 에러는 400에러로 사용자에게 던지게 됩니다.
                 *
                 * 그리거 mvcConfigurer를 구현한 컨피그로가서 등록을 해줌.
                 *
                 * @Override
                 *     public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
                 *         resolvers.add(new MyHandlerExceptionResolver());
                 *     }
                 *     이거 오버라이드 쳐서 등록
                 */
            }

        }catch (IOException e){
            log.error("resolver ex", e);
            e.printStackTrace();
        }
        return null;
        /*
         * 익셉션 리졸버는 여러개 등록할 수 있는데
         * NULL이 반환되면 다음 리졸버를 찾고
         * 모든 리졸버가 이 오류를 처리할 수 없으면 리졸버가 없는 경우 처럼
         * 그냥 예외가 서블릿 밖으로 던져짐.
         */
    }
}
