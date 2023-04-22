package safe.safe.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;
import safe.safe.dto.board.BoardFilterCondition;
import safe.safe.dto.board.BoardDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardDAOImpl implements BoardDAO {

  private final JdbcTemplate jdbcTemplate;
//  private JdbcTemplate jdbcTemplate;
//  public BoardDAOImpl(JdbcTemplate jdbcTemplate){
//    this.jdbcTemplate = jdbcTemplate;
//  }@RequiredArgsConstructor 로 생성

  @Override
  public List<BoardDTO> selectAll() {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT ");
    sql.append("   ROW_NUMBER() OVER (ORDER BY bcdate) AS num, ");
    sql.append("   bnum, ");
    sql.append("   btitle, ");
    sql.append("   bcdate, ");
    sql.append("   budate, ");
    sql.append("   bhit, ");
    sql.append("   nickname ");
    sql.append(" FROM ");
    sql.append("   Board, ");
    sql.append("   Member ");
    sql.append(" WHERE ");
    sql.append("   board.bid = member.id ");
    sql.append(" ORDER BY bcdate DESC ");

    //sql 실행
    List<BoardDTO> list = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(BoardDTO.class));

    return list;
  }


  @Override
  public List<BoardDTO> selectAll(int startRec, int endRec) {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT ");
    sql.append("   t1.* ");
    sql.append(" FROM( ");

    sql.append("     SELECT ");
    sql.append("       ROW_NUMBER() OVER (ORDER BY bcdate DESC) AS num, ");
    sql.append("       bnum, ");
    sql.append("       btitle, ");
    sql.append("       bcdate, ");
    sql.append("       budate, ");
    sql.append("       bhit, ");
    sql.append("       nickname, ");
    sql.append("       NVL(cnt, 0) AS cnt ");
    sql.append("     FROM ");
    sql.append("       Board ");
    sql.append("     INNER JOIN ");
    sql.append("       Member ");
    sql.append("     ON ");
    sql.append("       board.bid = member.id ");

    sql.append("     LEFT OUTER JOIN ( ");
    sql.append("       SELECT ");
    sql.append("         cbnum, ");
    sql.append("         count(*) AS cnt ");
    sql.append("       FROM ");
    sql.append("         Comments ");
    sql.append("       GROUP BY ");
    sql.append("         cbnum ");
    sql.append("     ) t2 ");
    sql.append("     ON ");
    sql.append("       board.bnum = t2.cbnum ");
    sql.append("   ) t1 ");

    sql.append(" WHERE t1.num BETWEEN ? AND ? ");

    //sql 실행
    List<BoardDTO> list = jdbcTemplate.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(BoardDTO.class),
            startRec,
            endRec
    );

    return list;
  }


  @Override
  public List<BoardDTO> findAll(BoardFilterCondition filterCondition) {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT ");
    sql.append("   t1.* ");
    sql.append(" FROM( ");

    sql.append("     SELECT ");
    sql.append("       ROW_NUMBER() OVER (ORDER BY bcdate DESC) AS num, ");
    sql.append("       bnum, ");
    sql.append("       btitle, ");
    sql.append("       bcdate, ");
    sql.append("       budate, ");
    sql.append("       bhit, ");
    sql.append("       nickname, ");
    sql.append("       NVL(cnt, 0) AS cnt ");
    sql.append("     FROM ");
    sql.append("       Board ");
    sql.append("     INNER JOIN ");
    sql.append("       Member ");
    sql.append("     ON ");
    sql.append("       Board.bid = Member.id ");

    sql.append("     LEFT OUTER JOIN ( ");
    sql.append("       SELECT ");
    sql.append("         cbnum, ");
    sql.append("         count(*) AS cnt ");
    sql.append("       FROM ");
    sql.append("         Comments ");
    sql.append("       GROUP BY ");
    sql.append("         cbnum ");
    sql.append("     ) t2 ");
    sql.append("     ON ");
    sql.append("       board.bnum = t2.cbnum ");

    //검색유형,검색어 존재시
    if(!StringUtils.isEmpty(filterCondition.getSearchType()) &&
       !StringUtils.isEmpty(filterCondition.getKeyword())){
      //sql문 검색 조건 추가
      sql = dynamicQuery(filterCondition, sql);
    }

    sql.append("   ) t1 ");
    sql.append(" WHERE t1.num BETWEEN ? AND ? ");

    //sql 실행
    List<BoardDTO> list = jdbcTemplate.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(BoardDTO.class),
            filterCondition.getStartRec(),
            filterCondition.getEndRec()
    );

    return list;
  }

  /**
   * 상세조회
   * @param bnum
   * @return 게시글 BoardDTO
   */
  @Override
  public BoardDTO selectOne(Long bnum) {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" select bnum, btitle, bcdate, budate, bcontent, bhit, bid, nickname ");
    sql.append(" from board, member ");
    sql.append(" where board.bid = member.id and bnum = ? ");

    //sql 실행
    List<BoardDTO> query = jdbcTemplate.query(
            sql.toString(),//1. sql 문
            new BeanPropertyRowMapper<>(BoardDTO.class),//2. 자동맵핑(setter 메소드를 통해 board 객체에 삽입)
            bnum//3. 조회할 게시글 번호
    );
    
    return (query.size() == 1)? query.get(0) : null;//찾은 게시글(인덱스0)반환
  }

  /**
   * 등록
   * @param board
   * @return
   */
  @Override
  public BoardDTO create(BoardDTO board) {
    //sql 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into board (bnum, bcategory, btitle, bcontent, bid) ");
    sql.append(" values (board_bnum_seq.nextval, 'C0101', ?, ?, ?) ");

    //sql 실행
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(
                sql.toString(),               //1. sql 문 db 에서 실행
                new String[]{"bnum"}          //2. insert 후 insert 레코드 중 반환할 컬럼명(시퀀스) KeyHolder 에 저장됨
        );

        pstmt.setString(1, board.getBtitle());
        pstmt.setString(2, board.getBcontent());
        pstmt.setString(3, board.getBid());

        return pstmt;
      }
    }, keyHolder);//notice_id 를 keyHolder 에 담아 반환(Map<>인가?)

    Long bnum = Long.valueOf(keyHolder.getKeys().get("bnum").toString());
    return selectOne(bnum);
  }

  /**
   * 수정
   * @param board
   * @return
   */
  @Override
  public BoardDTO update(BoardDTO board) {

    StringBuffer sql = new StringBuffer();
    sql.append(" update board ");
    sql.append(" set btitle = ? , ");
    sql.append("     bcontent = ? , ");
    sql.append("     budate = systimestamp ");
    sql.append(" where bnum = ? and bid = ? ");

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(
                sql.toString(),
                new String[]{"bnum"}
        );

        pstmt.setString(1, board.getBtitle());
        pstmt.setString(2, board.getBcontent());
        pstmt.setString(3, String.valueOf(board.getBnum()));
        pstmt.setString(4, board.getBid());

        return pstmt;
      }
    }, keyHolder);

    Long bnum = Long.valueOf(keyHolder.getKeys().get("bnum").toString());
    return selectOne(bnum);
  }

  /**
   * 댓글 없는 게시글 삭제
   * @param bnum
   * @return
   */
  @Override
  public int deleteBoard(Long bnum, String bid){

    StringBuffer sql = new StringBuffer();
    sql.append(" delete from board ");
    sql.append("  where bnum = ? and bid = ? ");

    int result = jdbcTemplate.update(sql.toString(), bnum, bid);//성공시 1 실패시 0

    return result;
  }

  /**
   * 댓글 있는 게시글 삭제
   * @param bnum
   * @return
   */
  @Override
  public int deleteContentOfBoard(Long bnum, String bid) {

    StringBuffer sql = new StringBuffer();
    sql.append(" update board ");
    sql.append(" set bstatus = 'D', bcontent = '삭제된 게시글입니다.' ");
    sql.append(" where bnum = ? and bid = ? ");

    int result = jdbcTemplate.update(sql.toString(), bnum, bid);//성공시 1 실패시 0(update 의 반환 타입은 int)

    return result;
  }

  /**
   * 조회수 증가
   * @param bnum
   * @return
   */
  @Override
  public int updateHit(Long bnum) {

    StringBuffer sql = new StringBuffer();
    sql.append(" update board ");
    sql.append(" set bhit = bhit + 1 ");
    sql.append(" where bnum = ? ");

    int result = jdbcTemplate.update(sql.toString(), bnum);//성공시 1 실패시 0(update 의 반환 타입은 int)

    return result;
  }

  /**
   * 전체건수
   * @return 게시글 전체건수
   */
  @Override
  public int totalCount() {
    String sql = "select count(*) from board";

    Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class);

    return cnt;
  }

  /**
   * 전체건수 - 검색
   * @param filterCondition 시작레코드번호, 종료레코드번호, 검색유형, 검색어
   * @return
   */
  @Override
  public int totalCount(BoardFilterCondition filterCondition) {
    //sql문 작성
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT count(*) ");
    sql.append("   FROM ");
    sql.append("     Board ");
    sql.append("   INNER JOIN ");
    sql.append("     Member ");
    sql.append("   ON ");
    sql.append("     Board.bid = Member.id ");

    //sql문 검색 조건 추가
    sql = dynamicQuery(filterCondition, sql);

    //sql문 실행
    Integer cnt = jdbcTemplate.queryForObject(
            sql.toString(), Integer.class
    );

    return cnt;
  }

  //sql문 검색 조건 추가
  private StringBuffer dynamicQuery(BoardFilterCondition filterCondition, StringBuffer sql) {

    sql.append(" WHERE ");

    //검색유형
    switch (filterCondition.getSearchType()){
      case "TC":  //제목 + 내용
        sql.append(" btitle like '%"+ filterCondition.getKeyword()+"%' ");
        sql.append(" OR DBMS_LOB.INSTR(bcontent,'"+ filterCondition.getKeyword()+"') > 0 ");
        break;
      case "T":   //제목
        sql.append(" btitle like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "C":   //내용
        sql.append(" DBMS_LOB.INSTR(board.bcontent,'"+ filterCondition.getKeyword()+"') > 0 ");
        break;
      case "N":   //닉네임
        sql.append(" nickname like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      default:
    }
    return sql;
  }
}
