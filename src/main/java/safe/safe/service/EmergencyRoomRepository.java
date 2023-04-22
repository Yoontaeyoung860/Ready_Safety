package safe.safe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import safe.safe.dto.EmergencyRoom;

public interface EmergencyRoomRepository extends PagingAndSortingRepository<EmergencyRoom, Long> {
  Page<EmergencyRoom> findAll(Pageable pageable);
}