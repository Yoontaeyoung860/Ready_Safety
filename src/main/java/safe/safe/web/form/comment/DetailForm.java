package safe.safe.web.form.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter@Setter
@ToString
public class DetailForm {
  private Long cnum;              //댓글번호
  private String cid;             //작성자
  private String nickname;        //닉네임
  private String ccontent;        //댓글본문
  private String cudate;   //댓글수정일
}
