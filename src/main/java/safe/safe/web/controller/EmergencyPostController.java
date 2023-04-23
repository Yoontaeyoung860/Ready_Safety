package safe.safe.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import safe.safe.dao.EmergencyFilterCondition;
import safe.safe.dto.Code;
import safe.safe.service.EmergencyPostSVC;
import safe.safe.service.UploadFileSVC;
import safe.safe.dto.UploadFileDTO;
import safe.safe.dto.board.EmergencyPostDto;

import safe.safe.paging.FindCriteria;

import safe.safe.web.form.board.EmergencyPostForm.EAddForm;
import safe.safe.web.form.board.EmergencyPostForm.EDetailForm;
import safe.safe.web.form.board.EmergencyPostForm.EEditForm;
import safe.safe.web.form.board.EmergencyPostForm.EItem;
import safe.safe.web.form.member.memberedit.EditForm;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/emergency")
public class EmergencyPostController {
    private final EmergencyPostSVC emergencyPostSVC;
    private final UploadFileSVC uploadFileSVC;

    @Autowired
    @Qualifier("fc10") //동일한 타입의 객체가 여러개있을때 빈이름을 명시적으로 지정해서 주입받을때
    private FindCriteria fc;


    @ModelAttribute("ecategoryCode")
    public List<Code> ecategory(){
        List<Code> list = new ArrayList<>();
        list.add(new Code("EA","심폐 소생술"));
        list.add(new Code("EB","드레싱 및 붕대 이용법"));
        list.add(new Code("EC","환자를 옮기는 방법"));
        list.add(new Code("ED","독극물 중독"));
        list.add(new Code("EE","동물, 곤충에게 물렸을 때"));
        list.add(new Code("EF","성인&소아 기도 폐쇄"));
        return list;
    }
    @GetMapping("/EA")
    public String showCPR() {
        return "emergency/cpr";
    }
    //  등록화면
    @GetMapping("/EB")
    public String showdressing() {
        return "emergency/dressing";
    }
    //  등록화면
    @GetMapping("/EC")
    public String showCarrying() {
        return "emergency/carryingthepatient";
    }
    //  등록화면

    @GetMapping("/ED")
    public String showPoisoning() {
        return "emergency/poisoning";
    }
    //  등록화면
    @GetMapping("")
    public String addForm(@ModelAttribute EAddForm eAddForm, Model model) {
        model.addAttribute("fc",fc);
        return "emergency/eAddForm";
    }
    @GetMapping("/EE")
    public String showbitten()  {
        return "emergency/bittenbeastinsect";
    }
    @GetMapping("/EF")
    public String showrespiratory()  {
        return "emergency/respiratoryobstruction";
    }


