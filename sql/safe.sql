--테이블 삭제
drop table member;
drop table emergency;
drop table code cascade constraints;        -- 코드
drop table board;
-----------------------------------------------
-- 시퀀스 삭제 (추가 중)...
DROP SEQUENCE member_id_seq;
DROP SEQUENCE emergency_eid_seq;
DROP SEQUENCE board_board_id_seq;

------------------------------------------------
--시퀀스 생성 (추가 중)...
--회원 시퀀스
create sequence member_id_seq
 start with 1
    increment by 1
    minvalue 0
    maxvalue 99999999
    nocycle;
--응급처치 게시판 시퀀스
create sequence emergency_eid_seq
    start with 1
    increment by 1
    minvalue 0
    maxvalue 99999999
    nocycle;
--게시판 시퀀스 생성
create sequence board_bnum_seq
start with 1 --시작값
increment by 1 --증감치
minvalue 0 --최소값
maxvalue 9999999999 --최대값
nocache --시퀀스 순차 증가 오류에 대응을 하지만 메모리에 미리 할당하지 않음에 대량으로 필요할 시 병목 현상 발생
nocycle; --순환하지않음

--게시판_댓글 시퀀스 생성
create sequence comments_cnum_seq
start with 1 --시작값
increment by 1 --증감치
minvalue 0 --최소값
maxvalue 9999999999 --최대값
nocache --시퀀스 순차 증가 오류에 대응을 하지만 메모리에 미리 할당하지 않음에 대량으로 필요할 시 병목 현상 발생
nocycle; --순환하지않음

----------------------------------------------
------------시퀀스 증가 오류 처리 장소------------
alter sequence member_id_seq nocache;
alter sequence emergency_eid_seq nocache;
alter sequence board_board_id_seq nocache;
----------------------------------------------
--회원 테이블
-- 회원 테이블
CREATE TABLE member (
  id VARCHAR2(40) NOT NULL,
  pw VARCHAR2(70) NOT NULL,
  name VARCHAR2(20) NOT NULL,
  email VARCHAR2(50) NOT NULL,
  nickname VARCHAR2(30) NOT NULL,
  admin_fl NUMBER(1) DEFAULT 3 NOT NULL,
  signup_dt TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  leave_fl NUMBER(1) DEFAULT 0 NOT NULL,
  leave_dt TIMESTAMP
);

-- 기본키 생성
ALTER TABLE member ADD CONSTRAINT member_id_pk PRIMARY KEY (id);

-- 닉네임 외래키 생성 (code 테이블이 필요합니다)
ALTER TABLE member ADD CONSTRAINT member_nickname_fk FOREIGN KEY (nickname) REFERENCES code(code_id);

----------------------------------------------------
create table emergency
(
    eid        varchar2(40),
    ecategory  varchar2(10) not null,
    econtent   varchar2(15) not null,
    ecreatedAt varchar2(30) not null,
    eupdatedAt varchar2(16) not null,
    ecdate      timestamp DEFAULT systimestamp,
    eudate      timestamp DEFAULT systimestamp
);
alter table emergency add constraint emergency_eid_pk primary key(eid);
constraint board_bcategory_ck check (bcategory in('EA','EB','EC','ED'.'EE','EF'))
-----------------------------------------------------

----------------------------------------------------
-- 게시판

--7. 게시판
--게시판 테이블 생성
create table board(
  bnum NUMBER(15) not null,                   -- 게시글번호
  bcategory varchar2(11),                     -- 분류카테고리
  btitle VARCHAR2(60) not null,               -- 게시글제목
  bcdate TIMESTAMP default systimestamp,      -- 게시글작성일
  budate TIMESTAMP default systimestamp,      -- 게시글수정일
  bcontent CLOB not null,                     -- 게시글내용
  bhit NUMBER(5) default 0,                   -- 게시글조회수
  bstatus char(1) default 'E',                -- 게시글 상태(E:exist, D:delete)
  bid VARCHAR2(40) not null                   -- 아이디
);

--게시판 제약조건 생성
alter table board add Constraint board_bnum_pk primary key (bnum);                                        -- 기본키 생성
alter table board add constraint board_bid_fk foreign key(bid) references member(id) on delete cascade;   -- bid의 외래키 = 회원의 기본키(id)
alter table board add constraint board_bstatus_ck check (bstatus in ('E','D'));                           -- 게시글 상태(E:exist, D:delete)
alter table board add constraint board_bcategory_fk foreign key(bcategory) references code(code_id);

