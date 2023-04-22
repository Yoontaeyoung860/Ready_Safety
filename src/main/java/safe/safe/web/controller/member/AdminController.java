package safe.safe.web.controller.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import safe.safe.dto.MemberDTO;
import safe.safe.service.MemberSVC;
import safe.safe.web.form.member.admin.MemberDetailForm;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  private final MemberSVC memberSVC;

  //관리자 홈
  @GetMapping
  public String home(){
    return "templates/member/admin/adminForm";
  }

  //회원전체조회
  @GetMapping("/memberlist")
  public String memberlist(Model model){

    List<MemberDTO> memberAll = memberSVC.allMemberList();
    model.addAttribute("memberAll", memberAll);

    return "templates/member/admin/adminMemberList";


  }

  //회원개별조회
  @GetMapping("/templates/member/{id}")
  public String member(
      @PathVariable("id") String id,
      Model model){

    MemberDTO memberDTO = memberSVC.findByID(id);
    log.info("memberDTO={}", memberDTO);



    MemberDetailForm memberdetailForm = new MemberDetailForm();
    BeanUtils.copyProperties(memberDTO, memberdetailForm);
    log.info("memberdetailForm={}", memberdetailForm);
    model.addAttribute("memberdetailForm", memberdetailForm);


//    MemberDetailForm memberdetailForm = new MemberDetailForm();
//    memberdetailForm.setId(memberDTO.getId());
//    memberdetailForm.setName(memberDTO.getName());
//    memberdetailForm.setEmail(memberDTO.getEmail());
//    memberdetailForm.setNickname(memberDTO.getNickname());
//    log.info("memberdetailForm={}", memberdetailForm);
//    model.addAttribute("memberdetailForm", memberdetailForm);

    return "templates/member/admin/adminMemberDetail";
  }

//  @GetMapping("/memberdetail")
//  public String memberid(
//      @PathVariable("id") String id,
//      HttpServletRequest request,
//      Model model){
//
//
//    HttpSession session = request.getSession(false);
//    LoginMember loginMember
//        = (LoginMember)session.getAttribute("loginMember");
//
//    MemberDTO memberDTO = memberSVC.findByID(loginMember.getId());
//    log.info("memberDTO={}", memberDTO);
//
//    MemberDetailForm memberdetailForm = new MemberDetailForm();
//    BeanUtils.copyProperties(memberDTO, memberdetailForm);
//    log.info("memberdetailForm={}", memberdetailForm);
//    model.addAttribute("memberdetailForm", memberdetailForm);
//
//    return "member/adminMemberDetail";
//  }
//







}
