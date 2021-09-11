package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


/**
 *
 * 포매터는 숫자  -> "1,000" 이런식으로 쉼표를 넣어준다거나
 * 날짜 객체로 바꾸고 싶을 때 사용하기도 한다
 *
 * 포매터는 객체 -> 문자 , 문자 -> 객체 로서의 변환이 가능하다
 * 구현해보자.
 *
 */
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    /* 여기서 number는 integer stirng double 등등 부모클래스로 있다. */
    /* locale 정보로 나라마다 다른 숫자 포맷을 만들어준다. */
    //input이 text이고 text를 객체로 바꿀때.
    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text = {}, locale = {}", text, locale);
        //"1, 000" -> 1000
        //문자 1000을 숫자 1000으로 바꾸기 위함
        NumberFormat format = NumberFormat.getInstance(locale);
        Number parse = format.parse(text);
        return parse;
    }

    @Override
    //input이 objcet 객체이고 반환이 string
    public String print(Number object, Locale locale) {
        log.info("object = {}, locale = {}",object, locale);
        // 이번에는 object를 문자로 바꿔야한다. 출력하기 위해
        String format = NumberFormat.getInstance(locale).format(object);
        return format;
    }
}
