package safe.safe.web.form.member.out;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OutForm {

  private String id;

  private String name;

  private String nickname;

  private String email;

  @NotBlank
  private String pw;

  private Boolean agree;


}