package safe.safe.dto.board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmergencyPostDto {
    private Long eid;         //응급처치법 식별번호
    private String etitle;    //응급처치법 제목
    private String ecategory; //응급처치법 카테고리
    private String econtent;  //응급처치법 본문
    private LocalDateTime ecreatedAt; //응급처치법 생성일시
    private LocalDateTime eupdatedAt; //응급처치법 수정일시

}
