package safe.safe.service;


import org.springframework.web.multipart.MultipartFile;
import safe.safe.dto.board.BoardFilterCondition;
import safe.safe.dto.board.BoardDTO;

import java.util.List;

public interface BoardSVC {
  /**
   * 전체조회
   * @return
   */
  List<BoardDTO> findAll();

  /**
   * 전체조회 - 페이징
   * @param startRec
   * @param endRec
   * @return
   */
  List<BoardDTO>  findAll(int startRec, int endRec);

  /**
   * 전체조회 - 검색
   * @param filterCondition 시작레코드번호, 종료레코드번호, 검색유형, 검색어
   * @return
   */
  List<BoardDTO>  findAll(BoardFilterCondition filterCondition);

  /**
   * 상세조회
   * @param bnum
   * @return
   */
  BoardDTO findByBnum(Long bnum);

  /**
   * 등록
   * @param board
   * @return
   */
  BoardDTO write(BoardDTO board);

  /**
   * 등록 - 파일첨부시
   * @param board
   * @param files
   * @return
   */
  BoardDTO write(BoardDTO board, List<MultipartFile> files);

  /**
   * 수정
   * @param board
   * @return
   */
  BoardDTO modify(BoardDTO board);

  /**
   * 수정 - 파일첨부시
   * @param board
   * @param files
   * @return
   */
  BoardDTO modify(BoardDTO board, List<MultipartFile> files);

  /**
   * 댓글 없는 게시글 삭제
   * @param bnum
   * @param bid
   * @return
   */
  int removeBoard(Long bnum, String bid);

  /**
   * 댓글 있는 게시글 삭제
   * @param bnum
   * @param bid
   * @return
   */
  int removeContentOfBoard(Long bnum, String bid);

  /**
   * 조회수 증가
   * @param bnum
   * @return
   */
  int increaseHit(Long bnum);

  /**
   * 전체건수
   * @return 게시글 전체건수
   */
  int totalCount();

  /**
   * 전체건수 - 검색
   * @param filterCondition 시작레코드번호, 종료레코드번호, 검색유형, 검색어
   * @return
   */
  int totalCount(BoardFilterCondition filterCondition);
}
