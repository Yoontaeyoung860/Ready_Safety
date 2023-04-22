package safe.safe.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import safe.safe.dto.UploadFileDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UploadFileDAOImpl implements UploadFileDAO{

  private final JdbcTemplate jdbcTemplate;

  /**
   * 업로드 파일 등록 - 단건
   * @param uploadFileDTO
   * @return fnum
   */
  @Override
  public Long addFile(UploadFileDTO uploadFileDTO) {
    StringBuffer sql = new StringBuffer();
    sql.append(" INSERT INTO uploadfile ( ");
    sql.append("         fnum, ");
    sql.append("         rnum, ");
    sql.append("         code, ");
    sql.append("         store_filename, ");
    sql.append("         upload_filename, ");
    sql.append("         fsize, ");
    sql.append("         ftype ");
    sql.append(" ) VALUES ( ");
    sql.append("         uploadfile_fnum_seq.nextval, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ? ");
    sql.append(" ) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(),new String[]{"fnum"});
        pstmt.setLong(1, uploadFileDTO.getRnum());
        pstmt.setString(2, uploadFileDTO.getCode());
        pstmt.setString(3, uploadFileDTO.getStore_filename());
        pstmt.setString(4, uploadFileDTO.getUpload_filename());
        pstmt.setString(5, uploadFileDTO.getFsize());
        pstmt.setString(6, uploadFileDTO.getFtype());
        return pstmt;
      }
    },keyHolder);

    return Long.valueOf(keyHolder.getKeys().get("fnum").toString());
  }

  /**
   * 업로드 파일 등록 - 여러건
   * @param list
   */
  @Override
  public void addFile(List<UploadFileDTO> uploadFiles) {
    StringBuffer sql = new StringBuffer();
    sql.append(" INSERT INTO uploadfile ( ");
    sql.append("         fnum, ");
    sql.append("         rnum, ");
    sql.append("         code, ");
    sql.append("         store_filename, ");
    sql.append("         upload_filename, ");
    sql.append("         fsize, ");
    sql.append("         ftype ");
    sql.append(" ) VALUES ( ");
    sql.append("         uploadfile_fnum_seq.nextval, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ?, ");
    sql.append("         ? ");
    sql.append(" ) ");

    //배치 처리
    jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, uploadFiles.get(i).getRnum());
        ps.setString(2, uploadFiles.get(i).getCode());
        ps.setString(3, uploadFiles.get(i).getStore_filename());
        ps.setString(4, uploadFiles.get(i).getUpload_filename());
        ps.setString(5, uploadFiles.get(i).getFsize());
        ps.setString(6, uploadFiles.get(i).getFtype());
      }

      //배치처리할 건수
      @Override
      public int getBatchSize() {
        return uploadFiles.size();
      }
    });
  }

  /**
   * 업로드 파일 조회 by fnum - 단건
   * @param fnum
   * @return uploadFileDTO
   */
  @Override
  public UploadFileDTO findFileByFnum(Long fnum) {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT * ");
    sql.append("   FROM uploadfile ");
    sql.append("  WHERE fnum = ? ");

    UploadFileDTO uploadFileDTO = null;
    try {
      uploadFileDTO = jdbcTemplate.queryForObject(
              sql.toString(),
              new BeanPropertyRowMapper<>(UploadFileDTO.class),
              fnum);
    }catch (EmptyResultDataAccessException e){
      e.printStackTrace();
      uploadFileDTO = null;
    }
    return uploadFileDTO;
  }

  /**
   * 업로드 파일 조회 by code, rnum - 여러건
   * @param code
   * @param rnum
   * @return list
   */
  @Override
  public List<UploadFileDTO> findFilesByCodeWithRnum(String code, Long rnum) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT  ");
    sql.append("   fnum, ");
    sql.append("   rnum, ");
    sql.append("   code,  ");
    sql.append("   store_filename, ");
    sql.append("   upload_filename,  ");
    sql.append("   fsize,  ");
    sql.append("   ftype,  ");
    sql.append("   fcdate,  ");
    sql.append("   fudate ");
    sql.append("  FROM uploadfile  ");
    sql.append(" WHERE code = ?  ");
    sql.append("   AND rnum = ?  ");

    List<UploadFileDTO> list = jdbcTemplate.query(sql.toString(),
            new BeanPropertyRowMapper<>(UploadFileDTO.class), code, rnum);
//    log.info("list={}",list);
    return list;
  }

  /**
   * 업로드 파일 삭제 by fnum - 단건
   * @param fnum
   * @return 1,0(변경된 행의 수)
   */
  @Override
  public int deleteFileByFnum(Long fnum) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM uploadfile ");
    sql.append(" WHERE fnum = ? ");

    return jdbcTemplate.update(sql.toString(), fnum);
  }

  /**
   * 업로드 파일 삭제 by code, rnum - 해당글의 모든 파일
   * @param code
   * @param rnum
   * @return 1,0(변경된 행의 수)
   */
  @Override
  public int deleteFileByCodeWithRnum(String code, Long rnum) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM uploadfile ");
    sql.append(" WHERE code = ? ");
    sql.append("   AND rnum = ? ");

    return jdbcTemplate.update(sql.toString(), code, rnum);
  }
}
