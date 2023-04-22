package safe.safe.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;
import safe.safe.dto.AreaRequestDTO;
import safe.safe.dto.WeatherDTO;

import java.util.List;
import java.util.Map;
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeatherDAOImpl implements WeatherDAO {

  private NamedParameterJdbcTemplate jdbcTemplate;

  @Autowired
  public WeatherDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<AreaRequestDTO> selectArea(Map<String, String> params) {
    String sql = "SELECT areacode, step1 FROM tb_weather_area WHERE step2 = '' AND step3 = '' ORDER BY step1";
    return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(AreaRequestDTO.class));
  }

  @Override
  public AreaRequestDTO selectCoordinate(String areacode) {
    String sql = "SELECT gridX as nx, gridY as ny FROM tb_weather_area WHERE areacode = :areacode";
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("areacode", areacode);
    return jdbcTemplate.queryForObject(sql, namedParameters, new BeanPropertyRowMapper<>(AreaRequestDTO.class));
  }

  @Override
  public List<WeatherDTO> selectSameCoordinateWeatherList(AreaRequestDTO areaRequestDTO) {
    String sql = "SELECT DISTINCT baseDate, baseTime, category, nx, ny, obsrValue FROM tw_weather_response WHERE baseDate = :baseDate AND baseTime = :baseTime AND nx = :nx AND ny = :ny";
    SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(areaRequestDTO);
    return jdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<>(WeatherDTO.class));
  }

  @Override
  public void insertWeatherList(List<WeatherDTO> weatherList) {
    String sql = "INSERT INTO tw_weather_response (baseDate, baseTime, category, nx, ny, obsrValue) VALUES (:baseDate, :baseTime, :category, :nx, :ny, :obsrValue)";
    SqlParameterSource[] namedParameters = SqlParameterSourceUtils.createBatch(weatherList.toArray());
    jdbcTemplate.batchUpdate(sql, namedParameters);
  }
}
