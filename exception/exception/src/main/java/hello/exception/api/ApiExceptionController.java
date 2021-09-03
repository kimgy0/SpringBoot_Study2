package hello.exception.api;


import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ApiExceptionController {
    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
            /**
             * 아규먼트 익셉션은 인자가 잘못됐을 때 내는 에러인데
             * 스팩을 bad라고 절대 보내면 안되는 스펙
             * bad가 오면 사용자의 문제라고 직접 못박아야함 서버잘못이아님
             *
             * 여기서!
             * 원래 인터셉터를 호출하게 되면
             * 컨트롤러 -> 인터셉터 -> 서블릿 -> 필터 -> 와스
             * 에러를 전달하고
             * 이넡셉터는 posthandle 이 예외가 터졋기 때문에 실앻ㅇ 안됨.
             *
             * 하지만 handleExceptionResolver를 사용하면
             * 와스까지 정상응답을 하고 exceptionResolver는 이문제를 해결하려 함
             * 글고 modelAndView를 반환
             *
             * 물론 인터셉터의 posthandle은 호출이 되어지지 않음.
             *
             * ==> resolver 패키지에 handlerExceptionResolver 구현한 클래스로 가보자
            */
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id,"hello " + id);
        /*

                1.

         *  정상 요청시에는 json이 반환되어지지만
         *  런타임예외가 발생했을 때는
         * public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
             public void customize(ConfigurableWebServerFactory factory)
             *
         *  이 클래스의 customize 때문에 다시 에러페이지를 호출하는데
         *  그러면 restcontroller 가 아닌 일반 컨트롤러로 가게 되어서 html 자체 소스코드를 반환합니다.


                2.
               커스터마이즈 메서드가 없어지게 되면 베이직에러 컨트롤러가 작동하는데 basicerrorcontroller 에 가보면
               produce = html/text 매핑도 있고 json 매핑도 있어서 패킷의 헤더의 accept 값만 바꿔주면
               베이직에러 컨트롤러가 알아서 다해줍니다.
         */
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
