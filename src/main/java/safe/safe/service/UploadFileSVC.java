package safe.safe.service;

import org.springframework.web.multipart.MultipartFile;
import safe.safe.dto.UploadFileDTO;

import java.util.List;

public interface UploadFileSVC {


  /**
   * 업로드 파일 처리 - 단건
   * @param code 분류코드
   * @param rnum 원글번호
   * @param file 첨부파일
   * @return 성공여부
   */
  boolean addFile(String code, Long rnum, MultipartFile file);

  /**
   * 업로드 파일 처리 - 여러건
   * @param code 분류코드
   * @param rnum 원글번호
   * @param files 첨부파일
   * @return 성공여부
   */
  boolean addFile(String code, Long rnum, List<MultipartFile> files);

  /**
   * 업로드 파일 경로
   * @param code
   * @return 업로드 파일 경로
   */
  String getFullPath(String code);

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
   * @return 삭제된 파일 개수
   */
  int deleteFileByFnum(Long fnum);

  /**
   * 업로드 파일 삭제 by code, rnum - 해당글의 모든 파일
   * @param code
   * @param rnum
   * @return 삭제된 파일 개수
   */
  int deleteFileByCodeWithRnum(String code, Long rnum);
}
