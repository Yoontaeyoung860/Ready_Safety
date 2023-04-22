package safe.safe.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import safe.safe.dto.board.CommentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentDAOImpl implements CommentDAO {

  private final JdbcTemplate jdbcTemplate;

  /**
   * 게시글번호로 댓글 전체 조회
   * @param cbnum 게시글번호
   * @return 전체 댓글
   */
  @Override
  public List<CommentDTO> selectAll(Long cbnum) {

    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" select row_number() over (order by ccdate) as num, cnum, cbnum, ccontent, ccdate, cudate, cid, nickname ");
    sql.append(" from comments, member ");
    sql.append(" where comments.cid = member.id and cbnum = ? ");
    sql.append(" order by ccdate asc ");

    //sql 실행
    List<CommentDTO> list = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CommentDTO.class), cbnum);

    return list;
  }//end of selectAll

  /**
   * 댓글번호로 댓글 단건 조회
   * @param cnum 댓글번호
   * @return 단건 댓글
   */
  @Override
  public CommentDTO selectOne(Long cnum) {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" select cnum, ccdate, cudate, ccontent, cid, cbnum, nickname ");
    sql.append(" from comments, member ");
    sql.append(" where comments.cid = member.id and cnum = ? ");

    //sql 실행
    List<CommentDTO> query = jdbcTemplate.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(CommentDTO.class),
            cnum
    );

    return (query.size() == 1) ? query.get(0) : null;
  }//end of selectOne

  /**
   * 댓글등록
   * @param comment 댓글
   * @return 댓글
   */
  @Override
  public CommentDTO create(CommentDTO comment) {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into comments (cnum, ccontent, cid, cbnum) ");
    sql.append(" values (comments_cnum_seq.nextval, ?, ?, ?) ");

//    //sql 실행
//    List<CommentDTO> query = jdbcTemplate.query(
//            sql.toString(),
//            new BeanPropertyRowMapper<>(CommentDTO.class),
//            comment.getCcontent(),
//            comment.getCid(),
//            comment.getCbnum());
//
//    return (query.size() == 1)? query.get(0) : null;

    //sql 실행
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(
                sql.toString(),               //1. sql 문 db 에서 실행
                new String[]{"cnum"}          //2. insert 후 insert 레코드 중 반환할 컬럼명(시퀀스) KeyHolder 에 저장됨
        );

        pstmt.setString(1, comment.getCcontent());
        pstmt.setString(2, comment.getCid());
        pstmt.setLong(3, comment.getCbnum());

        return pstmt;
      }
    }, keyHolder);//notice_id 를 keyHolder 에 담아 반환(Map<>인가?)

    Long cnum = Long.valueOf(keyHolder.getKeys().get("cnum").toString());
    return selectOne(cnum);
  }//end of create

  /**
   * 댓글수정
   * @param comment 댓글
   * @return 댓글
   */
  @Override
  public CommentDTO update(CommentDTO comment) {

    StringBuffer sql = new StringBuffer();
    sql.append(" update comments ");
    sql.append(" set ccontent = ? , ");
    sql.append("     cudate = systimestamp ");
    sql.append(" where cnum = ? and cid = ? ");

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(
                sql.toString(),
                new String[]{"cnum"}
        );

        pstmt.setString(1, comment.getCcontent());
        pstmt.setLong(2, comment.getCnum());
        pstmt.setString(3, comment.getCid());

        return pstmt;
      }
    }, keyHolder);

    Long cnum = Long.valueOf(keyHolder.getKeys().get("cnum").toString());
    return selectOne(cnum);
  }//end of update

  /**
   * 댓글 삭제
   * @param cnum
   * @param cid
   * @return
   */
  @Override
  public int delete(Long cnum, String cid) {

    StringBuffer sql = new StringBuffer();
    sql.append(" delete from comments ");
    sql.append("  where cnum =? and cid = ? ");

    int result = jdbcTemplate.update(sql.toString(), cnum, cid);//성공시 1 실패시 0

    return result;
  }//end of delete

  /**
   * 게시글번호로 댓글 개수 조회
   * @param cbnum
   * @return
   */
  @Override
  public int eachCount(Long cbnum) {
    //sql문 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" select count(*) ");
    sql.append("   from comments  ");
    sql.append("  where cbnum = ? ");

    //sql문 실행
    Integer cnt = jdbcTemplate.queryForObject(sql.toString(), Integer.class, cbnum);

    return cnt;
  }
}
