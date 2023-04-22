package safe.safe.dao;

import safe.safe.dto.AreaRequestDTO;
import safe.safe.dto.WeatherDTO;

import java.util.List;
import java.util.Map;



public interface WeatherDAO {
  List<AreaRequestDTO> selectArea(Map<String, String> params);

  AreaRequestDTO selectCoordinate(String areacode);

  List<WeatherDTO> selectSameCoordinateWeatherList(AreaRequestDTO areaRequestDTO);

  void insertWeatherList(List<WeatherDTO> weatherList);
}