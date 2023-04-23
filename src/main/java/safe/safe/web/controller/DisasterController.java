package safe.safe.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/disaster")
public class DisasterController {

    @GetMapping("/DA")
    public String showdisaster() {
        return "disaster/disasterdefinition";
    }

    @GetMapping("/DB")
    public String shownaturedisaster() {
        return "disaster/naturedisaster";
    }

    @GetMapping("/DB/NA")
    public String showna() {
        return "disaster/na";
    }

    @GetMapping("/DB/NB")
    public String shownb() {
        return "disaster/nb";
    }
    @GetMapping("/DB/NC")
    public String shown() {
        return "disaster/nc";
    }
    @GetMapping("/DB/ND")
    public String shownz() {
        return "disaster/nd";
    }
    @GetMapping("/DB/NE")
    public String showne() {
        return "disaster/ne";
    }
    @GetMapping("/DB/NF")
    public String shownf() {
        return "disaster/nf";
    }
    @GetMapping("/DB/NG")
    public String showng() {
        return "disaster/ng";
    }
    @GetMapping("/DB/NH")
    public String shownh() {
        return "disaster/nh";
    }
    @GetMapping("/DB/NI")
    public String showni() {
        return "disaster/ni";
    }
    @GetMapping("/DB/NJ")
    public String shownj() {
        return "disaster/nj";
    }
    @GetMapping("/DB/NK")
    public String shownk() {
        return "disaster/nk";
    }
    @GetMapping("/DC")
    public String shows() {
        return "disaster/socialaccidents";
    }
    @GetMapping("/DC/SA")
    public String showsa() {
        return "disaster/sa";
    }
    @GetMapping("/DC/SB")
    public String showsb() {
        return "disaster/sb";
    }
    @GetMapping("/DC/SC")
    public String showsc() {
        return "disaster/sc";
    }
    @GetMapping("/DC/SD")
    public String showsd() {
        return "disaster/sd";
    }
    @GetMapping("/DC/SE")
    public String showse() {
        return "disaster/se";
    }
    @GetMapping("/DC/SF")
    public String showsf() {
        return "disaster/sf";
    }
    @GetMapping("/DC/SG")
    public String showsg() {
        return "disaster/sg";
    }
    @GetMapping("/DC/SH")
    public String showsh() {
        return "disaster/sh";
    }
    @GetMapping("/DC/SI")
    public String showsi() {
        return "disaster/si";
    }

    @GetMapping("/DC/SJ")
    public String showsj() {
        return "disaster/sj";
    }
}
