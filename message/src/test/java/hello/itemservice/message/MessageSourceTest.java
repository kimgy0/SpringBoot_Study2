package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    @Test
    public void helloMessage(){
        String result = messageSource.getMessage("hello", null, null);
        System.out.println("result = " + result);
        // ex ) Locale.KOREA
        // 첫번째는 코드이며 두번째는 인자로 넘어갈 인자 세번째는 지역정보를 나타낸다.
        //      -> 여기서는 로케일 정보가 NULL이므로 베이스정보인 messages.properties로 진행한다.
        Assertions.assertThat(result).isEqualTo("안녕");
    }

    @Test
    public void notFoundMessageCode(){
        assertThatThrownBy(()->messageSource.getMessage("없는코드여 씨발라마", null, null))
                .isInstanceOf(NoSuchMessageException.class);

        //만약 코드를 messages.properties에서 찾지 못하면 에러를 발생하는데 그 에러가 nosuch--- 에러이면 테스트를 통과
    }

    @Test
    public void notFoundMessageCodeDefault(){
        String message = messageSource.getMessage("없는코드여 씨발라마", null, "기본메세지", null);
        //이렇게 하면 코드가 없을때 기본메세지 출력

    }

    //==================================================================================
    //===============================인자 사용 ==========================================

    @Test
    public void argumentMessage(){
        String message = messageSource.getMessage("hello.name", new Object[]{"Spring!!"}, "기본메세지", null);
        //인자를 넘겨줄떄는 new Object[]{"Spring!!"} 이렇게 넘겨야된다.
        //messages.properties 에는 hello.name = " 안녕 {0} " 이런식으로 되어있음.
    }

    //==================================================================================
    //===============================국제화 ==========================================

    @Test
    public void defaultLang() throws Exception{
        assertThat(messageSource.getMessage("hello",null,null)).isEqualTo("안녕");
        assertThat(messageSource.getMessage("hello",null,Locale.KOREA)).isEqualTo("안녕");
    }



    @Test
    public void enLang() throws Exception{
        assertThat(messageSource.getMessage("hello",null,Locale_ENGLISH)).isEqualTo("hello");
    }



    //기본적으로 빈으로 등록이 되어있다고 했어!
//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasenames("messages", "errors");
//        // messages.properties를 읽어들인다.
//        // errors.properties를 읽어들인다.
//        // 위의 두파일은 resource/ --- 경로에 집어넣어야해
//        messageSource.setDefaultEncoding("utf-8");
//        return messageSource;
//    }
    /*
     * 스프링부트는 자동으로 메세지 소스를 빈으로 등록한다.
     *
     *
     * 그래서 해줄 설정은 application.properties
     * spring.messages.basename = messages, config.i18n.messages
     *
     * (default :  spring.messages.basename = messages)
     *
     *
     * 를 입력하면 resource/message.properties와
     * 			 resource/config/i18n/message.properties를 인지함.
     */

}
