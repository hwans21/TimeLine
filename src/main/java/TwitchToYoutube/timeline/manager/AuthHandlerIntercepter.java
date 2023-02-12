package TwitchToYoutube.timeline.manager;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthHandlerIntercepter implements HandlerInterceptor {
    private final SessionManager sessionManager;
    private final Logger log = LogManager.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("preHandle start : "+request.getRequestURL());


        Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);


        HttpSession session = request.getSession();
        if(ObjectUtils.isEmpty(user)){
            response.sendRedirect("/");
            return false;
        }else{
            session.setMaxInactiveInterval(30*60);
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
        request.setAttribute("user",user);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }
}
