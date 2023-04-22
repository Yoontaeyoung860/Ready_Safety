package safe.safe.dao;


import safe.safe.dto.board.BoardFilterCondition;
import safe.safe.dto.board.BoardDTO;

import java.util.List;

public interface BoardDAO {

  List<BoardDTO> selectAll();


  List<BoardDTO> selectAll(int startRec, int endRec);


  List<BoardDTO>  findAll(BoardFilterCondition filterCondition);


  BoardDTO selectOne(Long bnum);


  BoardDTO create(BoardDTO board);


  BoardDTO update(BoardDTO board);


  int deleteBoard(Long bnum, String bid);

  int deleteContentOfBoard(Long bnum, String bid);


  int updateHit(Long bnum);


  int totalCount();


  int totalCount(BoardFilterCondition filterCondition);
}
