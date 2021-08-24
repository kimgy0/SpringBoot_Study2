package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();


    /*
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response){
        //세션의 id 생성 (추정불가능한 값)
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        
        //쿠키생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /*
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        if(cookies == null){
//            return null;
//        }
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals(SESSION_COOKIE_NAME)){
//                return sessionStore.get(cookie.getValue());
//            }
//        }
//        //메서드 리팩토링 ctrl + alt + m
//        return null;

        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie==null){
            return null;
        }
        return sessionStore.get(cookie.getValue());
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        return Arrays.stream(cookies) //이렇게 하면 배열을 array stream형식으로 만들어줌.
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    /*
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie != null){
            sessionStore.remove(cookie.getValue());
        }
    }





}
