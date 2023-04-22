package safe.safe.service;


import safe.safe.dto.board.CommentDTO;

import java.util.List;

public interface CommentSVC {

  List<CommentDTO> findAll(Long cbnum);

  CommentDTO findByCnum(Long cnum);

  CommentDTO write(CommentDTO comment);

  CommentDTO modify(CommentDTO comment);

  int remove(Long cnum, String cid);

  int eachCount(Long cbnum);
}
