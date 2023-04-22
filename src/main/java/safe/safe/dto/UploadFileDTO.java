package safe.safe.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UploadFileDTO {
  private Long fnum;                    // 첨부파일번호
  private Long rnum;                    // 참조번호(게시글번호 등)
  private String code;                  // 카테고리코드
  private String store_filename;        // 로컬파일명
  private String upload_filename;       // 업로드파일명
  private String fsize;                 // 파일크기(단위 byte)
  private String ftype;                 // 파일타입(mimetype)
  private LocalDateTime fcdate;         // 첨부날자
  private LocalDateTime fudate;         // 첨부수정날자
}
