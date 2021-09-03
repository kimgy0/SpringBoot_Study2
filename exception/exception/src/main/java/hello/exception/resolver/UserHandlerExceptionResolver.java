package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try{
            if(ex instanceof UserException){
                log.info("UserException resolver to 400");
                String accept = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                /**응답 상태코드도 바꿔줌.**/

                if("application/json".equals(accept)){
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex",ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult);
                    /**
                     * 제이슨 객체를 스트링형태로 바꾸기 위해서 objectMapper가 필요함.
                     */
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");


                    response.getWriter().write(result);
                    /**
                     * 이런식으로 처리해주면 서블릿 컨텍스트 까지 가서 (was)까지 가서 끝!
                     * 다시 요청안와!
                     * 이런식으로 처리해주면 끝.
                     */
                    return new ModelAndView();
                }else{
                    /** application/json 이 아닌 다른 케이스때에는 **/
                    //html/text 등등
                    return new ModelAndView("error/500");
                    //템플릿 렌더링을 해주겠다 그뜻.
                }
            }
        }catch (IOException e){
            log.error("resolver ex", e);
        }
        return null;
    }
}
