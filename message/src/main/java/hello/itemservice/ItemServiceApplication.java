package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasenames("messages");
//		// messages.properties를 읽어들인다.
//		// errors.properties를 읽어들인다.
//		// 위의 두파일은 resource/ --- 경로에 집어넣어야해
//		messageSource.setDefaultEncoding("utf-8");
//		return messageSource;
//	}
//}
}
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

