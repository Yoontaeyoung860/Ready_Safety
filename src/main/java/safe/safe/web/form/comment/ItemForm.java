package safe.safe.web.form.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class ItemForm {
  private Long cnum;            //댓글번호
  private String cid;           //작성자
  private String nickname;      //닉네임
  private String ccontent;      //댓글본문
  private String cudate;        //날짜
}
