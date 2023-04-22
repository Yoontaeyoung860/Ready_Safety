'use strict';

    //id 유효성 체크 상태
    const ValidChkStatus = {
      id:false
    }

    const $id       = document.getElementById('id');
    const $idDupChk = document.getElementById('idDupChk');

    $idDupChk.addEventListener('click', e=>{
      const xmlHttpreq = new XMLHttpRequest();

      const url = `/memberexist/id/${$id.value}/exist`;
      xmlHttpreq.open("GET",url);
      xmlHttpreq.send(id);

      xmlHttpreq.addEventListener('load', e=>{
        if(xmlHttpreq.status ===200){ //성공적으로 서버응답 받으면
          console.log(xmlHttpreq.response);
          const result = JSON.parse(xmlHttpreq.response); //Json포맷 문자열 => JS객체로변환
          console.log(result);
          // const $errmsg = $idDupChk.closest('li').querySelector('.errmsg');
          // const $errmsg = $idDupChk.closest('div').querySelector('.errmsg');
          const $errmsg = $idDupChk.closest('div').querySelector('#id_errmsg');

          if(result.rtcd === '00'){
            //alert('이미 사용되고 있는 아이디 입니다.');
            $errmsg.textContent = '이미 사용되고 있는 아이디 입니다.';
            $errmsg.style.display = 'block';

          }else{
            $errmsg.textContent = '';
            $errmsg.style.display = 'none';
            $idDupChk.textContent = '사용가능';
            // $idDupChk.disabled = 'disabled';
            // $id.disabled = 'disabled';
            validChkStatus.id = true;
          }
        }else{
          console.log('Error', xmlHttpreq.status, xmlHttpreq.statusText);
        }
      });
    });




    //email 유효성 체크 상태
    const valemailChkStatus = {
      email:false
    }

    const $email       = document.getElementById('email');
    const $emailDupChk = document.getElementById('emailDupChk');

    $emailDupChk.addEventListener('click', e=>{
      const xmlHttpreq = new XMLHttpRequest();

      const url = `/memberexist/email/${$email.value}/exist`;
      xmlHttpreq.open("GET",url);
      xmlHttpreq.send(email);

      xmlHttpreq.addEventListener('load', e=>{
        if(xmlHttpreq.status ===200){ //성공적으로 서버응답 받으면
          console.log(xmlHttpreq.response);
          const result = JSON.parse(xmlHttpreq.response); //Json포맷 문자열 => JS객체로변환
          console.log(result);
          // const $errmsg = $emailDupChk.closest('div').querySelector('.errmsg');
          const $errmsg = $emailDupChk.closest('div').querySelector('#email_errmsg');

          if(result.rtcd === '00'){
            //alert('이미 사용되고 있는 이메일 입니다.');
            $errmsg.textContent = '이미 사용되고 있는 이메일 입니다.';
            $errmsg.style.display = 'block';

          }else{
            $errmsg.textContent = '';
            $errmsg.style.display = 'none';
            $emailDupChk.textContent = '사용가능';
            // $emailDupChk.disabled = 'disabled';
            // $email.disabled = 'disabled';
            valemailChkStatus.email = true;
          }
        }else{
          console.log('Error', xmlHttpreq.status, xmlHttpreq.statusText);
        }
      });
    });

    //nickname 유효성 체크 상태
    const valnicknameChkStatus = {
      nickname:false
    }

    const $nickname       = document.getElementById('nickname');
    const $nicknameDupChk = document.getElementById('nicknameDupChk');

    $nicknameDupChk.addEventListener('click', e=>{
      const xmlHttpreq = new XMLHttpRequest();

      const url = `/memberexist/nickname/${$nickname.value}/exist`;
      xmlHttpreq.open("GET",url);
      xmlHttpreq.send(nickname);

      xmlHttpreq.addEventListener('load', e=>{
        if(xmlHttpreq.status ===200){ //성공적으로 서버응답 받으면
          console.log(xmlHttpreq.response);
          const result = JSON.parse(xmlHttpreq.response); //Json포맷 문자열 => JS객체로변환
          console.log(result);
          // const $errmsg = $nicknameDupChk.closest('div').querySelector('.errmsg');
          const $errmsg = $nicknameDupChk.closest('div').querySelector('#nickname_errmsg');


          if(result.rtcd === '00'){
            //alert('이미 사용되고 있는 닉네임 입니다.');
            $errmsg.textContent = '이미 사용되고 있는 닉네임 입니다.';
            $errmsg.style.display = 'block';

          }else{
            $errmsg.textContent = '';
            $errmsg.style.display = 'none';
            $nicknameDupChk.textContent = '사용가능';
            // $nicknameDupChk.disabled = 'disabled';
            // $nickname.disabled = 'disabled';
            valnicknameChkStatus.nickname = true;
          }
        }else{
          console.log('Error', xmlHttpreq.status, xmlHttpreq.statusText);
        }
      });
    });









    //회원가입 버튼 클릭시
    signupedBtn.addEventListener('click', e=>{

      //중복체크 미이행시
      if(!validChkStatus.id){
        alert('아이디 중복체크 바랍니다');
        $id.focus();
        $id.select();
        return;

      }
      if(!valemailChkStatus.email){
        alert('이메일 중복체크 바랍니다');
        $email.focus();
        $email.select();
        return;
      }
      if(!valnicknameChkStatus.nickname){
        alert('닉네임 중복체크 바랍니다');
        $nickname.focus();
        $nickname.select();
        return;
      }

      e.target.closest('form').submit();
    });
