package safe.safe.web.form.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailForm {
  private Long bnum;
  private String btitle;
  private String budate;
  private String bcontent;
  private Long bhit;
  private String bid;
  private String nickname;
}
