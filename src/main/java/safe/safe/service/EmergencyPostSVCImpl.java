package safe.safe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import safe.safe.dao.EmergencyFilterCondition;
import safe.safe.dao.EmergencyPostDAO;
import safe.safe.dto.board.EmergencyPostDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmergencyPostSVCImpl implements EmergencyPostSVC {

    private final EmergencyPostDAO emergencyPostDAO;
    private final UploadFileSVC uploadFileSVC;


    /**
     * 등록
     * @param emergency
     * @return
     */
    @Override
    public Long write(EmergencyPostDto emergency) {
        return emergencyPostDAO.create(emergency);
    }
    /**
     * 등록 - 파일첨부시
     * @param emergency
     * @param files
     * @return
     */

    @Override
    public Long write(EmergencyPostDto emergency, List<MultipartFile> files) {
        //1)원글 저장
        Long rnum = write(emergency);

        //2)첨부 저장
        uploadFileSVC.addFile("C0102",rnum,files);

        return rnum;
    }
    /**
     * 전체조회
     * @return
     */
    @Override
    public List<EmergencyPostDto> findAll() {
        return emergencyPostDAO.findAll();
    }
    @Override
    public List<EmergencyPostDto> findAll(int startRec, int endRec) {
        return emergencyPostDAO.findAll(startRec,endRec);
    }

    @Override
    public List<EmergencyPostDto> findAll(EmergencyFilterCondition filterCondition) {
        return emergencyPostDAO.findAll(filterCondition);
    }
    /**
     * 상세조회
     * @param eid 응급처치 게시판 번호
     * @return 응급처치 게시판 상세
     */
    @Override
    public EmergencyPostDto findByEmergencyId(Long eid) {
        EmergencyPostDto emergency = emergencyPostDAO.selectOne(eid);
        return emergency;
    }

    /**
     * 수정
     * @param emergency
     * @return
     */
    @Override
    public EmergencyPostDto modify(EmergencyPostDto emergency) {
        return emergencyPostDAO.update(emergency);
    }
    /**
     * 수정 - 파일첨부시
     * @param emergency
     * @param files
     * @return
     */
    @Override
    public EmergencyPostDto modify(EmergencyPostDto emergency, List<MultipartFile> files) {
        //1) 게시글 수정
        EmergencyPostDto modifiedNotice = emergencyPostDAO.update(emergency);
        Long rnum = modifiedNotice.getEid();

        //2) 첨부파일 저장
        uploadFileSVC.addFile("C0102", rnum, files);

        return modifiedNotice;
    }

    /**
     * 삭제
     * @param eid
     * @return
     */
    @Override
    public int remove(Long eid) {
        //1) 첨부파일 삭제
        uploadFileSVC.deleteFileByCodeWithRnum("C0102", eid);

        return emergencyPostDAO.delete(eid);
    }
    //전체건수
    @Override
    public int totalCount() {
        return emergencyPostDAO.totalCount();
    }

    @Override
    public int totalCount(EmergencyFilterCondition filterCondition) {
        return emergencyPostDAO.totalCount(filterCondition);
    }

    @Override
    public List<EmergencyPostDto> getPostsByCategory(String categoryCode) {
        // 카테고리 코드에 따라 게시물 목록을 가져옵니다.
        List<EmergencyPostDto> emergencyPostDtoList = emergencyPostDAO.findByCategory(categoryCode);

        return emergencyPostDtoList;
    }


}