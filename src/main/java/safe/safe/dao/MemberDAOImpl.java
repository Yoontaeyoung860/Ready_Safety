package safe.safe.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import safe.safe.dto.MemberDTO;
import safe.safe.dto.ChangPwDTO;
import safe.safe.web.form.member.find.FindIdReq;
import safe.safe.web.form.member.find.FindPwReq;
import safe.safe.web.form.member.find.FindTest;
import safe.safe.web.form.member.find.ChangPwReq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // 회원가입
    @Override
    public String insertMember(MemberDTO memberDTO) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into member(id, pw, name, nickname, email) ");
        sql.append("values(?, ?, ?, ?, ?)");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {PreparedStatement pstmt = con.prepareStatement(
                    sql.toString(), new String[] {"id"}
            );
                pstmt.setString(1,memberDTO.getId());
                pstmt.setString(2,passwordEncoder.encode(memberDTO.getPw()));
                pstmt.setString(3, memberDTO.getName());
                pstmt.setString(4, memberDTO.getNickname());
                pstmt.setString(5, memberDTO.getEmail());
                return pstmt;
            }
        },keyHolder);
        return keyHolder.getKeyAs(String.class);
    }

    // id 중복 비교
    @Override
    public boolean isExistId(String id) {
        String sql = "select count(id) from member where id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return (count==1) ? true : false;
    }

    // 탈퇴 id 중복 비교
    @Override
    public boolean isDeleteId(String id) {
        String sql = "select count(id) from member where id = ? and leave_fl = 1 ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return (count==1) ? true : false;
    }

    // email 중복 비교
    @Override
    public boolean isExistEmail(String email) {
        String sql = "select count(email) from member where email = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return (count==1) ? true : false;


    }

    // nickname 중복 비교
    @Override
    public boolean isExistNickname(String nickname) {
//    StringBuffer sql = new StringBuffer();
//    sql.append("select count(nickname) from member ");
//    sql.append(" where nickname = ? leave_fl = 0  ");
//    Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, nickname);
//    return (count == 1) ? true : false;

        String sql = "select count(nickname) from member where nickname = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname);
        return (count==1) ? true : false;


    }

    // 로그인 인증
    @Override
    public MemberDTO login(String id, String pw) {
        String sql = "select id, pw, name, email, nickname, admin_fl, signup_dt from member where id = ? and leave_fl = 0";
        List<MemberDTO> list = jdbcTemplate.query(sql,
            new BeanPropertyRowMapper<>(MemberDTO.class), id);

        if (list.size() == 1 && passwordEncoder.matches(pw, list.get(0).getPw())) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean isLogin(String id, String pw) {
        boolean isLogin = false;

        StringBuffer sql = new StringBuffer();
        sql.append("select count(id) ");
        sql.append("  from member ");
        sql.append(" where id = ? ");
        sql.append("   and pw = ? ");

        if(passwordEncoder.matches(pw, matchesId(id))) {
//      return false;
            return true;
        }

        Integer count =jdbcTemplate.queryForObject(sql.toString(), Integer.class, id, pw);

        if(count == 1) isLogin = true;


        return isLogin;
    }

    //비밀번호 일치여부 체크
    @Override
    public boolean isMember(String id, String pw) {
        log.info("id={}",id);
        log.info("pw={}",pw);

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from member ");
        sql.append(" where id = ? and pw =? and leave_fl = 0 ");

        if(passwordEncoder.matches(pw, matchesId(id))) {
//      return false;
            return true;
        }


        Integer count = jdbcTemplate.queryForObject(
                sql.toString(), Integer.class, id, pw
        );
        log.info(String.valueOf(count));
        return (count == 1) ? true : false;
    }

    @Override
    public String matchesId(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pw from member ");
        sql.append(" where id = ? ");
        String pw =
                jdbcTemplate.queryForObject(sql.toString(), String.class, id);
        return pw;
    }

    //관리자 코드 확인
    @Override
    public String admin(String id) {

        StringBuffer sql = new StringBuffer();
        sql.append("select admin_fl from member ");
        sql.append(" where id = ? ");
        String admin_fl =
                jdbcTemplate.queryForObject(sql.toString(), String.class, id);
        return admin_fl;
    }

    // 관리자 전체 회원 확인
    @Override
    public List<MemberDTO> allMemberList() {
        StringBuffer sql = new StringBuffer();
        sql.append("select id, name, email, nickname, signup_dt, leave_dt  ");
        sql.append("from member where admin_fl = 3 order by signup_dt desc ");
        List<MemberDTO> list = jdbcTemplate.query(sql.toString(),
                new BeanPropertyRowMapper<>(MemberDTO.class)
        );
        return list;
    }

    // 관리자 유지 회원 확인
    @Override
    public List<MemberDTO> isMemberList() {
        StringBuffer sql = new StringBuffer();
        sql.append("select id, name, email, nickname, signup_dt, leave_fl  ");
        sql.append("from member where admin_fl = 3 and leave_fl=0 order by signup_dt desc ");
        List<MemberDTO> list = jdbcTemplate.query(sql.toString(),
                new BeanPropertyRowMapper<>(MemberDTO.class)
        );
        return list;
    }

    // 관리자 탈퇴 회원 확인
    @Override
    public List<MemberDTO> dleMemberList() {
        StringBuffer sql = new StringBuffer();
        sql.append("select id, name, email, nickname, leave_dt, leave_fl  ");
        sql.append("from member where leave_fl=1 order by signup_dt desc ");
        List<MemberDTO> list = jdbcTemplate.query(sql.toString(),
                new BeanPropertyRowMapper<>(MemberDTO.class)
        );
        return list;
    }

    // 사용자 id 조회 또는 수정용 id 조회
    @Override
    public MemberDTO findByID(String id) {

        StringBuffer sql = new StringBuffer();
//    sql.append("select id, email, pw, name, nickname from member ");
        sql.append("select id, email, pw, name, nickname, admin_fl, signup_dt, leave_fl, leave_dt from member ");
        sql.append(" where id = ? ");
        MemberDTO memberDTO = jdbcTemplate.queryForObject(sql.toString(),
                new BeanPropertyRowMapper<>(MemberDTO.class), id);
        return memberDTO;
    }

    // 사용자 email 조회
    @Override
    public MemberDTO findByEmail(String email) {
        StringBuffer sql = new StringBuffer();
        sql.append("select id, pw, name, nickname, from member ");
        sql.append(" where email = ? ");
        MemberDTO memberDTO = jdbcTemplate.queryForObject(sql.toString(),
                new BeanPropertyRowMapper<>(MemberDTO.class), email);
        return memberDTO;
    }

    // 사용자 닉네임 조회
    @Override
    public MemberDTO findByNickname(String nickname) {
        StringBuffer sql = new StringBuffer();
        sql.append("select id, pw, name, email, from member ");
        sql.append(" where nickname = ? ");
        MemberDTO memberDTO = jdbcTemplate.queryForObject(sql.toString(),
                new BeanPropertyRowMapper<>(MemberDTO.class), nickname);
        return memberDTO;
    }

    // 회원 일반 정보 수정
    @Override
    public void modifyMember(String id, MemberDTO memberDTO) {
        StringBuffer sql = new StringBuffer();
        sql.append("update member set nickname = ?, email = ? ");
        sql.append(" where id = ? and leave_fl = 0  ");
        jdbcTemplate.update( sql.toString(),
                memberDTO.getNickname(), memberDTO.getEmail(), id);
    }

    // 회원 PW 정보 수정
    @Override
    public void modifyMemberPw(String id, MemberDTO memberDTO) {
        StringBuffer sql = new StringBuffer();
        sql.append("update member set pw = ? ");
        sql.append(" where id = ? and leave_fl = 0  ");

        jdbcTemplate.update( sql.toString(),
                passwordEncoder.encode(memberDTO.getPw()), id);
    }

    // id 찾기
    @Override
    public String findIdMember(String email, String name) {
        StringBuffer sql = new StringBuffer();
        sql.append("select id from member ");
        sql.append(" where email = ? and name = ? ");
//    String id =
//            jdbcTemplate.queryForObject(sql.toString(), String.class, email, name);
//    return id;

        List<String> result = jdbcTemplate.query(
                sql.toString(),
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getNString("id");
                    }
                },
                email, name
        );
        return (result.size() == 1) ? result.get(0) : null;




    }

    // pw 찾기
    @Override
    public String findPwMember(String id, String name, String email) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pw from member ");
        sql.append(" where id = ? and name = ? and email = ? and leave_fl = 0 ");
        String pw =
                jdbcTemplate.queryForObject(sql.toString(), String.class, id, name, email);
        return pw;

