package safe.safe.dao;

import safe.safe.dto.UploadFileDTO;

import java.util.List;

public interface UploadFileDAO {

  /**
   * 업로드 파일 등록 - 단건
   * @param uploadFileDTO
   * @return fnum
   */
  Long addFile(UploadFileDTO uploadFileDTO);

  /**
   * 업로드 파일 등록 - 여러건
   * @param uploadFiles
   */
  void addFile(List<UploadFileDTO> uploadFiles);

  /**
   * 업로드 파일 조회 by fnum - 단건
   * @param fnum
   * @return uploadFileDTO
   */
  UploadFileDTO findFileByFnum(Long fnum);

  /**
   * 업로드 파일 조회 by code, rnum - 여러건
   * @param code
   * @param rnum
   * @return list
   */
  List<UploadFileDTO> findFilesByCodeWithRnum(String code, Long rnum);

  /**
   * 업로드 파일 삭제 by fnum - 단건
   * @param fnum
   * @return 1,0(변경된 행의 수)
   */
  int deleteFileByFnum(Long fnum);

  /**
   * 업로드 파일 삭제 by code, rnum - 해당글의 모든 파일
   * @param code
   * @param rnum
   * @return 1,0(변경된 행의 수)
   */
  int deleteFileByCodeWithRnum(String code, Long rnum);
}
