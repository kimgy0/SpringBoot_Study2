package hello.itemservice.validation;

import net.bytebuddy.dynamic.scaffold.FieldRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test //객체에러
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

//        new ObjectError("item", new String[]{"required.item","required"})

        Assertions.assertThat(messageCodes).containsExactly("required.item","reqired");
    }


    @Test //필드에러
    void messageCodesResolverField(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);// 마지막은 필드의 타입
        /*
         * 내부적으로 bindingResult가 rejectValue가 리졸버를 쓰고 메세지들을 얻어와서
         * 메세지들을 얻어오면 new FieldError를 만들냅니다.
         *
         * 그리고 필요한 오류를 호출출         */

//        new FieldError("item", "itemName", null,false,messageCodes,null,null);

        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        Assertions.assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required");
    }

}
