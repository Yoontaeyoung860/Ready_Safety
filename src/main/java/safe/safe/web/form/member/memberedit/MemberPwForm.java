package safe.safe.web.form.member.memberedit;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class MemberPwForm {

  @NotEmpty(message = "비밀번호를 입력하세요")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[ !@#$%&*()_+-=\\[\\]|,./?>< ])[A-Za-z\\d~!@#$%&*()_+-=\\[\\]|,./?><]{8,15}$", message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
  private String pw;
}

