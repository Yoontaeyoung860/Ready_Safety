'use strict';


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
