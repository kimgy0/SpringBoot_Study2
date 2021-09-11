package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request){
        String data = request.getParameter("data");
        Integer integer = Integer.valueOf(data);
        System.out.println("integer = " + integer);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV1(@RequestParam Integer data){
        System.out.println("data = " + data);
        return "ok";
        /*
         * ?data = 10 으로 uri에 주게 되면 문자타입으로 들어오는데
         * 내가 config에서 등록한 converter가 동작해서
         * StringToIntegerConverter 가 동작합니다.
         *
         * 하지만 이게 없어도 스프링에서는 수많은 컨버터를 등록해놓았기 때문에 타입컨버팅이 내가만든 컨버터가 없어도 컨버트 해준다.
         */
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort){
        System.out.println("ipPort.getIp() = " + ipPort.getIp());
        System.out.println("ipPort.getPort() = " + ipPort.getPort());
        return "ok";
    }
    /*
     * 이거동작시키고 싶으면 ?ipPort = 128.0.0.1:8080 해주면 컨버터가 작동함.
     * @RequestParam 은 @RequestParam 을 처리하는 ArgumentResolver 인
        RequestParamMethodArgumentResolver 에서 ConversionService 를 사용해서 타입을 변환한다. 부모
        클래스와 다양한 외부 클래스를 호출하는 등 복잡한 내부 과정을 거치기 때문에 대략 이렇게 처리되는
        것으로 이해해도 충분하다. 만약 더 깊이있게 확인하고 싶으면 IpPortConverter 에 디버그 브레이크
        포인트를 걸어서 확인해보자.
     */
}
