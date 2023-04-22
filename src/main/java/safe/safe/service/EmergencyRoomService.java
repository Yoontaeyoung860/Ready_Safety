package safe.safe.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import safe.safe.dto.EmergencyRoom;

import java.io.IOException;
import java.util.List;

@Service
public class EmergencyRoomService {
  private final EmergencyRoomRepository emergencyRoomRepository;
  private final GeoApiContext geoApiContext;

  @Autowired
  public EmergencyRoomService(EmergencyRoomRepository emergencyRoomRepository, @Value("${google.maps.api-key}") String googleMapsApiKey) {
    this.emergencyRoomRepository = emergencyRoomRepository;
    this.geoApiContext = new GeoApiContext.Builder()
        .apiKey(googleMapsApiKey)
        .build();
  }

  public EmergencyRoom saveEmergencyRoom(EmergencyRoom emergencyRoom) {
    // 응급실 정보 저장
    EmergencyRoom savedEmergencyRoom = emergencyRoomRepository.save(emergencyRoom);

    // Google Maps API를 사용하여 위도와 경도 검색
    try {
      GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, savedEmergencyRoom.getDutyAddr()).await();
      if (results.length > 0) {
        LatLng location = results[0].geometry.location;
        savedEmergencyRoom.setLatitude(location.lat);
        savedEmergencyRoom.setLongitude(location.lng);

        // 위도와 경도를 업데이트한 응급실 정보 저장
        emergencyRoomRepository.save(savedEmergencyRoom);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return savedEmergencyRoom;
  }
  public List<EmergencyRoom> getAllEmergencyRooms() {
    return (List<EmergencyRoom>) emergencyRoomRepository.findAll();
  }
  public Page<EmergencyRoom> getAllEmergencyRooms(Pageable pageable) {
    return emergencyRoomRepository.findAll(pageable);
  }
}