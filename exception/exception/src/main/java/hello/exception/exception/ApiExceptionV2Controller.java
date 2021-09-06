package hello.exception.exception;

import hello.exception.api.ApiExceptionController;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult("BAD",e.getMessage());
    }
    /*
     * 밑에 로직에서 예외가 터지면 exception 리졸버 구현체를 타면 modelAndView를 반환했지!
     *
     * 예외가 와스까지 안가고 중간에 posthandler를 안탔잖아!
     * 예외해결 시도를 리졸버구현체에서 타게 되면 ExceptionHandlerExceptionResolver를 타고
     * @expetionHandler 어노테이션이 있으면 그 예외에 맞는 클래스를 찾고 그 위 메서드를 실행한다.
     *
     * 호출은 exceptionresolver가 호출해준다.
     *
     * 바로 정상흐름으로 바꿔서 객체를 리턴합니다
     * responsebody 적용 가능.
     *
     * 하지만 정상흐름이기 때문에 http상태코드가 정상인 200코드로 들어가게 된다.
     *
     * 이런 문제때문에 http상태코드까지 커버해주려면.
     * @ResponseStatus(HttpStatus.BAD_REQUEST)
     *
     * 중요한점은 MODELVIEW를 보내면서 EXCEPTIONRESOLVER가 해결하려고 한다고 했잔아
     * 그러니까 정상흐름으로 반환이 된다! 라는 점을 기억하자.
     *
     *      */


    @ExceptionHandler//(UserException.class) 생략가능 인자로 주입받자
    public ResponseEntity<ErrorResult> illegalExHandler(UserException e){
        log.error("[exceptionHandler] ex",e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult("EX", e.getMessage());
    }

    /**
     *
     * 부모예외와 자식예외가 둘다 메서드처리가 있으면 자식예외가 터졌다! 그러면 자식예외가 설정됨
     * 왜냐면 자세한 것이 더 디테일한 것이 우선권을 가진다.
     *
     */








    @GetMapping("/api2/members/{id}")
    public ApiExceptionV2Controller.MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }
        return new ApiExceptionV2Controller.MemberDto(id,"hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }

}
