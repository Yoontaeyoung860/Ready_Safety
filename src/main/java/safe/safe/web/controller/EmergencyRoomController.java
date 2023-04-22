package safe.safe.web.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import safe.safe.dto.EmergencyRoom;
import safe.safe.service.EmergencyRoomService;

import java.util.List;

@Controller
@RequestMapping("/emergency-rooms")
public class EmergencyRoomController {

  @Autowired
  private EmergencyRoomService emergencyRoomService;

  @Value("${open-api.url}")
  private String openApiUrl;

  @Value("${open-api.key}")
  private String openApiKey;

  @GetMapping("/fetch-data")
  public String fetchDataFromOpenApi() {
    RestTemplate restTemplate = new RestTemplate();
    String apiUrl = openApiUrl + "?ServiceKey=" + openApiKey;

    String response = restTemplate.getForObject(apiUrl, String.class);
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
    JsonArray jsonArray = jsonObject.getAsJsonObject("response").getAsJsonObject("body").getAsJsonArray("items");


    for (JsonElement element : jsonArray) {
      JsonObject item = element.getAsJsonObject();
      EmergencyRoom emergencyRoom = new EmergencyRoom();

      // Parse JSON data and set properties to emergencyRoom object
      emergencyRoom.setDutyAddr(item.get("dutyAddr").getAsString());
      emergencyRoom.setDutyName(item.get("dutyName").getAsString());
      emergencyRoom.setDutyTel1(item.get("dutyTel1").getAsString());
      emergencyRoom.setLatitude(item.get("latitude").getAsDouble());
      emergencyRoom.setLongitude(item.get("longitude").getAsDouble());

      emergencyRoomService.saveEmergencyRoom(emergencyRoom);
    }

    return "redirect:/emergency-rooms";
  }

  @PostMapping
  public String createEmergencyRoom(EmergencyRoom emergencyRoom) {
    emergencyRoomService.saveEmergencyRoom(emergencyRoom);
    return "redirect:/emergency-rooms";
  }


  @GetMapping("/create")
  public String showCreateForm() {
    return "create-emergency-room";
  }
  @GetMapping("")
  public String showEmergencyRooms() {
    return "emergency-rooms";
  }
  @GetMapping("/api")
  @ResponseBody

  public Page<EmergencyRoom> getAllEmergencyRooms(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return emergencyRoomService.getAllEmergencyRooms(pageable);
  }
  @GetMapping("/api/remote")
  @ResponseBody
  public ResponseEntity<String> getRemoteEmergencyRooms() {
    String endpointUrl = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytLcinfoInqire";
    String apiKey = "8Ek2lFFKv0iJm16TOGBd3a19KA65sZoech6w7u9w2WW+xwUZo0RGo521yza3x+RzVifL97AwiIArMvvyxDes6A==";

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/xml");

    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpointUrl)
        .queryParam("WGS84_LON", "127.085156592737")
        .queryParam("WGS84_LAT", "37.4881325624879")
        .queryParam("pageNo", "1")
        .queryParam("numOfRows", "10")
        .queryParam("serviceKey", apiKey);

    ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

    return response;
  }
}