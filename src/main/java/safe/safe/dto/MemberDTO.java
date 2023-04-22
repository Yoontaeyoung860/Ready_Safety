package safe.safe.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter              // 모든멤버필드의 getter 메소드를 자동 생성해준다.
@Setter              // 모든멤버필드의 setter 메소드를 자동 생성해준다.
@NoArgsConstructor   // 디폴트 생성자 자동 생성해준다.
@AllArgsConstructor  // 모든멤버필드를 매개값으로 받아 생성자를 자동 만들어준다.
@ToString
@Data
public class MemberDTO {

    private String id;                      // 아이디 id VARCHAR2(40) not null
    private String pw;                      // 비밀번호 pw VARCHAR2(70) not null  비밀번호 암호화 적용
    private String name;                    // 이름 name VARCHAR2(20) not null
    private String email;                   // 이메일 email VARCHAR2(50) not null
    private String nickname;                // 닉네임 nickname VARCHAR2(30) not null
    private int admin_fl;                   // 관리자여부 2 관리자 3 사용자 admin_fl NUMBER(1) default 3
    private LocalDateTime signup_dt;        // 가입시간 signup_dt TIMESTAMP  default systimestamp
    private int leave_fl;                   // 탈퇴여부 0 회원 1 탈퇴 leave_fl NUMBER(1) default 0
    private LocalDateTime leave_dt;          // 탈퇴시간 leave_dt TIMESTAMP



}