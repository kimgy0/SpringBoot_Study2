package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
/** 원래 런타임 익셉션은 500 에러를 뽑아야 하는데 어노테이션으로 에러코드와 메세지를 설정 할 수 있다. **/

//error.bad = 잘못된 요청요류 입니다. 라고 messages.properties에 적어주면
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") 로도 가능.
/*
 * 단점으로는 개발자가 수정할 수 없는 예외에는 적용이 불가능하다.
 * 애노테이션을 직접 넣어줘야하는데 수정을 못하면 적어주지를 못하기 떄문
 *
 * 추가로 애노테이션을 사용하면 동적으로 변하게 하는것도 어렵기 때문에 이땐 responseStatusException 예외를 사용합니다.
 *
 *  GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
        //상태코드 / 리즌 / 실제 에러 발생 예외.
    }
 */
public class BadRequestException extends RuntimeException{
}