//    List<String> result = jdbcTemplate.query(
//        sql.toString(),
//        new RowMapper<String>() {
//          @Override
//          public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//            return rs.getNString("pw");
//          }
//        },
//        id, name, email
//    );
//    return (result.size() == 1) ? result.get(0) : null;
//




    }

    // id 찾기 화면용
    @Override
    public List<String> findMemberId(FindIdReq findIdReq) {
        StringBuffer sql = new StringBuffer();
        sql.append("select id from member ");
        sql.append(" where name = ? and email = ? ");

        log.info(findIdReq.toString());

        List<String> id =  jdbcTemplate.queryForList(sql.toString(),
                String.class,
                findIdReq.getIdname(),findIdReq.getIdemail());

        log.info("form:{}",id.toString());
//		log.info("id={}",id.getId());
        return id;
    }

    //비밀번호 찾기
    @Override
    public ChangPwReq findMemberPw(FindPwReq findPwReq) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pw from member ");
        sql.append(" where id = ? and name = ? and email = ? ");

        ChangPwReq changPwReq = jdbcTemplate.queryForObject(sql.toString(),
                new BeanPropertyRowMapper<>(ChangPwReq.class),
                findPwReq.getPwid(), findPwReq.getPwname(), findPwReq.getPwemail());

        return changPwReq;
    }

    @Override
    public MemberDTO changeMemberPW(String id, ChangPwDTO changPwDTO) {
        StringBuffer sql = new StringBuffer();

        sql.append("update member ");
        sql.append("   set pw=? ");
        sql.append(" where id=? and pw = ? ");

        jdbcTemplate.update(sql.toString(),changPwDTO.getPwChk(),id, changPwDTO.getPw() );
        return findByID(id);
    }

    @Override
    public List<String> changeMemberPW(FindPwReq findPwReq) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pw from member ");
        sql.append(" where name = ? and email = ? ");

        log.info(findPwReq.toString());

        List<String> pw =  jdbcTemplate.queryForList(sql.toString(),
                String.class,
                findPwReq.getPwname(),findPwReq.getPwemail());

        log.info("form:{}",pw.toString());

        return pw;
    }

    @Override
    public void changeMemberPW(String email, String pw, String tmpPw) {

        StringBuffer sql = new StringBuffer();
        sql.append("update member ");
        sql.append("	 set pw = ? ");   	//변경할 비밀번호
        sql.append(" where email = ? ");
        sql.append("   and pw = ? ");   	//이전 비밀번호




        jdbcTemplate.update(sql.toString(), passwordEncoder.encode(tmpPw), email, pw);

    }

    @Override
    public ChangPwReq findMemberTestPw(FindTest findTest) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pw from member ");
        sql.append(" where id = ? and name = ? and email = ? ");

        ChangPwReq changPwReq = jdbcTemplate.queryForObject(sql.toString(),
                new BeanPropertyRowMapper<>(ChangPwReq.class),
                findTest.getPid(), findTest.getPname(), findTest.getPemail());

        return changPwReq;
    }

    @Override
    public List<String> changeMemberTestPW(FindTest findTest) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pw from member ");
        sql.append(" where name = ? and email = ? ");

        log.info(findTest.toString());

        List<String> pw =  jdbcTemplate.queryForList(sql.toString(),
                String.class,
                findTest.getPname(),findTest.getPemail());

        log.info("form:{}",pw.toString());

        return pw;
    }

    // 회원탈퇴
    @Override
    public void deleteMember(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("update member set leave_fl = 1, leave_dt = systimestamp ");
        sql.append(" where id = ? ");
        jdbcTemplate.update(sql.toString(), id);
    }
}
