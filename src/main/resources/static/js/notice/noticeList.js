'use strict';

  const $writeBtn = document.getElementById('writeBtn');
    $writeBtn.addEventListener('click',()=>{
      const url = `/notices`;
      location.href = url
    });

  //검색
  const $searchType = document.getElementById('searchType');
  const $keyword    = document.getElementById('keyword');
  const $searchBtn  = document.getElementById('searchBtn');

  //검색 버튼 클릭시
  $searchBtn?.addEventListener('click', search_f);

  //키워드 입력필드에 엔터키 눌렀을때 검색
  $keyword?.addEventListener('keydown', e=>{
      if(e.key === 'Enter') {
          search_f(e);
      }
  });

  function search_f(e){
      //검색어입력 유무체크
      if($keyword.value.trim().length === 0){
         alert('검색어를 입력하세요');
         $keyword.focus();$keyword.select(); //커서이동
         return false;
      }
      const url = `/notices/all/1/${$searchType.value}/${$keyword.value}`;
      location.href = url;
  }