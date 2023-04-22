'use strict';
// bookImg.addEventListener('mouseOver', event => {
//     var positionX = $bookImg.position().left;
//     var positionY = $bookImg.position().top;
//     $bookImg.append('<button id="deleteImg" style="width: 20px; height: 20px; left:' + positionX + 'top:' + positionY + '">x</button>');
// })

// bookImg.addEventListener('mouseleave', event => {
//     $("#delete").remove();
// })

// const deleteImg = document.getElementById('deleteImg');
// deleteImg.addEventListener('click', event => {
//     const isbn = document.getElementById('list').isbn.value;
//     const url = `/book/{' + isbn + '}/delete`;
//     location.href = url;
// })


// function deleteClick(){
//  if(confirm('삭제하시겠습니까?')){
//    const isbn = document.getElementById('BookForm').isbn.value;
//    const url = `/book/${isbn}/delete`;
//    location.href = url;
//    }
//  }

const $shelfWrap = document.querySelector('.shelf-wrap');

//도서 목록 호출
document.addEventListener('DOMContentLoaded', list);
//도서 목록 json 받아오기
function list(){
    fetch(`http://localhost:9080/api/book`,{method:'GET'})
    .then(res => res.json())
    .then(res => {
      console.log(res);
      displaylist(res.data);
    })
    .catch(err => console.error('Err:',err))
}
//도서 목록 화면 출력
function displaylist(books){
    let content = `<img class="empty-Img" id="emptyImg" width="130px" height="150px">`;

    books.forEach(ele => {
      content += `<div class="bookshelf-list">
                      <img class="book-Img" id="bookImg" src="${ele.thumbnail}" width="130px" height="150px" data-isbn="${ele.isbn}">
                      <div class="book-title">${ele.title}</div>
                  </div>`
    })

    $shelfWrap.innerHTML = content;
}

$shelfWrap.addEventListener('click', e=>{
  if(e.target.className == 'empty-Img'){
    console.log('책장 추가 이미지');
    location.href = `/book/search`;
  }else if(e.target.className == 'book-Img'){
    console.log('도서 기록 버튼');
    const isbn = e.target.dataset.isbn;
    const url = "/book/" + isbn + "/detail";
    location.href = url;
  }
})

//$emptyImg.addEventListener('click', event => {
//    location.href = `/book/search`;
//})
//
//$bookImg.addEventListener('click', event => {
//
// const isbn = event.target.dataset.isbn;
// const url = "/book/" + isbn + "/detail";
// location.href = url;
//})