'use strict';

    findPwBtn.addEventListener('click', findByPw);


    <!--    fetch(`/api/comments/${search.value}`,{-->
    <!--      method:'PATCH',-->
    <!--      headers:{'Content-Type':'application/json'},-->
    <!--      body:JSON.stringify(data)-->
    <!--    }).then(res=>res.json())-->
    <!--      .then(res=>{console.log(res); findAll_f();})-->
    <!--      .catch(err=>console.error('Err:',err))    ;-->
    
        function findByPw(e){
            const data ={};
            data.id = idTxt.value;
        data.name = nameTxt.value;
        data.email = emailTxt.value;
    
            fetch('/memberexist/exist/findpw',{
          method:'PUT',
          headers:{'Content-Type':'application/json'},
          body:JSON.stringify(data)
        }).then(res=>res.json())
          .then(res=>{console.log(res);
            findedPw.innerHTML = res.data;}
          )
          .catch(err=>console.error('Err:',err));
    
    <!--      const xhr = new XMLHttpRequest();-->
    <!--      xhr.open('PUT','/api/findid');-->
    <!--      xhr.send(data);-->
    <!--      xhr.addEventListener('load',e=>{-->
    <!--        //console.log(xhr);-->
    <!--        if(xhr.status === 200){-->
    <!--          //console.log(xhr.response); //json포맷의 문자열 수신확인-->
    <!--          const obj = JSON.parse(xhr.response);//json포맷의 문자열 => js객체변환-->
    <!--          if(obj.rtcd === '00'){-->
    <!--            findedId.textContent = obj.data;-->
    <!--          }else{-->
    <!--            findedId.textContent = obj.data;-->
    <!--          }-->
    <!--        }else{-->
    <!--          console.err('Error', xhr.status, xhr.statusText);-->
    <!--        }-->
    <!--      });-->
        }