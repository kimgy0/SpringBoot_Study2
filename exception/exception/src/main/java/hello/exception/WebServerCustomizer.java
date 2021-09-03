package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/*
 * 기본 오류페이지를 커스터마이제이션 하기 위한 과정
 */
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {


    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");

        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        // 상수값에 대한 에러코드가 생기면 PATH 2번째인자로 경로를 이동하게 된다.
        /** response.sendError(404)면 errorpage 404가 호출되는 구조. **/

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
        // 오류가 나거나 자식 예외클래스들에 대해서 다 500으로 보내준다.
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
        // 그리고 마지막으로 이렇게 추가해주어야 함.

    }
}
