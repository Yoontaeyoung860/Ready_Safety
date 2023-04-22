package safe.safe.web.controller.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import safe.safe.web.form.member.login.LoginMember;
import safe.safe.web.form.member.login.LoginForm;
import safe.safe.service.MemberSVC;
import safe.safe.dto.MemberDTO;
import safe.safe.web.form.member.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

  private final MemberSVC memberSVC;

  // 로그인 양식 요청
  @GetMapping("/login")
  public String loginForm
  (@ModelAttribute LoginForm loginForm, HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    return "member/login/loginForm";
  }

  // 로그인 입력에 따른 인증 진행
  @PostMapping("/login")
  public String login(@Valid @ModelAttribute LoginForm loginForm,
                      BindingResult bindingResult,
                      @RequestParam(required = false,defaultValue="/") String redirectUrl,
                      HttpServletRequest request,HttpServletResponse response
  ){

    log.info("redirect1={}",redirectUrl);
    if(bindingResult.hasErrors()){
      log.info("loginError={}", bindingResult);
      return "member/login/loginForm";
    }

    //오브젝트 체크 : 회원유무
    if(!memberSVC.isExistId(loginForm.getId())) {
      log.info("loginError={}", bindingResult);
      bindingResult.reject("error.login", "회원정보가 없습니다");
      return "member/login/loginForm";
    }

    //오브젝트 체크 :로그인
//    MemberDTO memberDTO = memberSVC.login(loginForm.getId(), loginForm.getPw());

    MemberDTO memberDTO = memberSVC.isLogin(loginForm.getId(), loginForm.getPw());  // 비밀번호 암호화(스프링시큐리티) 적용

    if(memberSVC.isDeleteId(loginForm.getId())) {
      bindingResult.reject("error.login", "탈퇴된 회원입니다");
      return "member/login/loginForm";
    }

    //아이디는 맞는데 비밀번호가 틀렸을때
    if(memberSVC.isExistId(loginForm.getId()) && memberDTO == null) {
      bindingResult.reject("error.login", "비밀번호가 틀렸습니다");
      return "member/login/loginForm";
    }

//    if(memberDTO == null){
//      bindingResult.reject("error.login", "회원정보가 없습니다");
//      return "login/loginForm";
//    }

    //회원 세션 정보
//    LoginMember loginMember = new LoginMember(memberDTO.getId(), memberDTO.getEmail(), memberDTO.getNickname());
    LoginMember loginMember = new LoginMember(
            memberDTO.getId(), memberDTO.getNickname(), memberDTO.getEmail(), memberDTO.getAdmin_fl());
    log.info("id={}, email={}, nickname={}, admin_fl={}",
            memberDTO.getId(), memberDTO.getEmail(),memberDTO.getNickname(), memberDTO.getAdmin_fl());




    //인증성공
    //세션이 있으면 세션 반환, 없으면 새로이 생성
    HttpSession session = request.getSession(true);
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//    session.setAttribute("loginMember", loginMember );

    // 자동로그인  - 컨트롤러 및 인터셉터 등 정보 부족으로 미구현
//    if(loginForm.isAutologincheck()) {
//      Cookie cookie = new Cookie("loginCookie", session.getId());
//      cookie.setPath("/");
//      cookie.setMaxAge(60*60*24*7);	//7일
//      response.addCookie(cookie);
//    }

//    // 관리자 여부 확인
//    int code =  Integer.parseInt(memberSVC.admin(loginForm.getId()));
//    if(code == 2) {
//      session.setAttribute("loginAdmin", loginMember );
//    }
//    if(code == 3) {
//      session.setAttribute("loginMember", loginMember );
//    }
    log.info("redirect2={}",redirectUrl);
//    return "redirect:/";
    return "redirect:"+redirectUrl;
  }

  //로그아웃
  @GetMapping("/logout")
  public String logout(HttpServletRequest request){
    //세션이 있으면 세션을 반환하고 없으면 null반환
    HttpSession session = request.getSession(false);
    if(session != null){
      session.invalidate();  //세션 제거
    }
    return "redirect:/";
  }


}