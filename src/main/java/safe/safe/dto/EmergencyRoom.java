package safe.safe.dto;

import lombok.*;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
@NoArgsConstructor   // 디폴트 생성자 자동 생성해준다.
@AllArgsConstructor  // 모든멤버필드를 매개값으로 받아 생성자를 자동 만들어준다.
@Data
@Entity
public class EmergencyRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String dutyName;
  private String dutyAddr;
  private String dutyTel1;
  private double latitude;
  private double longitude;

}
