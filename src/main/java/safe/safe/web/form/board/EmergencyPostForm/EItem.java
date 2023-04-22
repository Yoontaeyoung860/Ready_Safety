package safe.safe.web.form.board.EmergencyPostForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EItem {
  private Long eid;
  private String eTitle;
  private String eCDate;
  private String eUDate;
}
