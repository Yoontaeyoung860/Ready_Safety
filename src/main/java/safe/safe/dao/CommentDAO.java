package safe.safe.dao;


import safe.safe.dto.board.CommentDTO;

import java.util.List;

public interface CommentDAO {

  /**
   * 게시글번호로 댓글 전체 조회
   * @param cbnum 게시글번호
   * @return 전체 댓글
   */
  List<CommentDTO> selectAll(Long cbnum);

  /**
   * 댓글번호로 댓글 단건 조회
   * @param cnum 댓글번호
   * @return 단건 댓글
   */
  CommentDTO selectOne(Long cnum);

  /**
   * 댓글등록
   * @param comment 댓글
   * @return 댓글
   */
  CommentDTO create(CommentDTO comment);

  /**
   * 댓글수정
   * @param comment 댓글
   * @return 댓글
   */
  CommentDTO update(CommentDTO comment);

  /**
   * 댓글 삭제
   * @param cnum
   * @param cid
   * @return
   */
  int delete(Long cnum, String cid);

  /**
   * 게시글번호로 댓글 개수 조회
   * @param cbnum
   * @return
   */
  int eachCount(Long cbnum);
}
