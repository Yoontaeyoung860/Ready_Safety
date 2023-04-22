package safe.safe.service;

import safe.safe.dto.ChangPwDTO;
import safe.safe.dto.MemberDTO;
import safe.safe.web.form.member.find.ChangPwReq;
import safe.safe.web.form.member.find.FindIdReq;
import safe.safe.web.form.member.find.FindPwReq;
import safe.safe.web.form.member.find.FindTest;

import java.util.List;

public interface MemberSVC {

    // 회원가입
    void insertMember(MemberDTO memberDTO);

    // id 중복 비교
    boolean isExistId(String id);

    // 탈퇴 id 중복 비교
    boolean isDeleteId(String id);

    // email 중복 비교
    boolean isExistEmail(String email);

    // nickname 중복 비교
    boolean isExistNickname(String nickname);

    // 로그인 인증
    MemberDTO login(String id, String pw);

    // 로그인 인증 확인
    MemberDTO isLogin(String id, String pw);

    //비밀번호 일치여부 체크
    boolean isMember(String id, String pw);

    //관리자 코드 확인
    String admin(String id);

    // 관리자 전체 회원 확인
    List<MemberDTO> allMemberList();

    // 관리자 유지 회원 확인
    List<MemberDTO> isMemberList();

    // 관리자 탈퇴 회원 확인
    List<MemberDTO> dleMemberList();

    // 사용자  id 조회 또는 수정용 id 조회
    MemberDTO findByID(String id);

    // 사용자 email 조회
    MemberDTO findByEmail(String email);

    // 사용자 닉네임 조회
    MemberDTO findByNickname(String nickname);

    // 회원 일반 정보 수정
    void modifyMember(String id, MemberDTO memberDTO);

    // 회원 PW 정보 수정
    void modifyMemberPw(String id, MemberDTO memberDTO);

    // id 찾기
    String findIdMember(String email, String name);

    // pw 찾기
    String findPwMember(String id, String name, String email);



    // id 찾기
    List<String> findMemberId(FindIdReq findIdReq);

    //pw 변경 처리를 위한 찾기
    ChangPwReq findMemberPw(FindPwReq findPwReq);

    //임시 pw 변경 처리
    MemberDTO changeMemberPW(String id, ChangPwDTO changPwDTO);

    //변경된 pw 찾기
    List<String> changeMemberPW(FindPwReq findPwReq);


    //비밀번호찾기시 실행되는 비밀번호변경 화면용
    void changeMemberPW(String email, String pw, String tmpPw);


    //pw 변경 처리를 위한 찾기
    ChangPwReq findMemberTestPw(FindTest findTest);

    //변경된 pw 찾기
    List<String> changeMemberTestPW(FindTest findTest);





    // 회원탈퇴
    void deleteMember(String id);



}