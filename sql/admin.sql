-- 계정 삭제 (모든 계정이 관련된 것을 삭제)  -> SYSTEM으로 실행
DROP USER c##ready_safety CASCADE;
-- 계정 생성하면서 할당
CREATE USER c##ready_safety IDENTIFIED BY 1234 DEFAULT TABLESPACE users TEMPORARY TABLESPACE temp PROFILE DEFAULT;
-- 계정 권한 설정
GRANT CONNECT, RESOURCE TO c##ready_safety;
GRANT CREATE VIEW, CREATE SYNONYM TO c##ready_safety;
GRANT UNLIMITED TABLESPACE TO c##ready_safety;
ALTER USER c##ready_safety ACCOUNT UNLOCK;
