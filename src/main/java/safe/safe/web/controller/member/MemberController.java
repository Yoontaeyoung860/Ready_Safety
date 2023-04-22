package safe.safe.web.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import safe.safe.dto.MemberDTO;
import safe.safe.service.MemberSVC;
import safe.safe.web.form.member.login.LoginMember;
import safe.safe.web.form.member.memberedit.EditForm;
import safe.safe.web.form.member.memberedit.MemberPwForm;
import safe.safe.web.form.member.memberedit.PwEditForm;
import safe.safe.web.form.member.out.OutForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberSVC memberSVC;

//  @GetMapping("/memberinfo")
//  public String memberpage(Model model) {
//    return "member/memberForm";
//  }


  @GetMapping("/memberinfo")
  public String memberpage(HttpSession session,
                           RedirectAttributes redirectAttributes) {

//    MemberDTO memberDTO = new MemberDTO();
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");//세션에서 로그인 정보 가져오기
    int code =  Integer.parseInt(memberSVC.admin(loginMember.getId()));
    if(code == 2) {
      return "redirect:/admin";
    }
    return "member/memberedit/memberForm";
  }

  @GetMapping("/edit")
  public String editForm(
          HttpServletRequest request,
          Model model) {
    HttpSession session = request.getSession(false);
    LoginMember loginMember
            = (LoginMember)session.getAttribute("loginMember");

    //세션이 없으면 로그인 페이지로 이동
    if(loginMember == null) return "redirect:/";

    //회원정보 가져오기
    MemberDTO memberDTO =  memberSVC.findByID(loginMember.getId());
    EditForm editForm = new EditForm();
    BeanUtils.copyProperties(memberDTO, editForm);
    model.addAttribute("editForm", editForm);
    return "member/memberedit/memberEditForm";
  }

  @PatchMapping("/edit")
  public String edit(
          @Valid @ModelAttribute EditForm editForm,
          BindingResult bindingResult,
          HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    LoginMember loginMember
            = (LoginMember)session.getAttribute("loginMember");
    log.info("수정입력처리 확인");


    //세션이 없으면 로그인 페이지로 이동
    if(loginMember == null) return "redirect:/";

    //닉네임 존재유무
    MemberDTO findmemberDTO =  memberSVC.findByID(loginMember.getId());
    if(memberSVC.isExistNickname(editForm.getNickname()) && !(editForm.getNickname().equals(findmemberDTO.getNickname()))) {
      bindingResult.rejectValue("nickname", "nickname", "동일한 닉네임이 존재합니다");
    }

    //이메일 존재유무
    if(memberSVC.isExistEmail(editForm.getEmail()) && !(editForm.getEmail().equals(findmemberDTO.getEmail()))) {
      bindingResult.rejectValue("email", "email", "동일한 email이 존재합니다");
    }

    if(bindingResult.hasErrors()) {
      log.info("errors={}",bindingResult);
      log.info("bindingResult.hasErrors() 확인");
      return "member/memberedit/memberEditForm";
    }

    log.info("입력받은 memberDTO:{}",editForm);

    MemberDTO memberDTO = new MemberDTO();
    BeanUtils.copyProperties(editForm, memberDTO);
    memberSVC.modifyMember(loginMember.getId(), memberDTO);
    return "redirect:/member/edit";
  }

  @GetMapping("/memberpw")
  public String mypage(Model model) {
    model.addAttribute("memberPwForm", new MemberPwForm());
    return "member/memberedit/memberPwForm";
  }

  @PatchMapping("/memberpw")
  public String mypagePw(
          @Valid @ModelAttribute MemberPwForm memberPwForm,
          BindingResult bindingResult,
          HttpServletRequest request) {
    log.info("회원수정처리 호출됨");
    HttpSession session = request.getSession(false);
    LoginMember loginMember =
            (LoginMember)session.getAttribute("loginMember");
    log.info("회원 수정 처리:{}"+loginMember.toString());
    //세션이 없으면 로그인 페이지로 이동
    if(loginMember == null) return "redirect:/";

    //비밀번호를 잘못입력했을경우
    if(!memberSVC.isMember(loginMember.getId(), memberPwForm.getPw())) {
      bindingResult.rejectValue("pw", "pw", "비밀번호가 잘못입력되었습니다.");
      return "member/memberedit/memberPwForm";
    }

    if(bindingResult.hasErrors()) {
      log.info("errors={}",bindingResult);
      return "member/memberpw";
    }

    return "redirect:/member/pwedit";
  }

  @GetMapping("/pwedit")
  public String pwEditForm(
          HttpServletRequest request,
          Model model) {
    HttpSession session = request.getSession(false);
    LoginMember loginMember
            = (LoginMember)session.getAttribute("loginMember");

    //세션이 없으면 로그인 페이지로 이동
    if(loginMember == null) return "redirect:/";

    //회원정보 가져오기
    MemberDTO memberDTO =  memberSVC.findByID(loginMember.getId());
    PwEditForm pwEditForm = new PwEditForm();
    BeanUtils.copyProperties(memberDTO, pwEditForm);
    model.addAttribute("pwEditForm", pwEditForm);
    return "member/memberedit/memberPwEditForm";
  }

  @PatchMapping("/pwedit")
  public String pwEdit(
          @Valid @ModelAttribute PwEditForm pwEditForm,
          BindingResult bindingResult,
          HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    LoginMember loginMember
            = (LoginMember)session.getAttribute("loginMember");
    log.info("수정입력처리 확인");


    if(bindingResult.hasErrors()) {
      log.info("errors={}",bindingResult);
      log.info("bindingResult.hasErrors() 확인");
      return "member/memberedit/memberPwEditForm";
    }

    //세션이 없으면 로그인 페이지로 이동
    if(loginMember == null) return "redirect:/";

    //비밀번호 확인 체크
    if(!pwEditForm.getPw().equals(pwEditForm.getPwChk())) {
      //bindingResult.reject("error.mypage.editForm", "비밀번호가 다릅니다.");
      bindingResult.rejectValue("pwChk", "pwChk", "비밀번호가 일치하지 않습니다.");
      return "member/memberedit/memberPwEditForm";
    }


    log.info("입력받은 memberDTO:{}",pwEditForm);

    MemberDTO memberDTO = new MemberDTO();
    BeanUtils.copyProperties(pwEditForm, memberDTO);
    memberSVC.modifyMemberPw(loginMember.getId(), memberDTO);
    return "redirect:/";
  }





  //회원탈퇴
