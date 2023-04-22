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
public class EditForm {
  private Long bnum;
  @NotBlank
  @Size(max = 20)
  private String btitle;
  @NotBlank
  private String bcontent;

  private List<MultipartFile> files;  // 첨부파일
}
