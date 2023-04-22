'use strict';
const $infoForm = document.querySelector('.info-form');
saveBtn.addEventListener('click', btn_check);
prevBtn.addEventListener('click', btn_check);

const $infoLink = document.querySelector('.info-link');
$infoLink.addEventListener('click', e=>{
    const isbn = e.target.dataset.isbn;
    const cisbn = isbn.split("");
    if(e.target.className == 'younpung'){
         const url = "https://www.ypbooks.co.kr/book.yp?bookcd=" + cisbn[0];
         window.open(url, '_blank');
    }else if(e.target.className == 'gyobo'){
        const url = "http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=" + cisbn[0];
        window.open(url, '_blank');
    }else if(e.target.className == 'yes'){
        const url = "http://www.yes24.com/Product/Goods/" + cisbn[0];
        window.open(url, '_blank');
    }else if(e.target.className == 'alladin'){
        const url = "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=" + cisbn[0];
        window.open(url, '_blank');
    }
})

const $pushSpage = document.querySelector('.push-spage');
$pushSpage.addEventListener('change', e=>{
    document.querySelector('.input-spage').value = $pushSpage.value;
})

//도서 버튼
function btn_check(e){
    if(e.target.id === "saveBtn")
    {
         const url = "/book/save";
         $infoForm.action = url;
         $infoForm.submit();

    }else if(e.target.id === "prevBtn"){

        const inputWord = document.querySelector('.input-word').value;
        console.log(inputWord);
        const url = "/book/search/"+inputWord;

        location.href = url;
    }
}

//별점 버튼
const $radioBtn = document.querySelector('.radioBtn');
$radioBtn.addEventListener('click',e=>{
    if(e.target.className != 'star-div') return;

    const $oneStar = document.querySelector('#one-star');
    const $twoStar = document.querySelector('#two-star');
    const $threeStar = document.querySelector('#three-star');
    const $fourStar = document.querySelector('#four-star');
    const $fiveStar = document.querySelector('#five-star');
    const $oneStarDiv = document.querySelector('.one-star-div');
    const $twoStarDiv = document.querySelector('.two-star-div');
    const $threeStarDiv = document.querySelector('.three-star-div');
    const $fourStarDiv = document.querySelector('.four-star-div');
    const $fiveStarDiv = document.querySelector('.five-star-div');

    switch(e.target.className){
        case "one-star-div":
            $oneStar.checked = true;
            break;
        case "two-star-div":
            $twoStar.checked = true;
            break;
        case "three-star-div":
            $threeStar.checked = true;
            break;
        case "four-star-div":
            $fourStar.checked = true;
            break;
        case "five-star-div":
            $fiveStar.checked = true;
            break;
    }
})

//dom 이벤트 : 리뷰목록 불러오기
document.addEventListener('DOMContentLoaded', e=>{
    console.log('DOM 생성');

    const risbn = document.querySelector('.input-isbn').value;
    list_f(risbn);
});

//리뷰목록 조회
function list_f(risbn){
    console.log("list_f 호출됨!");
    const chk = document.querySelector('.form').dataset.loginChk;
    console.log("확인용"+chk);

    fetch(`http://localhost:9080/api/review/${risbn}`,{method:'GET'})
    .then(res => res.json())
    .then(res => {
        console.log(res);
        displayreviews_f(res.data);
    })
    .catch(err => console.error('Err:',err))
}

