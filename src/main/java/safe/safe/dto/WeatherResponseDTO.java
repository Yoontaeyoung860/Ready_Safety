package safe.safe.dto;

import lombok.Data;

@Data
public class WeatherResponseDTO
{
  private WeatherHeaderDTO header;

  private WeatherBodyDTO   body;
}