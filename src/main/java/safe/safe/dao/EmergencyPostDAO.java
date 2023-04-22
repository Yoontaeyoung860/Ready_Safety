package safe.safe.dao;

import safe.safe.dto.board.EmergencyPostDto;

import java.util.List;

public interface EmergencyPostDAO {
    //등록
    Long create(EmergencyPostDto emergency);
    //카테고리별 조회
    List<EmergencyPostDto> findByCategory(String ecategoryCode);
    //전체조회
    List<EmergencyPostDto> findAll();
    List<EmergencyPostDto> findAll(int startRec, int endRec);
    //검색
    List<EmergencyPostDto> findAll(EmergencyFilterCondition filterCondition);
    //상세
    EmergencyPostDto selectOne(Long eid);
    //수정
    EmergencyPostDto update(EmergencyPostDto emergency);
    //삭제
    int delete(Long eid);
    //게시글 전체건수
    int totalCount();
    int totalCount(EmergencyFilterCondition filterCondition);
}
