package safe.safe.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/beready")
public class BereadyController {

    @GetMapping("/RA")
    public String showbereadyra() {
        return "beready/ra";
    }
    @GetMapping("/RB")
    public String showbereadyrb() {
        return "beready/rb";
    }


}
