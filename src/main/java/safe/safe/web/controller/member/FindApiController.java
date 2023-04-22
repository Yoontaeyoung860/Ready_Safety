package safe.safe.web.controller.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import safe.safe.dto.MemberDTO;
import safe.safe.service.MemberSVC;
import safe.safe.web.form.member.find.FindIdReq;
import safe.safe.web.form.member.find.FindPwReq;
import safe.safe.web.form.member.support.JsonResult;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/memberexist")
public class FindApiController {

  private final MemberSVC memberSVC;


  //아이디 및 pw 찾기
  @GetMapping("/findidpw")
  public String findidpw(){
    log.info("findIdPwForm() 호출됨!");
    return "templates/member/find/findIdPwForm";
  }



  //아이디 찾기
  @GetMapping("/findid")
  public String findid(){
    log.info("findIdForm() 호출됨!");
    return "templates/member/find/findIdForm";
  }

  //pw 찾기
  @GetMapping("/findpw")
  public String findpw(){
    log.info("findPwForm() 호출됨!");
    return "templates/member/find/findPwForm";
  }


  // id 찾기 확인
  @ResponseBody
  @PutMapping("/exist/findid")
  public JsonResult<String> findIdMember(
          @RequestBody FindIdReq findIdReq, BindingResult bindingResult){

    log.info("findIdReq={}",findIdReq);
    JsonResult<String> result = null;

    String id = memberSVC.findIdMember(findIdReq.getIdemail(),findIdReq.getIdname());

    if(memberSVC.isDeleteId(id)) {
      result = new JsonResult<String>("45", "탈퇴아이디", "탈퇴아이디 입니다");

    } else if(!StringUtils.isEmpty(id)) {

//    if(!StringUtils.isEmpty(id)) {
      result = new JsonResult<String>("00", "success", id);

    }else{
      result = new JsonResult<String>("99", "fail", "찾고자하는 아이디가 없습니다.");

    }
    return result;
  }

  // pw 찾기 획인
  @ResponseBody
  @PutMapping("/exist/findpw")
  public JsonResult<String> findPwMember(
          @RequestBody FindPwReq findPwReq, BindingResult bindingResult){

    log.info("findPwReq={}",findPwReq);
    JsonResult<String> result = null;

    String pw = memberSVC.findPwMember(findPwReq.getPwid(),findPwReq.getPwname(), findPwReq.getPwemail());

    if(!StringUtils.isEmpty(pw)) {
      result = new JsonResult<>("00", "success", pw);

    }else{
      result = new JsonResult<>("99", "fail", "탈퇴된 아이디 또는 찾고자하는 비밀번호가 없습니다.");

    }
    return result;
  }

  // id 중복 체크
  @ResponseBody
  @GetMapping("/id/{id}/exist")
  public JsonResult<MemberDTO> isExistId(@PathVariable String id){
    boolean isExistId = memberSVC.isExistId(id);
    if(isExistId){
      return new JsonResult("00","success","OK");
    }else{
      return new JsonResult("99","fail","NOK");
    }
  }

  // email 중복 체크
  @ResponseBody
  @GetMapping("/email/{email}/exist")
  public JsonResult<MemberDTO> isExistEmail(@PathVariable String email){
    boolean isExistEmail = memberSVC.isExistEmail(email);
    if(isExistEmail){
      return new JsonResult("00","success","OK");
    }else{
      return new JsonResult("99","fail","NOK");
    }
  }

  // 닉네임 중복 체크
  @ResponseBody
  @GetMapping("/nickname/{nickname}/exist")
  public JsonResult<MemberDTO> isExistNickname(@PathVariable String nickname){
    boolean isExistNickname = memberSVC.isExistNickname(nickname);
    if(isExistNickname){
      return new JsonResult("00","success","OK");
    }else{
      return new JsonResult("99","fail","NOK");
    }
  }


}