//  @GetMapping("/{id}/out")
//  @GetMapping("/out")
//  public String outForm(@ModelAttribute OutForm outForm){
//    log.info("outForm 호출됨!");
//    return "member/outForm";
//  }

  @GetMapping("/out")
  public String outForm(HttpServletRequest request,
                        Model model) {
    HttpSession session = request.getSession(false);
    LoginMember loginMember
            = (LoginMember)session.getAttribute("loginMember");

    //세션이 없으면 로그인 페이지로 이동
    if(loginMember == null) return "redirect:/";

    //회원정보 가져오기
    MemberDTO memberDTO =  memberSVC.findByID(loginMember.getId());
    OutForm outForm = new OutForm();
    BeanUtils.copyProperties(memberDTO, outForm);
    model.addAttribute("outForm", outForm);
    return "member/out/outForm";
  }







  //  @PostMapping("/out")
  @PatchMapping("/out")
  public String out(
          @Valid @ModelAttribute OutForm outForm,
          BindingResult bindingResult,
          HttpSession session){

    log.info("out 호출됨");
    //1)유효성체크
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}",bindingResult);
      return "/member/out/outForm";
//      return "redirect:/member/out";
    }
    //2)동의 체크여부
    if(!outForm.getAgree()){
      bindingResult.rejectValue("agree",null, "탈퇴 안내를 확인하고 동의해 주세요.");
      return "/member/out/outForm";
//      return "redirect:/member/out";
    }

    //3) 비밀번호가 일치하는지 체크
    if(!memberSVC.isMember(outForm.getId(), outForm.getPw())){
      bindingResult.rejectValue("pw","memberDTO.pwChk");
      log.info("bindingResult={}", bindingResult);
      return "member/out/outForm";
//      return "redirect:/member/out";
    }

    //4) 탈퇴로직 수행
    //4-1) 회원정보 삭제
    memberSVC.deleteMember(outForm.getId());
    //4-2) 센션정보 제거
    if(session != null){
      session.invalidate();
    }
    return "redirect:/member/outCompleted";
  }

  @GetMapping("/outCompleted")
  public String outCompleted(){

    return "member/out/outCompleted"; //탈퇴수행 완료 view
  }



//  //아이디 찾기
//  @GetMapping("/findid")
//  public String findid(){
//    log.info("findIdForm() 호출됨!");
//    return "member/findIdForm";
//  }
//
//  //pw 찾기
//  @GetMapping("/findpw")
//  public String findpw(){
//    log.info("findPwForm() 호출됨!");
//    return "member/findPwForm";
//  }
//
//




}
