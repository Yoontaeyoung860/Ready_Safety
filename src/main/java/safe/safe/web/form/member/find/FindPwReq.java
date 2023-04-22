package safe.safe.web.form.member.find;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class FindPwReq {


  @NotBlank
  private String pwid;

  @NotBlank
  private String pwname;

  @NotBlank
  @Email
  private String pwemail;
}
