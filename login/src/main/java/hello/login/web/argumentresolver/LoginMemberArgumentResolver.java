package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {


    //아규먼트 리졸버는 핸들러 쪽에서 동작하잖아!!

    /**
     * 타입체킹
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        /*
         * 파라미터 정보가 이 메서드로 넘어오면서 login class우리가 만든 로그인 애노테이션이
         * 파라미터에 붙어서 존재를 하고 있는가를 묻고 있으면 true를 반환합니다.
         */
        /*
         *간단하게 instanceof는 특정 Object가 어떤 클래스/인터페이스를 상속/구현했는지를 체크하며
            Class.isAssignableFrom()은 특정 Class가 어떤 클래스/인터페이스를 상속/구현했는지 체크합니다.
         */

        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        //이쪽으로 넘어온 parameter의 정보의 타입을 받아서 그 클래스를 member가 구현했는지!
        return hasLoginAnnotation && hasMemberType;
        //두개다 만족해야 실행.
    }



    /**
     * 어노테이션 아규먼트리졸버 구현
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();

        if(session == null){
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
        //여기서 return 은 어노테이션이 붙은
        //@Login Member member 에 null 이나 객체가 들어감.
    }
}

/**
 *  추가 방법
 *  config에
 *
 *  @Override
 *     public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
 *         resolvers.add(new LoginMemberArgumentResolver());
 *     }
 */
