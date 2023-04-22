package safe.safe.web.form.member.find;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class FindTest {
  @NotBlank
  private String iname;
  @NotBlank
  @Email
  private String iemail;
  @NotBlank
  private String pid;
  @NotBlank
  private String pname;
  @NotBlank
  @Email
  private String pemail;

}

