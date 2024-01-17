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

    public void createSession(Object value, HttpServletResponse response){
        // 식별값 생성
        String sessionId = UUID.randomUUID().toString();

        // 세션 저장소에 식별 값을 Key로 객체를 value로 저장
        sessionStore.put(sessionId,value);

        // 클라이언트에게 줄 쿠키 생성 물론 식별자 값을 담아서 줌
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME,sessionId);

        // 응답 메시지에 쿠키 추가
        response.addCookie(mySessionCookie);
    }
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request,String cookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies()).filter(cookie->cookie.getName().equals(cookieName)).findFirst().orElse(null);
    }
}
