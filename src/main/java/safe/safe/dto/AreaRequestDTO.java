package safe.safe.dto;

import lombok.Data;

@Data
public class AreaRequestDTO
{
  // 행정구역코드
  private String areacode;
  // 시도
  private String step1;
  // 시군구
  private String step2;
  // 읍면동
  private String step3;
  // 발표 일자
  private String baseDate;
  // 발표 시각
  private String baseTime;
  // 예보지점 X좌표
  private String nx;
  // 예보지점 Y좌표
  private String ny;
}