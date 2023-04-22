package safe.safe.dto;

import lombok.Data;

@Data
public class WeatherDTO
{
  // 발표 일자
  private String baseDate;

  // 발표 시각
  private String baseTime;

  // 자료구분코드
  private String category;

  // 예보지점 X좌표
  private String nx;

  // 예보지점 Y좌표
  private String ny;

  // 실황 값
  private Double obsrValue;
}