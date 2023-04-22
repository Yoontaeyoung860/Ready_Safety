package safe.safe.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDTO {
  private Long cnum;              //댓글번호
  private String cid;             //작성자
  private String nickname;        //닉네임
  private String ccontent;        //댓글본문
  private LocalDateTime ccdate;   //댓글작성일
  private LocalDateTime cudate;   //댓글수정일
  private Long cbnum;             //게시글번호
}
