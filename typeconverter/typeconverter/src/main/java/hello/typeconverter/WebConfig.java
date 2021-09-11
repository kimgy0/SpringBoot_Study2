package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // add Formaters (컨버터의 확장된 기능)
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        //포맷터 추가.
        /*
         * 포맷터는 이런식으로 등록해주면 10,000이라는 문자가 단순 10000숫자로 변해서 컨트롤러에 들어오게 됨.
         * 출력할 떄 역시 10000을 보내면 10,000 으로 출력함.
         *
         *
         */
        registry.addFormatter(new MyNumberFormatter());
    }
    /**
     *           //등록
     *         DefaultConversionService conversionService = new DefaultConversionService();
     *         //conversionService 를 구현한 구현체
     *         conversionService.addConverter(new StringToIntegerConverter());
     *         conversionService.addConverter(new IntegerToStringConverter());
     *         conversionService.addConverter(new StringToIpPortConverter());
     *         conversionService.addConverter(new IpPortToStringConverter());
     *
     *         대신하는 코드드
     */}
