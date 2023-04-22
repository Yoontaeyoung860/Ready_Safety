package safe.safe.web.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("loginCheckInterceptor호출");

    String redirectUrl = null;
    String requestURI = request.getRequestURI();
    log.info("requestURI={}", requestURI);

    if(request.getQueryString() != null){
      String queryString = URLEncoder.encode(request.getQueryString(), "UTF-8");
      StringBuffer str = new StringBuffer();
      redirectUrl = str.append(requestURI)
              .append("&")
              .append(queryString)
              .toString();
    }else{
      redirectUrl = requestURI;
    }
    // "/emergency/EA" 경로일 경우 로그인 하지 않은 사용자도 허용
    if (requestURI.equals("/emergency/EA")) {
      return true;
    }
    log.info("LoginCheckInterceptor.preHandle 실행:{}",redirectUrl);

    HttpSession session = request.getSession(false);
    if(session == null || session.getAttribute("loginMember") == null) {
      log.info("미인증 요청 시도!");
      response.sendRedirect("/login?redirectUrl="+redirectUrl);
      return false;
    }

    return true;
  }

}
