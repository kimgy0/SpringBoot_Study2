package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;

public class ConversionServiceTest {
    @Test
    void conversionService(){
        //등록
        DefaultConversionService conversionService = new DefaultConversionService();
        //conversionService 를 구현한 구현체
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //사용하기
        Integer result = conversionService.convert("10",Integer.class);
        System.out.println("result = "+ result);

        //숫자 10이 들어가면 반환을 string형식으로 함
        Assertions.assertThat( conversionService.convert(10,String.class)).isEqualTo("10");
        //string 10이 들어가면 반환을 integer 형식으로 함
        Assertions.assertThat(conversionService.convert("10",Integer.class)).isEqualTo(10);

        IpPort convert = conversionService.convert("127.0.0.1:8080", IpPort.class);
        Assertions.assertThat(convert).isEqualTo(new IpPort("127.0.0.1",8080));

        String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        Assertions.assertThat(ipPortString).isEqualTo("127.0.0.1:8080");


    }
}
