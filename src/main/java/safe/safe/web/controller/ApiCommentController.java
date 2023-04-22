package safe.safe.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import safe.safe.dto.board.CommentDTO;
import safe.safe.service.CommentSVC;
import safe.safe.web.form.comment.ApiResult;
import safe.safe.web.form.comment.*;
import safe.safe.web.form.member.login.LoginMember;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class ApiCommentController {

  private final CommentSVC commentSVC;

  //댓글 전체 조회
  @GetMapping("/{cbnum}")
  public ApiResult<Object> list(@PathVariable Long cbnum) {
    log.info("list() 호출됨!");

    List<CommentDTO> list = commentSVC.findAll(cbnum);

    List<ItemForm> items = new ArrayList<>();
    for (CommentDTO commentDTO : list) {
      ItemForm item = new ItemForm();
      BeanUtils.copyProperties(commentDTO, item);

      //댓글 작성일
      LocalDate commentDate = commentDTO.getCudate().toLocalDate();
      //금일
      LocalDate today = LocalDate.now();

      //금일 작성글 -> HH:mm 포맷으로 출력
      if (commentDate.equals(today)) {
        item.setCudate(commentDTO.getCudate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString());
        //금일 이전 작성글 -> YYYY-MM-DD 포맷으로 출력
      } else {
        item.setCudate(commentDTO.getCudate().toLocalDate().toString());
      }

      items.add(item);
    }

    if(items.size() != 0){
      return new ApiResult<>("00", "댓글 전체 조회 성공", items);
    }else{
      return new ApiResult<>("99", "등록된 댓글이 없습니다.", items);
    }
  }//end of list

  //댓글 단건 조회
  @GetMapping("/{cnum}/detail")
  public ApiResult<Object> detail(@PathVariable Long cnum){
    log.info("detail() 호출됨!");

    CommentDTO commentDTO = commentSVC.findByCnum(cnum);
    DetailForm detailForm = new DetailForm();
    BeanUtils.copyProperties(commentDTO, detailForm);

    //댓글 작성일
    LocalDate commentDate = commentDTO.getCudate().toLocalDate();
    //금일
    LocalDate today = LocalDate.now();

    //금일 작성글 -> HH:mm 포맷으로 출력
    if (commentDate.equals(today)) {
      detailForm.setCudate(commentDTO.getCudate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString());
      //금일 이전 작성글 -> YYYY-MM-DD 포맷으로 출력
    } else {
      detailForm.setCudate(commentDTO.getCudate().toLocalDate().toString());
    }

    return new ApiResult<>("00", "댓글 단건 조회 성공", detailForm);
  }//end of detail

  //댓글 등록
  @PostMapping("/{cbnum}")
  public ApiResult<Object> add(@PathVariable Long cbnum,
                               @RequestBody AddForm addForm,
                               HttpSession session){
    log.info("add() 호출됨!");

    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setCcontent(addForm.getCcontent());
    commentDTO.setCbnum(cbnum);
    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");//세션에서 로그인 정보 가져오기
    commentDTO.setCid(loginMember.getId());

    CommentDTO savedComment = commentSVC.write(commentDTO);

    return new ApiResult<>("00", "댓글 등록 성공", savedComment);
  }//end of add

  //댓글 수정
  @PatchMapping("/{cbnum}")
  public ApiResult<Object> edit(@PathVariable Long cbnum,
                                @RequestBody EditForm editForm,
                                HttpSession session){
    log.info("edit() 호출됨!");

    LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");
    if(!loginMember.getId().equals(editForm.getCid())){
      log.info("edit() : 아이디 불일치");
      return null;
    }

    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setCnum(editForm.getCnum());
    commentDTO.setCid(editForm.getCid());
    commentDTO.setCcontent(editForm.getCcontent());

    CommentDTO modifiedComment = commentSVC.modify(commentDTO);

    return new ApiResult<>("00", "댓글 수정 성공", modifiedComment);
  }//end of edit

  //댓글 삭제
  @DeleteMapping("/{cnum}")
  public ApiResult<Integer> delete(@PathVariable Long cnum,
                                   HttpSession session){
    log.info("delete() 호출됨!");

    LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
    int result = commentSVC.remove(cnum, loginMember.getId());
    ApiResult<Integer> apiResult = null;

    if(result == 1){
      apiResult = new ApiResult<>("00", "댓글 삭제 성공", result);
      log.info("apiResult = {}", apiResult);
    }else{
      apiResult = new ApiResult<>("99", "댓글 삭제 실패", result);
      log.info("apiResult = {}", apiResult);
    }

    return apiResult;
  }
}
