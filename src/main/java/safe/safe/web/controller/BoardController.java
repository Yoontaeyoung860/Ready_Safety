package safe.safe.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import safe.safe.dto.board.BoardFilterCondition;
import safe.safe.dto.board.BoardDTO;
import safe.safe.service.BoardSVC;
import safe.safe.service.CommentSVC;
import safe.safe.dto.UploadFileDTO;
import safe.safe.web.form.board.AddForm;
import safe.safe.web.form.board.DetailForm;
import safe.safe.web.form.board.EditForm;
import safe.safe.web.form.board.ItemForm;
import safe.safe.paging.FindCriteria;
import safe.safe.service.UploadFileSVC;
import safe.safe.web.form.member.login.LoginMember;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/board")
public class BoardController {

  private final BoardSVC boardSVC;
  private final CommentSVC commentSVC;
  private final UploadFileSVC uploadFileSVC;

  @Autowired
  @Qualifier("fc10")
  private FindCriteria fc;

  //전체목록
  @GetMapping({"",
      "/{reqPage}",
      "/{reqPage}/{searchType}/{keyword}"})
  public String list(@PathVariable(required = false) Optional<Integer> reqPage,
                     @PathVariable(required = false) Optional<String> searchType,
                     @PathVariable(required = false) Optional<String> keyword,
                     Model model){
    log.info("list() 호출됨!");

    //FindCriteria 값 설정
    fc.getRc().setReqPage(reqPage.orElse(1)); //요청페이지, 요청없으면 1, 시작레코드, 종료레코드 set
    fc.setSearchType(searchType.orElse(""));  //검색유형
    fc.setKeyword(keyword.orElse(""));        //검색어

    List<BoardDTO> list = null;

    //검색어 있음
    if(searchType.isPresent() && keyword.isPresent()){
      BoardFilterCondition filterCondition = new BoardFilterCondition(
          fc.getRc().getStartRec(), fc.getRc().getEndRec(),
          searchType.get(),
          keyword.get());
      fc.setTotalRec(boardSVC.totalCount(filterCondition));
      fc.setSearchType(searchType.get());
      fc.setKeyword(keyword.get());
      list = boardSVC.findAll(filterCondition);

      //검색어 없음
    }else {
      //총레코드수
      fc.setTotalRec(boardSVC.totalCount());
      list = boardSVC.findAll(fc.getRc().getStartRec(), fc.getRc().getEndRec());
    }

    List<ItemForm> items =new ArrayList<>();
    for(BoardDTO boardDTO : list){
      ItemForm item = new ItemForm();
      //copyProperties 의 경우 필드명이 같아도 타입이 다르면 복사되지 않는다(budate=null)
      BeanUtils.copyProperties(boardDTO, item);

      //게시글 작성일
      LocalDate boardDate = boardDTO.getBudate().toLocalDate();
      //금일
      LocalDate today = LocalDate.now();

      //금일 작성글 -> HH:mm 포맷으로 출력
      if(boardDate.equals(today)){
        item.setBudate(boardDTO.getBudate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString());
        //금일 이전 작성글 -> YYYY-MM-DD 포맷으로 출력
      }else{
        item.setBudate(boardDTO.getBudate().toLocalDate().toString());
      }

      items.add(item);
    }

    model.addAttribute("items", items);
    model.addAttribute("fc",fc);

    return "board/listForm";
  }

  //상세화면
  @GetMapping("/{bnum}/detail")
  public String detail(@PathVariable Long bnum, Model model){
    log.info("detail() 호출됨!");

    //1) 게시글 조회
    BoardDTO boardDTO = boardSVC.findByBnum(bnum);
    DetailForm detailForm = new DetailForm();
    //copyProperties 의 경우 필드명이 같아도 타입이 다르면 복사되지 않는다(budate=null)
    BeanUtils.copyProperties(boardDTO, detailForm);

    //게시글 작성일
    LocalDate boardDate = boardDTO.getBudate().toLocalDate();
    //금일
    LocalDate today = LocalDate.now();
    //금일 작성글 -> HH:mm 포맷으로 출력
    if(boardDate.equals(today)){
      detailForm.setBudate(boardDTO.getBudate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString());
      //금일 이전 작성글 -> YYYY-MM-DD 포맷으로 출력
    }else{
      detailForm.setBudate(boardDTO.getBudate().toLocalDate().toString());
    }

    model.addAttribute("detailForm", detailForm);
    log.info("detailForm={}", detailForm);

    //2) 첨부파일 조회
    List<UploadFileDTO> attachFiles = uploadFileSVC.findFilesByCodeWithRnum("C0101", boardDTO.getBnum());
    if(attachFiles.size() > 0){
      log.info("attachFiles={}",attachFiles);
      model.addAttribute("attachFiles", attachFiles);
    }

    return "board/detailForm";
  }

