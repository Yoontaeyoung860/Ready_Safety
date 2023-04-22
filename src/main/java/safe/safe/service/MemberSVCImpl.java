package safe.safe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import safe.safe.dao.MemberDAO;
import safe.safe.dto.ChangPwDTO;
import safe.safe.dto.MemberDTO;
import safe.safe.web.form.member.find.ChangPwReq;
import safe.safe.web.form.member.find.FindIdReq;
import safe.safe.web.form.member.find.FindPwReq;
import safe.safe.web.form.member.find.FindTest;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

    private final MemberDAO memberDAO;

    // 회원가입
    @Override
    public void insertMember(MemberDTO memberDTO) {
        String id = memberDAO.insertMember(memberDTO);
    }

    // id 중복 비교
    @Override
    public boolean isExistId(String id) {
        return memberDAO.isExistId(id);
    }

    @Override
    public boolean isDeleteId(String id) {
        return memberDAO.isDeleteId(id);
    }

    // email 중복 비교
    @Override
    public boolean isExistEmail(String email) {
        return memberDAO.isExistEmail(email);
    }

    // nickname 중복 비교
    @Override
    public boolean isExistNickname(String nickname) {
        return memberDAO.isExistNickname(nickname);
    }

    // 로그인 인증
    @Override
    public MemberDTO login(String id, String pw) {
        return memberDAO.login(id, pw);
    }

    @Override
    public MemberDTO isLogin(String id, String pw) {
        MemberDTO mdto = null;
        if(memberDAO.isLogin(id, pw)) {
            mdto = memberDAO.findByID(id);
        }
        return mdto;
    }

    //비밀번호 일치여부 체크
    @Override
    public boolean isMember(String id, String pw) {
        return memberDAO.isMember(id, pw);
    }

    //관리자 코드 확인
    @Override
    public String admin(String id) {
        return memberDAO.admin(id);
    }

    // 관리자 전체 회원 확인
    @Override
    public List<MemberDTO> allMemberList() {
        return memberDAO.allMemberList();
    }

    // 관리자 유지 회원 확인
    @Override
    public List<MemberDTO> isMemberList() {
        return memberDAO.isMemberList();
    }

    // 관리자 탈퇴 회원 확인
    @Override
    public List<MemberDTO> dleMemberList() {
        return memberDAO.dleMemberList();
    }

    // 사용자 정보 수정용 id 조회
    @Override
    public MemberDTO findByID(String id) {
        return memberDAO.findByID(id);
    }

    // 사용자 email 조회
    @Override
    public MemberDTO findByEmail(String email) {
        return memberDAO.findByEmail(email);
    }

    // 사용자 닉네임 조회
    @Override
    public MemberDTO findByNickname(String nickname) {
        return memberDAO.findByNickname(nickname);
    }

    // 회원 일반 정보 수정
    @Override
    public void modifyMember(String id, MemberDTO memberDTO) {
        memberDAO.modifyMember(id, memberDTO);
    }

    // 회원 PW 정보 수정
    @Override
    public void modifyMemberPw(String id, MemberDTO memberDTO) {
        memberDAO.modifyMemberPw(id, memberDTO);
    }

    // id 찾기
    @Override
    public String findIdMember(String email, String name) {
        return memberDAO.findIdMember(email, name);
    }

    // pw 찾기
    @Override
    public String findPwMember(String id, String name, String email) {
        return memberDAO.findPwMember(id, name, email);
    }





    @Override
    public List<String> findMemberId(FindIdReq findIdReq) {
        return memberDAO.findMemberId(findIdReq);
    }

    @Override
    public ChangPwReq findMemberPw(FindPwReq FindPwReq) {
        return memberDAO.findMemberPw(FindPwReq);
    }

    @Override
    public MemberDTO changeMemberPW(String id, ChangPwDTO changPwDTO) {
        return memberDAO.changeMemberPW(id, changPwDTO);
    }

    @Override
    public List<String> changeMemberPW(FindPwReq findPwReq) {
        return memberDAO.changeMemberPW(findPwReq);
    }

    @Override
    public void changeMemberPW(String email, String pw, String tmpPw) {
        memberDAO.changeMemberPW(email, pw, tmpPw);
    }

    @Override
    public ChangPwReq findMemberTestPw(FindTest findTest) {
        return memberDAO.findMemberTestPw(findTest);
    }

    @Override
    public List<String> changeMemberTestPW(FindTest findTest) {
        return memberDAO.changeMemberTestPW(findTest);
    }


    // 회원탈퇴
    @Override
    public void deleteMember(String id) {
        memberDAO.deleteMember(id);
    }
}
