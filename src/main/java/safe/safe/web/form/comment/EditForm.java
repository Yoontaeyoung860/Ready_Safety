package safe.safe.web.form.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class EditForm {
  private Long cnum;            //댓글번호
  private String cid;           //작성자
  private String ccontent;      //댓글본문
}
