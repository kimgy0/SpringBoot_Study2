package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-Page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-Page/500";
    }



    //======================================================================================================
    /**
     * 1.
     * html 을 반환하는것 말고 제이슨으로 반환하기 위해 오류코드를 빼주고 직접 객체를 만들어서 반환.
     *
     * produces = MediaType.APPLICATION_JSON_VALUE
     * 요청 헤더는 aceept = application/json으로 설정해야한다.
     *
     *
     */

    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response){
        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // RequestDispatcher 이클래스에 상수 형식으로 정의되어있어!
        // The value of the attribute is of type java.lang.Integer.

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }










    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); //ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));

        log.info("dispatchType={}", request.getDispatcherType());
        /**
         * 처음 was에서 요청이 올때는 dispatchType = Request 이런식으로 오는데
         * 에러를 호출하고 다시 요청이 올때는 dispatchType = Error 로 나온다.
         *
         * 그외에도
         * FORWARD : RequestDispatcherServlet.FORWARD(request,response); 가 호출되면 들어옴
         * INCLUDE : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcherServlet.INCLUDE(request,response);
         * ASYNC :서블릿 비동기 호출 때
         */

    }
    /**
     * was까지 에러를 전달한 다음에 was는 다시 컨트롤러로 새롭게 에러페이지 요청을 하는데
     * 그때 요청에 addAttribute로 예외 정보가 들어가 있어서 그것을 뽑아내면서 검증하는 과정.
     */
}