--게시판 시퀀스 생성
create sequence board_bnum_seq
start with 1 --시작값
increment by 1 --증감치
minvalue 0 --최소값
maxvalue 9999999999 --최대값
nocache --시퀀스 순차 증가 오류에 대응을 하지만 메모리에 미리 할당하지 않음에 대량으로 필요할 시 병목 현상 발생
nocycle; --순환하지않음

--8. 게시판_댓글
--게시판_댓글 테이블 생성
create table comments(
  cnum NUMBER(15) not null,                 -- 댓글번호
  ccdate TIMESTAMP default systimestamp,    -- 댓글작성일
  cudate TIMESTAMP default systimestamp,    -- 댓글수정일
  ccontent VARCHAR2(50) not null,           -- 댓글내용
  cid VARCHAR2(40) not null,                -- 아이디
  cbnum NUMBER(15) not null                 -- 게시글번호
);

--게시판_댓글 제약조건 생성
alter table comments add Constraint comments_cnum_pk primary key (cnum);                                            -- 기본키 생성
alter table comments add constraint comments_cid_fk foreign key(cid) references member(id) on delete cascade;       -- cid의 외래키 = 회원의 기본키(id)
alter table comments add constraint comments_cbnum_fk foreign key(cbnum) references board(bnum) on delete cascade;  -- cbnum의 외래키 = 게시판의 기본키(bnum)


-- 테이블 생성
CREATE TABLE emergency_medical (
    eeid NUMBER(10) PRIMARY KEY, -- 게시글 아이디
    ename VARCHAR2(100) NOT NULL, -- 이름
    ecdate TIMESTAMP default systimestamp, -- 게시글작성일
    eudate TIMESTAMP default systimestamp, -- 게시글수정일
    eaddress VARCHAR2(200) NOT NULL,  --주소
    ephone_number VARCHAR2(20) NOT NULL --전화번호
);

-- 시퀀스 생성
CREATE SEQUENCE emergency_medical_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- 샘플 데이터 삽입
INSERT INTO emergency_medical (eeid, ename, eaddress, ephone_number)
VALUES (emergency_medical_seq.NEXTVAL, '병원1', '주소1', '010-1111-2222');

INSERT INTO emergency_medical (eeid, ename, eaddress, ephone_number)
VALUES (emergency_medical_seq.NEXTVAL, '병원2', '주소2', '010-2222-3333');

INSERT INTO emergency_medical (eeid, ename, eaddress, ephone_number)
VALUES (emergency_medical_seq.NEXTVAL, '병원3', '주소3', '010-3333-4444');

CREATE TABLE tb_weather_area (
	areacode VARCHAR2(50) NOT NULL,
	step1 VARCHAR2(50) NOT NULL,
	step2 VARCHAR2(50) DEFAULT NULL,
	step3 VARCHAR2(50) DEFAULT NULL,
	gridX VARCHAR2(50) NOT NULL,
	gridY VARCHAR2(50) NOT NULL,
	longitudeHour VARCHAR2(50) NOT NULL,
	longitudeMin VARCHAR2(50) NOT NULL,
	longitudeSec VARCHAR2(50) NOT NULL,
	latitudeHour VARCHAR2(50) NOT NULL,
	latitudeMin VARCHAR2(50) NOT NULL,
	latitudeSec VARCHAR2(50) NOT NULL,
	longitudeMs VARCHAR2(50) NOT NULL,
	latitudeMs VARCHAR2(50) NOT NULL
)
TABLESPACE USERS
PCTFREE 10
INITRANS 1
MAXTRANS 255
STORAGE (
	INITIAL 64K
	NEXT 1M
	MINEXTENTS 1
	MAXEXTENTS UNLIMITED
)
NOCOMPRESS
NOPARALLEL
MONITORING;

CREATE TABLE tw_weather_response (
    baseDate VARCHAR2(50) NOT NULL,
    baseTime VARCHAR2(50) NOT NULL,
    category VARCHAR2(50) NOT NULL,
    nx VARCHAR2(50) NOT NULL,
    ny VARCHAR2(50) NOT NULL,
    obsrValue VARCHAR2(50) NOT NULL,
    CONSTRAINT pk_tw_weather_response PRIMARY KEY (baseDate, baseTime, category, nx, ny)
);

