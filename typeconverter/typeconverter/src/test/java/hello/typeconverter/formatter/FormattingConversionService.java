package hello.typeconverter.formatter;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

public class FormattingConversionService {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        //컨버젼 서비스 이다.
        //이거는 formatter를 등록하기 위한 테스트 클래스

        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        conversionService.addFormatter(new MyNumberFormatter());

        //컨버터와 포매터를 둘다 등록시켜줄 수 있음

        //컨버터 사용
        conversionService.convert("127.0.0.1:8080", IpPort.class);
        Assertions.assertThat(new IpPort("127.0.0.1", 8080));

        //포맷터 사용
        Assertions.assertThat(conversionService.convert(1000,String.class)).isEqualTo("1,000");
        Assertions.assertThat(conversionService.convert("1,000",String.class)).isEqualTo(1000);
    }

}
