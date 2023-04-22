'use strict';
//검색
const $searchType = document.getElementById('searchType');
    console.log($searchType);
const $keyword    = document.getElementById('keyword');
    console.log($keyword);
const $searchBtn  = document.getElementById('searchBtn');
    console.log($searchBtn);

//검색 버튼 클릭시
$searchBtn?.addEventListener('click', search_f);

//키워드 입력필드에 엔터키 눌렀을때 검색
$keyword?.addEventListener('keydown', e=>{
    if(e.key === 'Enter') {
        search_f(e);
    }
});

function search_f(e){
    console.log("확인");
    //검색어입력 유무체크
    if($keyword.value.trim().length === 0){
       alert('검색어를 입력하세요');
       $keyword.focus();$keyword.select(); //커서이동
       return false;
    }
    const url = `/board/1/${$searchType.value}/${$keyword.value}`;
    location.href = url;
}

//페이징 버튼 style 부여
document.addEventListener('DOMContentLoaded', e=>{
    console.log('DOM 생성');

    //현재 url에서 'board' 문자열 위치 찾기
    const urlStr = window.location.href;
    const location = urlStr.indexOf('board');
    console.log(location);

    //'board' 뒤 페이지 숫자 파라미터 가져오기
    let targetPage = '';
    targetPage += urlStr.charAt(location + 6);
    targetPage += urlStr.charAt(location + 7);
    targetPage += urlStr.charAt(location + 8);

    //페이지 파라미터가 없는 경우 1페이지
    if(targetPage == ''){
      targetPage = '1';
    }
    console.log(targetPage);

    //페이지 버튼 요소 찾기
    const pageNum = document.getElementsByClassName('board-page-num');

    for( let i = 0; i < pageNum.length; i++ ){
      let ele = pageNum.item(i);

      //페이지 버튼의 텍스트가 현재 페이지와 일치시 색상 부여
      if(ele.textContent == targetPage){
        ele.style.backgroundColor = 'skyblue';
      }
    }
});
