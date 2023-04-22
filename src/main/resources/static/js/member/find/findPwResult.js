'use strict';

       findPwBtn.addEventListener('click', findByPw);
  
  
      function findByPw(e){
          const data ={};
          data.id = idPwTxt.value;
      data.name = namePwTxt.value;
      data.email = emailPwTxt.value;
  
          fetch('/memberexist/exist/findpw',{
        method:'PUT',
        headers:{'Content-Type':'application/json'},
        body:JSON.stringify(data)
      }).then(res=>res.json())
        .then(res=>{console.log(res);
          findedPw.innerHTML = res.data;}
        )
        .catch(err=>console.error('Err:',err));
  
  
      }
  
  
  
  