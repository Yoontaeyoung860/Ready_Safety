package safe.safe.service;

import org.springframework.web.multipart.MultipartFile;
import safe.safe.dao.EmergencyFilterCondition;
import safe.safe.dto.board.EmergencyPostDto;

import java.util.List;

public interface EmergencyPostSVC {
    //등록
    Long write(EmergencyPostDto emergency);
    /**
     * 원글작성-첨부파일 있는경우
     * @param emergency
     * @param files 첨파일
     * @return 게시글 번호
     */
    Long write(EmergencyPostDto emergency, List<MultipartFile> files);

    //전체조회
    List<EmergencyPostDto> findAll();
    List<EmergencyPostDto> findAll(int startRec, int endRec);
    /**
     * 검색
     * @param filterCondition 분류,시작레코드번호,종료레코드번호,검색유형,검색어
     * @return
     */
    List<EmergencyPostDto>  findAll(EmergencyFilterCondition filterCondition);
    //상세
    EmergencyPostDto findByEmergencyId(Long eid);
    //수정
    EmergencyPostDto modify(EmergencyPostDto notice);

    /**
     * 수정 - 파일첨부시
     * @param emergency
     * @param files
     * @return
     */
    EmergencyPostDto modify(EmergencyPostDto emergency, List<MultipartFile> files);
    //삭제
    int remove(Long eid);

    //게시글 전체 건수
    int totalCount();
    int totalCount(EmergencyFilterCondition filterCondition);

    List<EmergencyPostDto> getPostsByCategory(String categoryCode);


}
