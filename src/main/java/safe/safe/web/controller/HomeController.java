package safe.safe.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping
public class HomeController {

  //  @RequestMapping("/")
  @GetMapping("/")
  public String home(HttpServletRequest request){

    log.info("info={}","home()호출됨");

    String view = null;
    HttpSession session = request.getSession(false);

    view = (session == null || session.getAttribute("loginMember") == null) ? "main/main" : "main/main" ;

    return view;
  }

  @GetMapping("/restart")
  public String restart(){
    log.info("info={}","restart()호출됨");

    return "main/main";
  }


}