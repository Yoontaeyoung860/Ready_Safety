'use strict';
//
//document.addEventListener('DOMContentLoaded', event =>{
//    console.log("돔 생성");
//
//    const id = document.querySelector('.form').dataset.id;
//    list_f(id);
//});
//
//function list_f(id){
//    console.log("리스트호출");
//
//    fetch(`http://localhost:9080/api/book/${id}/list`, {method:'GET'})\
//    .then(res => res.json())
//    .then(res => {
//    displayBooks(res.data);
//    })
//    .catch(err => console.error('Err:', err))
//}
//
//function displayBooks(books){
//    const id = document.querySelector('.form').dataset.id;
//
//    let content = ``;
//    books.forEach(item => {
//        console.log("출력");
//        content +=
//               `<div class="item">
//                  <div class="item-header">
//                    <input class="cnum" type="hidden" value="${item.cnum}">
//                    <input class="cid" type="hidden" value="${item.cid}">
//                    <span>${comment.nickname}</span>
//                    <span><button class="editBtn" href="">수정</button><button class="delBtn" href="">삭제</button></span>
//                  </div>
//                  <div class="item-content" style="white-space:pre-wrap;">${item.ccontent}</div>
//                  <div class="item-footer"><span>${item.cudate}</span></div>
//                </div>`;
//    }
//
//    });
//    const $itemlist = document.querySelector('.detail-list');
//    $itemlist.innerHTML = content;
//}

//렌지바의 처음값은 dpage를 가져오기
const $rangeBar = document.querySelector('.range-bar');
let $range = document.querySelector('.range-bar').value;

//start페이지 처음값은 dpage를 가져오기
const $startPage = document.querySelector('.start-page');
let $start = document.querySelector('.start-page').value;

//end페이지 처음값은 spage를 가져오기
const $endPage = document.querySelector('.end-page');
let $end = document.querySelector('.end-page').value;

//렌지바를 움직이면 그 값이 startpage에 들어가기
$rangeBar.addEventListener('input', event => {
    $startPage.value = $rangeBar.value;
});

//startpage를 입력하고 엔터를 치면 렌지바의 값이 바뀌기
$startPage.addEventListener('keyup', event => {
    if (event.keyCode == 13) {
        console.log("$start:" + $start);
        $rangeBar.value = $startPage.value;
        console.log("$range:" + $range);
    }
});
//endpage를 입력하면 렌지바의 max값이 바뀌기
//endpage를 입력하면 spage 바꿔주기

$endPage.addEventListener('keyup', event => {
    if (event.keyCode == 13) {
        console.log("$end:" + $end);
        $rangeBar.max = $endPage.value;
        console.log("$rangeBar.max:" + $rangeBar.max);
        
    }
});

const $detailBtn = document.querySelector('.detail-btn');
$detailBtn.addEventListener('click', event => {
    const frm = document.querySelector('.detail-form');
    console.log("frm:" + frm);
    console.log(document.querySelector('.date').value);
    console.log(frm.dataset.dsnum);
    console.log($start);
    console.log(frm.dataset.isbn);
    //객체 생성(ddate,dsnum,dpage,sisbn)
    const insertForm = {};
    insertForm.ddate = document.querySelector('.date').value;
    insertForm.dsnum = frm.dataset.dsnum;
    insertForm.dpage = document.querySelector('.start-page').value
    insertForm.sisbn = frm.dataset.isbn;
    insertForm.spage = document.querySelector('.end-page').value

    fetch(`http://localhost:9080/api/book/save`,{
      method:'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(insertForm)  // js객체를 => json 포맷 문자열 변환
    })
    .then(res=>res.json())
    .then(res=>{
      console.log(res);
      list_f();
    })
    .catch(err => console.error('Err:',err));
});

const $listBtn = document.querySelector('.list-btn');
$listBtn.addEventListener('click', event => {
    location.href = '/book/list';
});

//const $detailBtn = document.querySelector('.detail-btn');
//$detailBtn.addEventListener('click', create_f);
//
//function create_f(e){
//    const data = {};
//    data.ddate = date.value;
//    data.spage = endPage.value;
//    data.dpage = startPage.value;
//
//    fetch('/api/book/${isbn}/save',{
//        method:'POST',
//        headers: { 'Content-Type':'application/json'},
//        body: JSON.stringify(data)})  // js객체 => json포맷 문자열 변환
//            .then(res=>res.json())         // json포맷 문자열 => js객체 변환
//            .then(res=>{console.log(res); list_f(); })
//            .catch(err=>{console.error('Err:',err)});
//        console.log('after fetch');
//}

document.addEventListener('DOMContentLoaded', list_f);

//상품목록 가져오기
function list_f(e){
  const isbn = document.querySelector('.detail-form').dataset.isbn;
  fetch(`http://localhost:9080/api/book/${isbn}/list`,{
    method:'GET'
  }).then(res=>res.json())
    .then(res=>{printItemList(res.data);})
    .catch(err=>{console.error('Err:',err)});
}

//상품목록 출력
function printItemList(list){
    let html = '';
    list.forEach(item => {
        html += `<p>`;
        html += `<span>기록날짜:${item.ddate}</span>`
        html += `<span>기록페이지:${item.dpage}</span>`
        html += `<span>총페이지:${item.spage}</span>`
        html += `<button class="delBtn" data-item-dnum="${item.dnum}">삭제</button>`;
        html += `</p>`;
    });
    detailList.innerHTML = html;
}

function delItem(event){
    console.log(event.target.dataset.dnum);
}

const $detailList = document.querySelector('.detail-list');
$detailList.addEventListener('click',e=>{
    if(e.target.className != 'delBtn') return;

    const dnum = e.target.dataset.itemDnum;

    fetch(`http://localhost:9080/api/book/${dnum}/delete`,{method:'DELETE'})
    .then(res=>res.json())
    .then(res=>{
      console.log(res);
      list_f();
    })
    .catch(err => console.error('Err:',err));
})