    @GetMapping("/{cate}")
    public String getPostsByCategory(@PathVariable("cate") String category, Model model) {
        List<EmergencyPostDto> posts = emergencyPostSVC.getPostsByCategory(category);
        String categoryName = ""; // Get the category name from the category code
        for (Code code : ecategory()) {
            if (code.getCode().equals(category)) {
                categoryName = code.getDecode();
                break;
            }
        }
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("posts", posts);
        return "emergency/emergencyCategoryList";
    }
    //  등록처리
    @PostMapping("")
    public String add(
        @Valid
        @ModelAttribute EAddForm eAddForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model){

        model.addAttribute("fc",fc);
        if(bindingResult.hasErrors()){
            log.info("add/bindingResult={}",bindingResult);
            return "emergency/eAddForm";
        }

        EmergencyPostDto emergencyPost = new EmergencyPostDto();
        emergencyPost.setEtitle(eAddForm.getETitle());
        emergencyPost.setEcontent(eAddForm.getEContent());

        // 파일첨부유무별 게시글 저장
        Long originId = 0L;
        if(eAddForm.getFiles().size() == 0) {
            originId = emergencyPostSVC.write(emergencyPost);//게시글 저장
        }else{
            originId = emergencyPostSVC.write(emergencyPost, eAddForm.getFiles());//게시글 저장 - 파일첨부시
        }

        redirectAttributes.addAttribute("id",originId);

        return "redirect:/emergency/{id}/detail";  //http://서버:9080/notices/공지사항번호
    }
    //  상세화면
    @GetMapping("/{eid}/detail")
    public String detailForm(@PathVariable Long eid, Model model, HttpSession session){
        EmergencyPostDto emergencyPost = emergencyPostSVC.findByEmergencyId(eid);

        EDetailForm eDetailForm = new EDetailForm();
        eDetailForm.setEid(emergencyPost.getEid());
        eDetailForm.setETitle(emergencyPost.getEtitle());
        eDetailForm.setEContent(emergencyPost.getEcontent());



        model.addAttribute("noticeDetailForm",eDetailForm);

        //2) 첨부파일 조회
        List<UploadFileDTO> attachFiles = uploadFileSVC.findFilesByCodeWithRnum("C0102", eDetailForm.getEid());
        if(attachFiles.size() > 0){
            log.info("attachFiles={}",attachFiles);
            model.addAttribute("attachFiles", attachFiles);
        }

        model.addAttribute("fc",fc);

        return "notice/noticeDetailForm";
    }
    //  수정화면
    @GetMapping("/edit/{eid}")
    public String editForm(@PathVariable Long eid, Model model){
        EmergencyPostDto emergencyPost = emergencyPostSVC.findByEmergencyId(eid);

        EDetailForm eEditForm = new EDetailForm();
        eEditForm.setEid(emergencyPost.getEid());
        eEditForm.setETitle(emergencyPost.getEtitle());
        eEditForm.setEContent(emergencyPost.getEcontent());

        model.addAttribute("eEditForm", eEditForm);

        // 첨부파일 조회
        List<UploadFileDTO> attachFiles = uploadFileSVC.findFilesByCodeWithRnum("C0102", eEditForm.getEid());
        if(attachFiles.size() > 0){
            log.info("attachFiles={}",attachFiles);
            model.addAttribute("attachFiles", attachFiles);
        }

        return "emergency/eEditForm";
    }
    //  수정처리
    @PatchMapping("/{eid}")
    public String edit(
        @Valid
        @ModelAttribute EEditForm eEditForm,
        BindingResult bindingResult,
        @PathVariable Long eid,
        RedirectAttributes redirectAttributes
    ){

        if(bindingResult.hasErrors()){
            return "emergency/eEditForm";
        }

        EmergencyPostDto emergency = new EmergencyPostDto();
        emergency.setEid(eid);
        emergency.setEtitle(eEditForm.getETitle());
        emergency.setEcontent(eEditForm.getEContent());
        EmergencyPostDto modifiedNotice = emergencyPostSVC.modify(emergency);

        //3) 파일첨부유무별 게시글 수정
        EmergencyPostDto modifiedEmergencyDTO;
        if(eEditForm.getFiles().size() == 0) {
            modifiedEmergencyDTO = emergencyPostSVC.modify(emergency);//게시글 수정
        }else{
            modifiedEmergencyDTO = emergencyPostSVC.modify(emergency, eEditForm.getFiles());//게시글 수정 - 파일첨부시
        }

        redirectAttributes.addAttribute("eid", modifiedEmergencyDTO.getEid());

        return "redirect:/emergency/{eid}/detail";
    }


    //  삭제처리
    @DeleteMapping("{eid}")
    public String del(@PathVariable Long eid){
        emergencyPostSVC.remove(eid);
        return "redirect:/emergency/all";
    }

    //  전체목록
    @GetMapping({"/all",
        "/all/{reqPage}",
        "/all/{reqPage}/{searchType}/{keyword}"})
    public String list(
        @PathVariable(required = false) Optional<Integer> reqPage,
        @PathVariable(required = false) Optional<String> searchType,
        @PathVariable(required = false) Optional<String> keyword,
        Model model , HttpSession session){

        log.info("/list 요청됨{},{},{},{}",reqPage,searchType,keyword);

        //FindCriteria 값 설정
        fc.getRc().setReqPage(reqPage.orElse(1)); //요청페이지, 요청없으면 1
        fc.setSearchType(searchType.orElse(""));  //검색유형
        fc.setKeyword(keyword.orElse(""));        //검색어

        List<EmergencyPostDto> list = null;
        //검색어 있음
        if(searchType.isPresent() && keyword.isPresent()){
            EmergencyFilterCondition filterCondition = new EmergencyFilterCondition(
                fc.getRc().getStartRec(), fc.getRc().getEndRec(),
                searchType.get(),
                keyword.get());
            fc.setTotalRec(emergencyPostSVC.totalCount(filterCondition));
            fc.setSearchType(searchType.get());
            fc.setKeyword(keyword.get());
            list = emergencyPostSVC.findAll(filterCondition);

            //검색어 없음
        }else {
            //총레코드수
            fc.setTotalRec(emergencyPostSVC.totalCount());
            list = emergencyPostSVC.findAll(fc.getRc().getStartRec(), fc.getRc().getEndRec());
        }

        List<EItem> emergencys = new ArrayList<>();
        for (EmergencyPostDto emergency : list) {
            EItem item = new EItem();
            item.setEid(emergency.getEid());
            item.setETitle(emergency.getEtitle());

            //날짜 포맷
            LocalDate emergencyDate = emergency.getEupdatedAt().toLocalDate();
            LocalDate today = LocalDate.now();
            if(emergencyDate.equals(today)){//오늘 작성된 글이면
                item.setEUDate(emergency.getEupdatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }else{//오늘 이전에 작성된 글이면
                item.setEUDate(emergency.getEupdatedAt().toLocalDate().toString());
            }
            emergencys.add(item);
        }

        model.addAttribute("emergencys", emergencys);
        model.addAttribute("fc",fc);

        return "emergency/emergencyList";
    }
}