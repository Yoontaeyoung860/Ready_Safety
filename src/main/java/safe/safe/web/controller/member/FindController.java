package safe.safe.web.controller.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import safe.safe.dto.MemberDTO;
import safe.safe.service.MemberSVC;
import safe.safe.web.controller.member.pwutil.PasswordGeneratorCreator;
import safe.safe.web.form.member.find.ChangPwReq;
import safe.safe.web.form.member.find.FindIdReq;
import safe.safe.web.form.member.find.FindPwReq;

import javax.validation.Valid;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/find")
public class FindController {

  private final MemberSVC memberSVC;


  //아이디 및 pw 찾기
  @GetMapping("/findidpw/{target}")
  public String findidpw( @ModelAttribute FindIdReq findIdReq,
                          @ModelAttribute FindPwReq findPwReq
  ) {
    log.info("findIdPwForm() 호출됨!");

    return "templates/member/find/findIdPwForm";
  }

  @PostMapping("/findid")
  public String findId(@Valid @ModelAttribute FindIdReq findIdReq,
                       BindingResult bindingResult,
                       Model model
  ) {

    model.addAttribute("findPwReq", new FindPwReq());

    if (bindingResult.hasErrors()) {
        log.info("bindingResult={}", bindingResult);
      return "templates/member/find/findIdPwForm";
    }

    String fId = memberSVC.findIdMember(findIdReq.getIdemail(), findIdReq.getIdname());

    if (fId == null) {
      bindingResult.rejectValue("idname","idname", "회원정보가 존재하지 않습니다.");
      return "templates/member/find/findIdPwForm";
    }

    if (memberSVC.isDeleteId(fId)) {
      bindingResult.rejectValue("idname","idname", "이미 탈퇴한 회원입니다");
      return "templates/member/find/findIdPwForm";
    }

    String finds =fId;
    model.addAttribute("findids", finds);

//    List<String> findids = memberSVC.findMemberId(findIdReq);
//    model.addAttribute("findids", findids);
    return "templates/member/find/findIdResult";

//    model.addAttribute("findids",
//            "찾으시는 회원의 ID는 "+"   "+findids+"   "+"입니다");  // id+pw 화면 1개로 할 경우로 return 페이지 만들어야 함


  }


  @PostMapping("/findpw")
  public String findPW(@Valid @ModelAttribute FindPwReq findPwReq,
                       BindingResult bindingResult,
                       Model model
  ) {

    model.addAttribute("findIdReq", new FindIdReq());

    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "templates/member/find/findIdPwForm";
    }

    String fId = memberSVC.findIdMember(findPwReq.getPwemail(), findPwReq.getPwname());

    if (fId == null) {
      bindingResult.rejectValue("pwid","pwid", "회원정보가 존재하지 않습니다.");
      return "templates/member/find/findIdPwForm";
    }

    if (memberSVC.isDeleteId(fId)) {
      bindingResult.rejectValue("pwid","pwid", "이미 탈퇴한 회원입니다");
      return "templates/member/find/findIdPwForm";
    }


    ChangPwReq changPwReq = memberSVC.findMemberPw(findPwReq);
    MemberDTO memberDTO = memberSVC.findByID(findPwReq.getPwid());
    String tmpPw = PasswordGeneratorCreator.generator(10);
    memberSVC.changeMemberPW(findPwReq.getPwemail(), changPwReq.getPw(), tmpPw);

    String finds =tmpPw;
    model.addAttribute("findpws", finds);

//    List<String> findpws = memberSVC.changeMemberPW(findPwReq);
//    model.addAttribute("findpws", findpws);
    return "templates/member/find/findPwResult";

  }

//
//
//
//
//  @GetMapping("/findida")
//  public String FindIdReq(@ModelAttribute FindIdReq findIdReq,
//                          BindingResult bindingResult) {
//    return "member/find/t/findIdForm";
//  }
//
//  @GetMapping("/findpwa")
//  public String FindPwReq(@ModelAttribute FindPwReq findPwReq,
//                          BindingResult bindingResult) {
//    return "member/find/t/findPWForm";
//  }
//
//
//
//
//
//
//  @GetMapping("/findtest")
//  public String FindTest(@ModelAttribute FindTest findTest,
//                          BindingResult bindingResult) {
//    return "member/find/findIdPwForm copy";
//  }
//
//  @PostMapping("/findtest")
//  public String findTestResult(@Valid @ModelAttribute FindTest findTest,
//                       BindingResult bindingResult,
//                       Model model,
//                       HttpServletRequest request, HttpServletResponse response
//  ) {
//
//    if (bindingResult.hasErrors()) {
//      log.info("bindingResult={}", bindingResult);
////      return "/member/find/findIdPwForm";
//      return "member/find/findIdPwForm copy";
//    }
//
//
//    String fId = memberSVC.findIdMember(findTest.getIemail(), findTest.getIname());
//
//    if (fId == null) {
//      bindingResult.reject("error.findida", "회원정보가 없습니다");
////      return "redirect:/find/findidpw";
//      return "member/find/findIdNullResult";
////      return "/member/find/findIdPwForm";      // 에러 : Neither BindingResult nor plain target object for bean name 'findPwReq' available as request attribute
//
//    }
//
////    if(!memberSVC.isExistId(fId)) {
////      log.info("findidaError={}", bindingResult);
////      bindingResult.reject("error.login", "회원정보가 없습니다");
////      return "redirect:/find/findidpw";
//////      return "member/find/findIdPwForm";
////    }
//
//    if (memberSVC.isDeleteId(fId)) {
//      bindingResult.reject("error.findida", "이며 탈퇴된 회원입니다");
////      model.addAttribute("findIdReq",findIdReq);
//      model.addAttribute("fId", fId);
//      return "member/find/findIdDelResult";
////      return "member/find/findIdPwForm";
//    }
//
//
//    ChangPwReq changPwReq = memberSVC.findMemberTestPw(findTest);
//
//    MemberDTO memberDTO = memberSVC.findByID(findTest.getPid());
//
//    String tmpPw = PasswordGeneratorCreator.generator(10);
//
//    memberSVC.changeMemberPW(findTest.getPemail(), changPwReq.getPw(), tmpPw);
//
//    List<String> findpws = memberSVC.changeMemberTestPW(findTest);
//    model.addAttribute("findpws", findpws);
//    return "member/find/findPwResult";
////
////
////
////
////
////
////    model.addAttribute("findpws", findpws);
////    return "member/findedId_Result";
//
//  }
//
//
//



}
