'use strict';
  
    findIdBtn.addEventListener('click', findById);
  
      function findById(e){
        const data ={};
        data.name = nameIdTxt.value;
        data.email = emailIdTxt.value;

          fetch('/memberexist/exist/findid',{            
        method:'PUT',
        headers:{'Content-Type':'application/json'},
        body:JSON.stringify(data)
      }).then(res=>res.json())
        .then(res=>{console.log(res);
          findedId.innerHTML = res.data;}
        )
        .catch(err=>console.error('Err:',err));
  
      }

