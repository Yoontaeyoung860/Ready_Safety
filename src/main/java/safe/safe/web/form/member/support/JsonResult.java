package safe.safe.web.form.member.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult<T> {

  private String rtcd;
  private String rtmsg;
  private T data;
}

