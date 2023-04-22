package safe.safe.web.form.member.find;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class FindIdReq {


  @NotBlank
  private String idname;


  @NotBlank
  @Email
  private String idemail;

}
