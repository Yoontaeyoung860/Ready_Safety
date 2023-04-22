package safe.safe.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import safe.safe.dao.UploadFileDAO;
import safe.safe.dto.UploadFileDTO;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadFileSVCImpl implements UploadFileSVC{

  private final UploadFileDAO uploadFileDAO;

  //첨부파일 저장될 파일시스템의 경로 application.properties에 정의
  @Value("${attach.root_dir}")
  private String ROOT_DIR;  //첨부파일 루트경로

  /**
   * 업로드 파일 처리 - 단건
   * @param code 분류코드
   * @param rnum 원글번호
   * @param file 첨부파일
   * @return 성공여부
   */
  @Override
  public boolean addFile(String code, Long rnum, MultipartFile file) {
    try {
      UploadFileDTO uploadFileDTO = new UploadFileDTO();
      uploadFileDTO.setRnum(rnum);
      uploadFileDTO.setCode(code);

      String originalFileName = file.getOriginalFilename();//업로드 파일명
      log.info("originalFileName={}", originalFileName);
      String storeFileName = createStoreFilename(originalFileName);//임의 파일명
      log.info("storeFileName={}", storeFileName);
      uploadFileDTO.setUpload_filename(originalFileName);
      uploadFileDTO.setStore_filename(storeFileName);
      uploadFileDTO.setFsize(String.valueOf(file.getSize()));//용량
      log.info("fsize={}", String.valueOf(file.getSize()));
      uploadFileDTO.setFtype(file.getContentType());
      log.info("ftype={}", file.getContentType());

      //파일시스템에 물리적 파일 저장
      storeFile(uploadFileDTO, file);
      //uploadfile 테이블에 첨부파일 메타정보 저장
      uploadFileDAO.addFile(uploadFileDTO);

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * 업로드 파일 처리 - 여러건
   * @param code 분류코드
   * @param rnum 원글번호
   * @param files 첨부파일
   * @return 성공여부
   */
  @Override
  public boolean addFile(String code, Long rnum, List<MultipartFile> files) {
    try {
      List<UploadFileDTO> uploadFiles = new ArrayList<>();

      for (MultipartFile file : files) {
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setRnum(rnum);
        uploadFileDTO.setCode(code);

        String originalFileName = file.getOriginalFilename();//업로드 파일명
        log.info("originalFileName={}", originalFileName);
        String storeFileName = createStoreFilename(originalFileName);//임의 파일명
        log.info("storeFileName={}", storeFileName);
        uploadFileDTO.setUpload_filename(originalFileName);
        uploadFileDTO.setStore_filename(storeFileName);
        uploadFileDTO.setFsize(String.valueOf(file.getSize()));//용량
        log.info("fsize={}", String.valueOf(file.getSize()));
        uploadFileDTO.setFtype(file.getContentType());
        log.info("ftype={}", file.getContentType());

        uploadFiles.add(uploadFileDTO);
      }

      //파일시스템에 물리적 파일 저장
      storeFiles(uploadFiles, files);
      //uploadfile 테이블에 첨부파일 메타정보 저장
      uploadFileDAO.addFile(uploadFiles);

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * 업로드 파일 경로
   * @param code
   * @return 업로드 파일 경로
   */
  @Override
  public String getFullPath(String code) {
    StringBuffer path = new StringBuffer();
    //path = c:/javaedu8/attach/{code}/ 경로에 물리적 파일 저장
    path = path.append(ROOT_DIR).append(code).append("/");
    //경로가 없으면 생성(단 attach 폴더는 존재해야함)
    createFolder(path.toString());
    log.info("파일저장위치={}", path.toString());
    return path.toString();
  }

  /**
   * 업로드 파일 조회 by fnum - 단건
   * @param fnum
   * @return uploadFileDTO
   */
  @Override
  public UploadFileDTO findFileByFnum(Long fnum) {
    return uploadFileDAO.findFileByFnum(fnum);
  }

  /**
   * 업로드 파일 조회 by code, rnum - 여러건
   * @param code
   * @param rnum
   * @return list
   */
  @Override
  public List<UploadFileDTO> findFilesByCodeWithRnum(String code, Long rnum) {
    return uploadFileDAO.findFilesByCodeWithRnum(code, rnum);
  }

  /**
   * 업로드 파일 삭제 by fnum - 단건
   * @param fnum
   * @return 삭제된 파일 개수
   */
  @Override
  public int deleteFileByFnum(Long fnum) {
    log.info(String.valueOf(fnum));
    //물리적 파일 삭제
    UploadFileDTO uploadFileDTO = uploadFileDAO.findFileByFnum(fnum);
    deleteFile(uploadFileDTO.getCode(), uploadFileDTO.getStore_filename());

    //메타정보 삭제
    int affectedRow = uploadFileDAO.deleteFileByFnum(fnum);//변경된 행의 수

    return affectedRow;//삭제된 파일 개수 반환
  }

  /**
   * 업로드 파일 삭제 by code, rnum - 해당글의 모든 파일
   * @param code
   * @param rnum
   * @return 삭제된 파일 개수
   */
  @Override
  public int deleteFileByCodeWithRnum(String code, Long rnum) {

    //물리적 파일 삭제
    List<UploadFileDTO> uploadFiles = uploadFileDAO.findFilesByCodeWithRnum(code, rnum);
    for (UploadFileDTO uploadFileDTO : uploadFiles) {
      deleteFile(uploadFileDTO.getCode(), uploadFileDTO.getStore_filename());
    }

    //메타정보 삭제
    uploadFileDAO.deleteFileByCodeWithRnum(code, rnum);

    return uploadFiles.size();//삭제된 파일 개수 반환
  }

  //임의파일명 생성
  private String createStoreFilename(String originalFile) {
    StringBuffer storeFileName = new StringBuffer();
    storeFileName.append(UUID.randomUUID().toString())//고유 식별자 생성 ex)'고유식별자'
            .append(".")// ex)'고유식별자.'
            .append(extractExt(originalFile));//확장자 추출 ex)'고유식별자.확장자'
    return storeFileName.toString();//고유 식별자로 된 임의파일명 반환
  }

  //확장자 추출
  private String extractExt(String originalFile) {
    int posOfExt =originalFile.lastIndexOf(".");//업로드 파일의 "."와 최초로 마주친 위치 인덱스 반환
    String ext = originalFile.substring(posOfExt + 1);//"."인덱스 다음 인덱스(posOfExt + 1)의 문자열부터 확장자 추출
    return ext;//확장자 문자열 반환
  }



  //파일시스템에 물리적 파일 저장(예외처리 필요)
  private void storeFile(UploadFileDTO uploadFileDTO, MultipartFile file) {
    try {
      //MultipartFile.transferTo = getFullPath 경로에 getStore_filename 파일명으로 물리적 파일 저장
      file.transferTo(Path.of(getFullPath(uploadFileDTO.getCode()), uploadFileDTO.getStore_filename()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  //파일시스템에 물리적 파일 저장(여러 파일 저장시)
  private void storeFiles(List<UploadFileDTO> uploadFiles, List<MultipartFile> files) {
    for (int i=0; i<uploadFiles.size(); i++) {
      storeFile(uploadFiles.get(i), files.get(i));
    }
  }

  //폴더 생성
  private void createFolder(String path) {
    File folder = new File(path);//path 경로를 가지는 파일 객체 생성
    if(!folder.exists()){
      folder.mkdir();//디렉토리를 생성하고 false 반환, 디렉토리가 이미 존재하면 생성하지 않고 false 반환
    }
  }



  //물리적 파일 삭제
  private boolean deleteFile(String code ,String store_filename) {

    boolean isDeleted = false;

    File file = new File(getFullPath(code) + store_filename);//파일 객체 생성

    if(file.exists()) {
      if(file.delete()) {//파일 삭제 성공시 true, 실패시 false 반환한다
        isDeleted = true;
        log.info("삭제성공");
      }else{
        log.info("삭제실패");
      }
    }

    return isDeleted;
  }
}
