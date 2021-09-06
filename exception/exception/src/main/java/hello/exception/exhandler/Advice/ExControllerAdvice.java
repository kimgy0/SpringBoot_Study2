package hello.exception.exhandler.Advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * @ControllerAdvice
 * @ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능을
 * 부여해주는 역할을 한다.
 * @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
 * @RestControllerAdvice 는 @ControllerAdvice 와 같고, @ResponseBody 가 추가되어 있다.
 * @Controller , @RestController 의 차이와 같다
 */
/**
 *
 * // Target all Controllers annotated with @RestController
 * @ControllerAdvice(annotations = RestController.class)
 * public class ExampleAdvice1 {}
 * // Target all Controllers within specific packages
 * @ControllerAdvice("org.example.controllers")
 * public class ExampleAdvice2 {}
 * // Target all Controllers assignable to specific classes
 * @ControllerAdvice(assignableTypes = {ControllerInterface.class,AbstractController.class})
 * public class ExampleAdvice3 {}
 *
 *
 * 속성으로 basePackage  = "" 도 가능
 * 대상 지정 방법
 */

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult("BAD",e.getMessage());
    }


    @ExceptionHandler
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

}