  //작성모드
  @GetMapping("/add")
  public String add(@ModelAttribute AddForm addForm,
                    HttpServletRequest request
  ){
    log.info("add() 호출됨!");

    HttpSession session = request.getSession(false);
    log.info("referer={}", request.getHeader("referer"));

    //비로그인 상태인 경우 => 로그인 페이지로 이동
    if (session == null || session.getAttribute("loginMember") == null) {
      return "redirect:/login";
    }

    return "board/addForm";
  }

  //작성처리
  @PostMapping("/add")
  public String addBoard(@Valid @ModelAttribute AddForm addForm,
                         BindingResult bindingResult,      // 폼객체에 바인딩될때 오류내용이 저장되는 객체
                         HttpSession session,
                         RedirectAttributes redirectAttributes
  ){
    log.info("addBoard() 호출됨!");

    //1) error message
    if(bindingResult.hasErrors()){
      log.info("/board/add/bindingResult={}",bindingResult);
      return "board/addForm";
    }

    //2) BoardDTO 생성
    BoardDTO boardDTO = new BoardDTO();
    BeanUtils.copyProperties(addForm, boardDTO);
    //보안을 위해 세션에서 id 값을 받아온다 만약 비로그인상태거나 세션이 만료된 상태면 인터셉터에서 처리한다
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");//세션에서 로그인 정보 가져오기
    boardDTO.setBid(loginMember.getId());

    //3) 파일첨부유무별 게시글 저장
    BoardDTO writedBoardDTO;
    if(addForm.getFiles().size() == 0) {
      writedBoardDTO = boardSVC.write(boardDTO);//게시글 저장
    }else{
      writedBoardDTO = boardSVC.write(boardDTO, addForm.getFiles());//게시글 저장 - 파일첨부시
    }

    //4) 리다이렉트
    redirectAttributes.addAttribute("bnum", writedBoardDTO.getBnum());
    return "redirect:/board/{bnum}/detail";
  }

  //수정모드
  @GetMapping("/{bnum}/edit")
  public String edit(@PathVariable Long bnum,
                     Model model
  ){
    log.info("edit() 호출됨!");

    //1) 게시글 조회
    BoardDTO boardDTO = boardSVC.findByBnum(bnum);
    EditForm editForm = new EditForm();
    BeanUtils.copyProperties(boardDTO, editForm);
    model.addAttribute("editForm", editForm);

    //2) 첨부파일 조회
    List<UploadFileDTO> attachFiles = uploadFileSVC.findFilesByCodeWithRnum("C0101", boardDTO.getBnum());
    if(attachFiles.size() > 0){
      log.info("attachFiles={}",attachFiles);
      model.addAttribute("attachFiles", attachFiles);
    }

    return "board/editForm";
  }

  //수정처리
  @PatchMapping("/{bnum}/edit")
  public String editBoard(@Valid @ModelAttribute EditForm editForm,
                          BindingResult bindingResult,      // 폼객체에 바인딩될때 오류내용이 저장되는 객체
                          @PathVariable Long bnum,
                          HttpSession session,
                          RedirectAttributes redirectAttributes
  ){
    log.info("editBoard() 호출됨!");

    //1) error message
    if(bindingResult.hasErrors()){
      log.info("/board/add/bindingResult={}",bindingResult);
      return "board/editForm";
    }

    //2) BoardDTO 생성
    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setBnum(bnum);
    BeanUtils.copyProperties(editForm, boardDTO);
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");//세션에서 로그인 정보 가져오기
    boardDTO.setBid(loginMember.getId());

    //3) 파일첨부유무별 게시글 수정
    BoardDTO modifiedBoardDTO;
    if(editForm.getFiles().size() == 0) {
      modifiedBoardDTO = boardSVC.modify(boardDTO);//게시글 수정
    }else{
      modifiedBoardDTO = boardSVC.modify(boardDTO, editForm.getFiles());//게시글 수정 - 파일첨부시
    }

    //4)리다이렉트
    redirectAttributes.addAttribute("bnum", modifiedBoardDTO.getBnum());
    return "redirect:/board/{bnum}/detail";
  }

  //삭제처리
  @GetMapping("/{bnum}/del")
  public String delete(@PathVariable Long bnum,
                       HttpSession session
  ){
    log.info("del() 호출됨!");

    LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");

    log.info("댓글유무={}", commentSVC.findAll(bnum).size());

    //댓글 없는 게시글 완전 삭제
    if(commentSVC.findAll(bnum).size() == 0){
      log.info("게시글 완전 삭제");
      boardSVC.removeBoard(bnum, loginMember.getId());

      return "redirect:/board";

      //댓글 있는 게시글 본문 삭제
    }else{
      log.info("게시글 본문 삭제");
      boardSVC.removeContentOfBoard(bnum, loginMember.getId());

      return "redirect:/board/{bnum}/detail";
    }
  }

}