//리뷰목록 출력
function displayreviews_f(reviews){
    console.log("displayComments_f 호출됨!");
    const id = document.querySelector('.form').dataset.id;

    let content = ``;
    reviews.forEach(review => {

//        //작성자 본인일시
//        if(id == review.rid){
//            prevReview_f(review.rnum);
//        }

        content +=
            `<div class="review">
                <div class="review-header">
                    <span>${review.nickname}</span>
                    <span>
                        <span style="${review.rstar >= 1 ? 'color:rgba(250, 208, 0, 0.99)' : 'color:#f0f0f0'}">★</span>
                        <span style="${review.rstar >= 2 ? 'color:rgba(250, 208, 0, 0.99)' : 'color:#f0f0f0'}">★</span>
                        <span style="${review.rstar >= 3 ? 'color:rgba(250, 208, 0, 0.99)' : 'color:#f0f0f0'}">★</span>
                        <span style="${review.rstar >= 4 ? 'color:rgba(250, 208, 0, 0.99)' : 'color:#f0f0f0'}">★</span>
                        <span style="${review.rstar >= 5 ? 'color:rgba(250, 208, 0, 0.99)' : 'color:#f0f0f0'}">★</span>

                    </span>
                </div>
                <div class="review-content" style="white-space:pre-wrap;">${review.rcontent}</div>
                <div class="review-footer"><span>${review.rudate}</span></div>
            </div>`;
    });

    const $reviewList = document.querySelector('.review-list');
    $reviewList.innerHTML = content;
}

  //리뷰등록 버튼 클릭시
  addBtn.addEventListener('click', e=>{
    const $reviewTextarea = document.querySelector('.review-textarea');
    const chk = document.querySelector('.form').dataset.loginChk;
    //    console.log(chk);

    //별값 가져오기
    let star = 0;
    const starList = document.getElementsByName('star');
    starList.forEach(ele => {
        if(ele.checked){
            star = ele.value;
        }
    })
    console.log(별);

    if(star == 0){
        alert("평점을 선택해주세요.");
        return;
    }

    if($reviewTextarea.value.trim() == ''){
        alert("리뷰를 작성해주세요.");
        return;
    }

    //비로그인 상태 : 리다이렉트
    if(chk == "false"){
      console.log(chk);
      alert("로그인을 하신 후 이용해 주시기 바랍니다.");
//      const bnum = document.querySelector('.form').dataset.bnum;
//      location.href = `/login?redirectUrl=/board/${bnum}/detail`;
      return;
    //로그인 상태 : 댓글등록
    }else{
      const risbn = document.querySelector('.input-isbn').value;
      const $reviewTextarea = document.querySelector('.review-textarea');

      const reviewAddForm = {};
      reviewAddForm.rcontent = $reviewTextarea.value;
      reviewAddForm.rstar = star;

      fetch(`http://localhost:9080/api/review/${risbn}`,{
        method:'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(reviewAddForm)  // js객체를 => json 포맷 문자열 변환
      })
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        list_f(risbn);
        $reviewTextarea.value = '';//댓글창 비우기
      })
      .catch(err => console.error('Err:',err));
    }
  })


