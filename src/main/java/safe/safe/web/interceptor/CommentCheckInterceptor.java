package safe.safe.web.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 로그를 사용하기 위한 어노테이션
@Slf4j
public class CommentCheckInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("CommentCheckInterceptor 호출됨!");

    String redirectUrl = "";//리다이렉트할 url
    String requestURI = request.getRequestURI();//요청 uri
    log.info("requestURI={}",requestURI);
    String requestMethod = request.getMethod();//요청 method
    log.info("requestMethod={}",requestMethod);
    String referer = (String)request.getHeader("REFERER");//현재 주소창 url
    log.info("referer={}",referer);

    //리다이렉트 생성
    String[] split = referer.split("/");
    boolean chk = false;
    for(String ele : split){
      if(ele.equals("board")){
        chk = true;
      }
      if(chk){
        redirectUrl += "/" + ele;
      }
    }
    log.info("CommentCheckInterceptor.preHandle 실행 : {}", redirectUrl);

    //delete petch post 를 금지해야
    //비로그인 접근 금지해야

    //2. 요청메소드 판별하기
    //POST 방식이면
    if(HttpMethod.POST.matches(requestMethod) || HttpMethod.PATCH.matches(requestMethod)){
      log.info("1");
      //로그인
      if(loginChk(request)) return true;
      log.info("2");
      //비로그인
      response.sendRedirect("/login?redirectUrl="+redirectUrl);
      return false;
    }
    log.info("3");
    //그외 요청방식이면
    return true;
  }

  //3. 로그인 체크 => 비로그인 상태시 리다이렉트 응답
  boolean loginChk(HttpServletRequest request){

    //session을 가져온다 없다면 null 반환
    HttpSession session = request.getSession(false);
    //비로그인
    if(session == null || session.getAttribute("loginMember") == null){
      log.info("미인증 요청 시도!");
      return false;
      //로그인
    }else{
      return true;
    }
  }//end of loginChk
}
