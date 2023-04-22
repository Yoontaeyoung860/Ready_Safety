package safe.safe.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import safe.safe.dto.board.EmergencyPostDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class EmergencyPostDAOImpl implements EmergencyPostDAO {


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<EmergencyPostDto> rowMapper = (rs, rowNum) -> {
        EmergencyPostDto dto = new EmergencyPostDto();
        dto.setEid(rs.getLong("eid"));
        dto.setEtitle(rs.getString("etitle"));
        dto.setEcategory(rs.getString("ecategory"));
        dto.setEcontent(rs.getString("econtent"));
        dto.setEcreatedAt(rs.getTimestamp("ecreatedAt").toLocalDateTime());
        dto.setEupdatedAt(rs.getTimestamp("eupdatedAt").toLocalDateTime());
        return dto;
    };

    public EmergencyPostDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<EmergencyPostDto> findByCategory(String ecategoryCode) {
        String sql = "SELECT * FROM emergency WHERE ecategory = ?";
        return jdbcTemplate.query(sql, rowMapper, ecategoryCode);
    }

    //등록
    @Override
    public Long create(EmergencyPostDto emergency) {
        //SQL작성
        StringBuffer sql = new StringBuffer();
        sql.append("insert into notice (eid,ecategory,etitle,econtent) ");
        sql.append("values(Emergency_eid_seq.nextval,?, ?, ? ) ");

        //SQL실행
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(
                        sql.toString(),
                        new String[]{"eid"}  // insert 후 insert 레코드중 반환할 컬럼명, KeyHolder에 저장됨.
                );

                pstmt.setString(1, emergency.getEtitle());
                pstmt.setString(2, emergency.getEcontent());

                return pstmt;
            }
        },keyHolder);

        return Long.valueOf(keyHolder.getKeys().get("eid").toString());
    }

    private RowMapper<EmergencyPostDto> emergencyPostDtoRowMapper = new RowMapper<EmergencyPostDto>() {
        @Override
        public EmergencyPostDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmergencyPostDto emergencyPostDto = new EmergencyPostDto();
            emergencyPostDto.setEid(rs.getLong("eid"));
            emergencyPostDto.setEcategory(rs.getString("ecategory"));
            emergencyPostDto.setEtitle(rs.getString("etitle"));
            emergencyPostDto.setEcontent(rs.getString("econtent"));
            emergencyPostDto.setEcreatedAt(rs.getTimestamp("created_dt").toLocalDateTime());
            return emergencyPostDto;
        }
    };


    //전체조회
    @Override
    public List<EmergencyPostDto> findAll() {
        StringBuffer sql = new StringBuffer();
        sql.append("select eid, etitle, econtent,ecdate, eudate ");
        sql.append("  from emergency ");
        sql.append("order by eid desc ");

        List<EmergencyPostDto> list = jdbcTemplate.query(
                sql.toString(), new BeanPropertyRowMapper<>(EmergencyPostDto.class));

        return list;
    }

    @Override
    public List<EmergencyPostDto> findAll(int startRec, int endRec) {
        StringBuffer sql = new StringBuffer();

        sql.append("select t1.* ");
        sql.append("from( ");
        sql.append("select ");
        sql.append("ROW_NUMBER() OVER(ORDER BY eid desc)no, ");
        sql.append("eid,etitle,econtent,ecdate,eudate ");
        sql.append("from emergency) t1 ");
        sql.append("where t1.no between ? and ? ");

        List<EmergencyPostDto> list = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(EmergencyPostDto.class),
                startRec, endRec
        );
        return list;
    }

    //검색
    @Override
    public List<EmergencyPostDto> findAll(EmergencyFilterCondition EmergencyfilterCondition) {
        StringBuffer sql = new StringBuffer();

        sql.append("select t1.* ");
        sql.append("from( ");
        sql.append("  SELECT ");
        sql.append("  ROW_NUMBER() OVER (ORDER BY eid DESC) no, ");
        sql.append("      eid, ");
        sql.append("      etitle, ");
        sql.append("      econtent, ");
        sql.append("      ecdate, ");
        sql.append("      eudate ");
        sql.append("  FROM emergency");
        sql.append("  WHERE ");

        //분류
        sql = dynamicQuery(EmergencyfilterCondition, sql);

        sql.append(") t1 ");
        sql.append("where t1.no between ? and ? ");

        List<EmergencyPostDto> list = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(EmergencyPostDto.class),
                EmergencyfilterCondition.getStartRec(),
                EmergencyfilterCondition.getEndRec()
        );

        return list;
    }




    //상세 조회
    @Override
    public EmergencyPostDto selectOne(Long eid) {
        StringBuffer sql = new StringBuffer();
        sql.append("select nnum, ntitle,ncontent, nhit, ncdate, nudate ");
        sql.append("from notice ");
        sql.append("where nnum = ? ");

        List<EmergencyPostDto> query = jdbcTemplate.query(
                sql.toString(), new BeanPropertyRowMapper<>(EmergencyPostDto.class), eid);

        return (query.size() == 1) ? query.get(0) : null;
    }
    //수정
    @Override
    public EmergencyPostDto update(EmergencyPostDto emergency) {
        StringBuffer sql = new StringBuffer();
        sql.append("update notice ");
        sql.append("set etitle = ? , ");
        sql.append("    econtent = ? , ");
        sql.append("    eudate   = systimestamp ");
        sql.append("where eid = ? ");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(
                        sql.toString(),
                        new String[]{"eid"}  // update 후 update 레코드중 반환할 컬럼명, KeyHolder에 저장됨.
                );

                pstmt.setString(1, emergency.getEtitle());
                pstmt.setString(2, emergency.getEcontent());
                pstmt.setLong(3, emergency.getEid());

                return pstmt;
            }
        },keyHolder);

        long eid = Long.valueOf(keyHolder.getKeys().get("eid").toString());
        return selectOne(eid);
    }
    //삭제
    @Override
    public int delete(Long eid) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from emergency ");
        sql.append(" where eid = ? ");

        int cnt = jdbcTemplate.update(sql.toString(), eid);

        return cnt;
    }


    //전체건수
    @Override
    public int totalCount() {

        String sql = "select count(*) from emergency";

        Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class);

        return cnt;
    }

    @Override
    public int totalCount(EmergencyFilterCondition filterCondition) {

        StringBuffer sql = new StringBuffer();

        sql.append("select count(*) ");
        sql.append("  from emergency  ");
        sql.append(" where  ");

        sql = dynamicQuery(filterCondition, sql);

        Integer cnt = 0;

        cnt = jdbcTemplate.queryForObject(
                sql.toString(), Integer.class
        );

        return cnt;
    }


    private StringBuffer dynamicQuery(EmergencyFilterCondition filterCondition, StringBuffer sql) {


    //검색유형
    switch (filterCondition.getSearchType()){
        case "TC":  //제목 + 내용
            sql.append("    (  etitle    like '%"+ filterCondition.getKeyword()+"%' ");
            sql.append("    or econtent like '%"+ filterCondition.getKeyword()+"%' )");
            break;
        case "T":   //제목
            sql.append("       etitle    like '%"+ filterCondition.getKeyword()+"%' ");
            break;
        case "C":   //내용
            sql.append("       econtent like '%"+ filterCondition.getKeyword()+"%' ");
            break;
        default:
    }
    return sql;
}
}