//  function prevReview_f(rnum){
//    console.log("prevReview_f 호출됨!");
//    const $reviewTextarea = document.querySelector('.review-textarea');
//
//    fetch(`http://localhost:9080/api/review/${rnum}/detail`,{method:'GET'})
//    .then(res => res.json())
//    .then(res => {
//        console.log(res);
//        displayreviews_f(res.data);
//        $reviewTextarea.value = res.data.rcontent;
//        document.findElementByAttribute("value", res.data.rstar).checked = true;
//    })
//    .catch(err => console.error('Err:',err))
//  }
//
//
//  //댓글수정 버튼 클릭시
//  const $reviewArea = document.querySelector('.review-area');
//  $reviewArea.addEventListener('click',e=>{
////    //1) a태그 이벤트 막기
////    e.preventDefault();
//    const $review = e.target.closest('.review');
//
//    //2) 수정 버튼 체크
////    if(e.target.textContent == '수정'){
////    }else if(e.target.textContent == '취소'){
////      console.log("댓글 수정 취소 버튼 클릭");
////    }else if(e.target.textContent == '취소'){
////      console.log("댓글 수정 저장 버튼 클릭");
////    }
//
//    //a태그, button태그 아니면 리턴
////    if(e.target.tagName != "A" && e.target.tagName != "BUTTON"){
//    if(e.target.tagName != "BUTTON"){
//      console.log("리턴");
//      return;
//    }
//
//    //등록이면 리턴
//    if(e.target.textContent == "등록"){
//      return;
//    }
//
//    //로그인 체크
//    const id = document.querySelector('.form').dataset.id;
//    console.log("로그인한 id : "+id);
//
//    const rid = $review?.children[0].children[1].value;
//    console.log("댓글작성자 rid : "+rid);
//
//    if(id != rid){
//      console.log("로그인이 필요한 서비스입니다.");
//      alert("로그인이 필요한 서비스입니다.");
//      return;
//    }
//
////    //로그인 체크
////    const id = document.querySelector('.form').dataset.id;
////    console.log("로그인한 id : "+id);
////
////    const cid = $comment.children[0].children[1].value;
////    console.log("댓글작성자 cid : "+cid);
////
////    //로그인 상태 : 수정창 열기
////    if(id == cid){
//
//    switch(e.target.textContent){
//      case "수정":
//          console.log("리뷰 수정 버튼 클릭");
//
//            //기존 수정창 닫기
//            modifyModeCancel();
//
//            //새 수정창 열기
//            modifyMode($review, rid);
//          break;
//      case "취소":
//          console.log("리뷰 수정 취소 버튼 클릭");
//
//          //기존 수정창 닫기
//          modifyModeCancel();
//          break;
//      case "저장":
//          console.log("리뷰 수정 저장 버튼 클릭");
//
//          modifyReply_f($review);
//          break;
//      case "삭제":
//          console.log("리뷰 삭제 버튼 클릭");
//
//          if(confirm("정말 삭제하시겠습니까?")){
//            deleteReply_f($review);
//          }
//          break;
//    }//end of switch
//
////    //비로그인 상태 : 리턴
////    }else{
////      console.log("로그인이 필요한 서비스입니다.");
////      alert("로그인이 필요한 서비스입니다.");
////      return;
////    };
//
//  })
//
//  function modifyReply_f($review){
//    console.log("modifyReply_f() 실행");
//
//    const risbn = document.querySelector('.input-isbn').value;
//
//    //객체 생성
//    const revirwEditForm = {};
//    revirwEditForm.rnum = $review.children[0].children[0].value;
//    revirwEditForm.rid = $review.children[0].children[1].value;
//    revirwEditForm.rcontent = $review.children[1].value;
//
//    fetch(`http://localhost:9080/api/review/${risbn}`,{
//      method:'PATCH',
//      headers: {
//        'Content-Type': 'application/json',
//      },
//      body: JSON.stringify(revirwEditForm)  // js객체를 => json 포맷 문자열 변환
//    })
//    .then(res=>res.json())
//    .then(res=>{
//      console.log(res);
//      list_f(risbn);
//    })
//    .catch(err => console.error('Err:',err));
//  }
//
//  function deleteReply_f($review){
//    console.log("deleteReply_f() 실행");
//
//    const rnum = $review.children[0].children[0].value;
//    const risbn = document.querySelector('.input-isbn').value;
//
//    fetch(`http://localhost:9080/api/review/${risbn}`,{method:'DELETE'})
//    .then(res=>res.json())
//    .then(res=>{
//      console.log(res);
//      list_f(risbn);
//    })
//    .catch(err => console.error('Err:',err));
//  }
//
//  //기존 수정창 닫기
//  function modifyModeCancel(){
//    if(document.getElementById("saveReviewBtn") != null){
//      console.log("modifyModeCancel() 실행");
//      const $saveReviewBtn = document.getElementById("saveReviewBtn");
//      const $closeReview = $saveReviewBtn.closest('.comment');
//      console.log($closeReview);
//      const cnum = $closeReview.children[0].children[0].value;
//      fetch(`http://localhost:9080/api/review/${rnum}/detail`,{method:'GET'})
//      .then(res=>res.json())
//      .then(res=>{
//        console.log(res);
//        displayreviews_f(res.data, $closeReview);
//      })
//      .catch(err => console.error('Err:',err));
//    return;
//    }
//  }
//
//  //댓글 단건 조회 : 기존 수정창 닫기용
//  function displayComment_f(reviewDetailForm, $closeReview){
//    $closeReview.innerHTML =
//       `<div class="review">
//          <div class="review-header">
//            <input class="rnum" type="hidden" value="${reviewDetailForm.rnum}">
//            <input class="rid" type="hidden" value="${reviewDetailForm.rid}">
//            <span>${reviewDetailForm.nickname}</span>
//            <span><button class="editBtn" href="">수정</button><button class="delBtn" href="">삭제</button></span>
//          </div>
//          <div class="review-content" style="white-space:pre-wrap;">${reviewDetailForm.rcontent}</div>
//          <div class="review-footer"><span>${reviewDetailForm.rudate}</span></div>
//        </div>`;
//    return;
//  }
//
//  //수정창 열기
//  function modifyMode($review, rid){
//    console.log("modifyMode() 실행");
//    const rnum = $review.children[0].children[0].value;
//    const nickname = $review.children[0].children[2].textContent;
//    const content = $review.children[1].textContent;
//    const rudate = $review.children[2].children[0].textContent;
//    console.log(rnum);
//    console.log(nickname);
//    console.log(content);
//    console.log(rudate);
//
//    $review.innerHTML =
//    `<div class="review">
//       <div class="review-header">
//         <input class="rnum" type="hidden" value="${rnum}">
//         <input class="rid" type="hidden" value="${rid}">
//         <span>${nickname}</span>
//         <span>${rudate}</span>
//       </div>
//       <textarea class="review-textarea">${content}</textarea>
//       <div class="review-footer"><div class="btn-right"><button id="saveReviewBtn">저장</button><button id="cancelBtn">취소</button></div></div>
//     </div>`;
//    //alert("else");
//    return;
//  }//end of modifyMode