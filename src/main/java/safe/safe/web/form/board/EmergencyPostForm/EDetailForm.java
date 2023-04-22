package safe.safe.web.form.board.EmergencyPostForm;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class EDetailForm {
    private Long eid;             //공지 아이디
    private String eTitle;         //제목
    private String eContent;       //본문
    private LocalDateTime ecreatedAt; //응급처치법 생성일시
    private LocalDateTime eupdatedAt; //응급처치법 수정일시

}
