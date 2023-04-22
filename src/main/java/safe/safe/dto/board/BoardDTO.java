package safe.safe.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {
  private Long bnum;            //게시글번호
  private String btitle;        //제목
  private LocalDateTime bcdate; //작성일
  private LocalDateTime budate; //수정일
  private String bcontent;      //본문
  private Long bhit;            //조회수
  private String bstatus;       //게시글 상태(E:exist, D:delete)
  private String bid;           //작성자
  private String nickname;      //닉네임
  private int cnt;              //게시글별 댓글 개수
}
