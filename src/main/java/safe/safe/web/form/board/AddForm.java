package safe.safe.web.form.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter@Setter
@ToString
public class AddForm {
  @NotBlank
  @Size(max = 20)
  private String btitle;        //제목
  @NotBlank
  private String bcontent;      //본문

  private List<MultipartFile> files;  // 첨부파일
}
