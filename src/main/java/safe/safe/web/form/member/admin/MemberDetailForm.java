package safe.safe.web.form.member.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDetailForm {

  private String id;

  private String name;

  private String nickname;

  private String email;

  private LocalDateTime signup_dt;

  private int leave_fl;

  private LocalDateTime leave_dt;


}
