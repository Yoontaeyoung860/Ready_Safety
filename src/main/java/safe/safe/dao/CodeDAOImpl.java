package safe.safe.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import safe.safe.dto.Code;
import safe.safe.dto.CodeAll;

import java.util.List;
@Slf4j
@Repository
@RequiredArgsConstructor
public class CodeDAOImpl implements CodeDAO{
   private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Code> code(String pcode) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t1.code_id code, t1.decode decode  ");
        sql.append("from code t1, code t2 ");
        sql.append("where t1.pcode_id = t2.code_id  ");
        sql.append("and t1.useyn = 'Y'  ");
        sql.append("and t1.pcode_id = ? ");

        List<Code> codes = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(Code.class),
                pcode
        );
        return codes;
    }

    //모든 코드 반환
    @Override
    public List<CodeAll> codeAll() {
        StringBuffer sql = new StringBuffer();
        sql.append("select t1.pcode_id pcode, t2.decode pdecode, t1.code_id ccode, t1.decode cdecode  ");
        sql.append("  from code t1, code t2 ");
        sql.append(" where t1.pcode_id = t2.code_id  ");
        sql.append("   and t1.useyn = 'Y' ");

        List<CodeAll> codeAll = jdbcTemplate.query(
                sql.toString(),
                new BeanPropertyRowMapper<>(CodeAll.class)
        );
        return codeAll;
    }
}
