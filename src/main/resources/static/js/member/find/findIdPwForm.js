'use strict';

function div_show(selectList) {
  var obj1 = document.getElementById("find_id_form");
  var obj2 = document.getElementById("find_pw_form");

  if( selectList == "0" ) {
      obj1.style.display = "block";
      obj2.style.display = "none";
  } else if( selectList == "1" ) {
      obj1.style.display = "none";
      obj2.style.display = "block";
  }

}

findIdBtn.addEventListener('click', findById);
  
function findById(e){
  const data ={};
  data.name = idname.value;
  data.email = idemail.value;

  if(data.name == "" || data.email == "") {
    alert('데이터를 입력해주세요')
    return;
  }

//       fetch('/memberexist/exist/findid',{
//     method:'PUT',
//     headers:{'Content-Type':'application/json'},
//     body:JSON.stringify(data)
//   }).then(res=>res.json())
//     .then(res=>{console.log(res);
//       findedId.innerHTML = res.data;}
//     )
//     .catch(err=>console.error('Err:',err));

  findIdForm.submit();
}

  
findPwBtn.addEventListener('click', findByPw);
  
  
function findByPw(e){
  const data ={};
  data.id = pwid.value;
  data.name = pwname.value;
  data.email = pwemail.value;

  if(data.id == "" || data.name == "" || data.email == "") {
    alert('데이터를 입력해주세요')
    return;
  }

//       fetch('/memberexist/exist/findpw',{
//     method:'PUT',
//     headers:{'Content-Type':'application/json'},
//     body:JSON.stringify(data)
//   }).then(res=>res.json())
//     .then(res=>{console.log(res);
//       findedPw.innerHTML = res.data;}
//     )
//     .catch(err=>console.error('Err:',err));

  findPwForm.submit();
}
  
document.addEventListener('DOMContentLoaded', e=>{
  const urlStr = window.location.href;
  const location = urlStr.indexOf('findidpw');

  let target = '';
  target += urlStr.charAt(location + 9);
  target += urlStr.charAt(location + 10);

  if(target == 'id'){
    find_id.checked = true;
    div_show('0');
  }else if(target == 'pw'){
    find_pw.checked = true;
    div_show('1');
  }else{
    find_id.checked = true;
    div_show('0');
  }
})