COMMENT ON TABLE tw_weather_response IS '날씨 API 호출 응답값 저장';
COMMENT ON COLUMN tw_weather_response.baseDate IS '발표일자';
COMMENT ON COLUMN tw_weather_response.baseTime IS '발표시각';
COMMENT ON COLUMN tw_weather_response.category IS '자료구분코드';
COMMENT ON COLUMN tw_weather_response.nx IS '예보지점X좌표';
COMMENT ON COLUMN tw_weather_response.ny IS '예보지점Y좌표';
COMMENT ON COLUMN tw_weather_response.obsrValue IS '실황 값';
--7. 코드
--코드 테이블 생성
create table code(
    code_id     varchar2(11),                   -- 코드
    decode      varchar2(30),                   -- 코드명
    discript    clob,                           -- 코드설명
    pcode_id    varchar2(11),                   -- 상위코드
    useyn       char(1) default 'Y',            -- 사용여부 (사용:'Y',미사용:'N')
    cdate       timestamp default systimestamp, -- 생성일시
    udate       timestamp default systimestamp  -- 수정일시
);
--기본키
alter table code add Constraint code_code_id_pk primary key (code_id);

--외래키
alter table code add constraint bbs_pcode_id_fk
    foreign key(pcode_id) references code(code_id);

--제약조건
alter table code modify decode constraint code_decode_nn not null;
alter table code modify useyn constraint code_useyn_nn not null;
alter table code add constraint code_useyn_ck check(useyn in ('Y','N'));

--샘플데이터 of code
insert into code (code_id,decode,pcode_id,useyn) values ('C01','커뮤니티',null,'Y');
insert into code (code_id,decode,pcode_id,useyn) values ('C0101','게시판','C01','Y');
insert into code (code_id,decode,pcode_id,useyn) values ('C0102','공지사항','C01','Y');
insert into code (code_id,decode,pcode_id,useyn) values ('C0103','QNA','C01','Y');
insert into code (code_id,decode,pcode_id,useyn) values ('F01','첨부',null,'Y');
insert into code (code_id,decode,pcode_id,useyn) values ('F0101','파일','F01','Y');
insert into code (code_id,decode,pcode_id,useyn) values ('F0102','이미지','F01','Y');

--9. 파일첨부
--파일첨부 테이블 생성
create table uploadfile(
  fnum NUMBER(15) not null,                     -- 첨부파일번호
  rnum NUMBER(15) not null,                     -- 참조번호(게시글번호 등)
  code varchar2(11),                            -- 분류코드
  store_filename VARCHAR2(50) not null,         -- 로컬파일명
  upload_filename VARCHAR2(50) not null,        -- 업로드파일명
  fsize VARCHAR2(50) not null,                  -- 파일크기(단위 byte)
  ftype VARCHAR2(50) not null,                  -- 파일타입(mimetype)
  fcdate TIMESTAMP default systimestamp,        -- 첨부날자
  fudate TIMESTAMP default systimestamp         -- 첨부수정날자
);

--파일첨부 제약조건 생성
alter table uploadfile add Constraint uploadfile_fnum_pk primary key (fnum);                            -- 기본키 생성
alter table uploadfile add constraint uploadfile_code_fk foreign key(code) references code(code_id);    -- 외래키 생성

--파일첨부 시퀀스 생성
create sequence uploadfile_fnum_seq
start with 1 --시작값
increment by 1 --증감치
minvalue 0 --최소값
maxvalue 9999999999 --최대값
nocache --시퀀스 순차 증가 오류에 대응을 하지만 메모리에 미리 할당하지 않음에 대량으로 필요할 시 병목 현상 발생
nocycle; --순환하지않음

--select * from uploadfile;

--즐겨찾기
create table favorite(
  fnum number(10),
  bnum number(8),
  id varchar2(40),
  fdate timestamp default systimestamp,
  constraint favorite_fnum_PK primary key(fnum),
  constraint favorite_id_FK foreign key(id)
                                 references member(id)
                                 ON DELETE CASCADE,
  constraint favorite_bnum_FK foreign key(bnum)
                                 references business(bnum)
                                 ON DELETE CASCADE
);





commit;

