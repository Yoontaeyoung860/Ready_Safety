package safe.safe.web.form.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class numberOfComment {
  private Long cbnum;              //게시글번호
  private int count;               //게시글의 댓글 개수
}
