package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    /**
     * 서블릿에서 예외를 처리하는 종류 2가지
     * exception 직접 호출
     * response.sendError(response, request)
     */

    @GetMapping("/error-ex")
    public void errorEx(){
        throw new RuntimeException("예외가 발생.");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404오류!");
        /*
         * response 객체로 sendError로 에러를 던져준다고 해서 바로 에러가 발생하는 것은 아니고
         * 하단의 서블릿 컨테이너에 에러를 전달해서 예외가 발생했다는 정보를 제공한다.
         *
         * WAS -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(sendError) -> 웹페이지
         */

    }
    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "400오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500오류!");
        /*
         * response 내부에 오류가 발생했다는 상태를 저장해두고,
         * 서블릿 컨테이너는 고객에게 응답 전에 메서드가 호출되었는지를 보고 호출되었다면 에러코드에 맞는 페이지를 호출한다.
         */
    }
}
