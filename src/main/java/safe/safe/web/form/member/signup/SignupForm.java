package safe.safe.web.form.member.signup;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignupForm {

  @NotBlank
  @Size(min=4)
  @Pattern(regexp = "^[A-Za-z0-9]{4,}$", message = "영문 대문자 또는 소문자 또는 숫자 4자리 이상")
  private String id;

  @NotBlank
  @Size(min=8, max=15)
//  @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%&*()_+-=|,./?><])[A-Za-z\\d~!@#$%&*()_+-=|,./?><]{8,15}$", message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
  private String pw;

  @NotBlank
  @Size(min=8, max=15)
//  @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%&*()_+-=|,./?><])[A-Za-z\\d~!@#$%&*()_+-=|,./?><]{8,15}$", message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
  private String pwChk;

  @NotBlank
  @Size(min=1, max=8)
  @Pattern(regexp = "^[가-힝]*$", message = "한글로 4~8자 이내")
  private String name;

  @NotBlank
  @Email
  @Size(min=4, max=50)
  private String email;

  @NotBlank
  private String nickname;

//  private LocalDateTime signup_dt;
//
//  private String leave_fl;
//
//  private LocalDateTime leave_dt;




  //확인용

}
