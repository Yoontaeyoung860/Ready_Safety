package safe.safe.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import safe.safe.dao.BoardDAO;
import safe.safe.dto.board.BoardFilterCondition;
import safe.safe.dto.board.BoardDTO;
import safe.safe.service.BoardSVC;
import safe.safe.service.UploadFileSVC;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC {

  private final BoardDAO boardDAO;
  private final UploadFileSVC uploadFileSVC;

  /**
   * 전체조회
   * @return 게시글 List
   */
  @Override
  public List<BoardDTO> findAll() {
    return boardDAO.selectAll();
  }

  /**
   * 전체조회 - 페이징
   * @param startRec
   * @param endRec
   * @return
   */
  @Override
  public List<BoardDTO> findAll(int startRec, int endRec) {
    return boardDAO.selectAll(startRec, endRec);
  }


  @Override
  public List<BoardDTO> findAll(BoardFilterCondition filterCondition) {
    return boardDAO.findAll(filterCondition);
  }


  @Override
  public BoardDTO findByBnum(Long bnum) {
    //1) 상세조회한 DTO
    BoardDTO boardDTO = boardDAO.selectOne(bnum);

    //2) 조회수 증가
    boardDAO.updateHit(bnum);

    return boardDTO;
  }


  @Override
  public BoardDTO write(BoardDTO board) {
    return boardDAO.create(board);
  }


  @Override
  public BoardDTO write(BoardDTO board, List<MultipartFile> files) {
    //1) 게시글 저장
    BoardDTO savedBoard = boardDAO.create(board);
    Long rnum = savedBoard.getBnum();

    //2) 첨부파일 저장
    uploadFileSVC.addFile("C0101", rnum, files);

    return savedBoard;
  }

  @Override
  public BoardDTO modify(BoardDTO board) {
    return boardDAO.update(board);
  }


  @Override
  public BoardDTO modify(BoardDTO board, List<MultipartFile> files) {
    //1) 게시글 수정
    BoardDTO modifiedBoard = boardDAO.update(board);
    Long rnum = modifiedBoard.getBnum();

    //2) 첨부파일 저장
    uploadFileSVC.addFile("C0101", rnum, files);

    return modifiedBoard;
  }


  @Override
  public int removeBoard(Long bnum, String bid) {
    //1) 첨부파일 삭제
    uploadFileSVC.deleteFileByCodeWithRnum("C0101", bnum);

    //2) 게시글 삭제
    return boardDAO.deleteBoard(bnum, bid);
  }

  /**
   * 댓글 있는 게시글 삭제
   * @param bnum
   * @return
   */
  @Override
  public int removeContentOfBoard(Long bnum, String bid) {
    //1) 첨부파일 삭제
    uploadFileSVC.deleteFileByCodeWithRnum("C0101", bnum);

    //2) 게시글 삭제
    return boardDAO.deleteContentOfBoard(bnum, bid);
  }

  /**
   * 조회수 증가
   * @param bnum
   * @return
   */
  @Override
  public int increaseHit(Long bnum) {
    return boardDAO.updateHit(bnum);
  }

  /**
   * 전체건수
   * @return 게시글 전체건수
   */
  @Override
  public int totalCount() {
    return boardDAO.totalCount();
  }

  /**
   * 전체건수 - 검색
   * @param filterCondition 시작레코드번호, 종료레코드번호, 검색유형, 검색어
   * @return
   */
  @Override
  public int totalCount(BoardFilterCondition filterCondition) {
    return boardDAO.totalCount(filterCondition);
  }
}
