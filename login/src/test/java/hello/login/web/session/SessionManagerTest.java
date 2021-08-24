package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() throws Exception{

        MockHttpServletResponse response = new MockHttpServletResponse();
        /*
         * HttpServletResponse 는 인터페이스이다. 이걸 필요로하는데
         * 이런경우 테스트가 어렵기 때문에 ( 구현체는 있지만 그래도 복잡)
         * 그래서 spring에선 mock이라는 구현체를 그냥 테스트정도만 할 수 있게
         * 제공하는 기능이 있다.
         */
        //given
        //세션 생성 하고 쿠키 전달
        Member member = new Member();
        sessionManager.createSession(member, response);


        //when
        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //then
        //세션 조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isSameAs(member);
        Assertions.assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isNull();

    }

}