'use strict';
  //dom 이벤트 : 댓글목록 불러오기
  document.addEventListener('DOMContentLoaded', e=>{
    console.log('DOM 생성');

    const isbn = document.querySelector('.form').dataset.isbn;
    list_f(isbn);
  });

  //댓글목록 조회 함수
  function list_f(isbn){
    console.log("list_f 호출됨!");
    const chk = document.querySelector('.form').dataset.loginChk;
    console.log("확인용"+chk);

    fetch(`http://localhost:9080/api/review/${isbn}`,{method:'GET'})
    .then(res => res.json())
    .then(res => {
    console.log(res);
    displayReviews_f(res.data);
    })
    .catch(err => console.error('Err:',err))
  }








  //댓글목록 출력 함수
  function displayReviews_f(Reviews){
    console.log("displayRevies_f 호출됨!");

    let content = ``;
    Reviews.forEach(review => {
      content +=
      //  `<div class="review">
      //     <table border="1px" width="500">
      ` <table border="1px" width="500">
         <div class="review">
            <tr>
              <td width=15%>
                <div class="review-head">
                  <input class="rnum" type="hidden" value="${review.rnum}">
                  <input class="rid" type="hidden" value="${review.rid}">
                  <span>${review.nickname}</span>
                </div>
              </td>
              <td width=40%>
                <div class="review-content" style="white-space:pre-wrap;">${review.rcontent}</div>
              </td>
              <td width=20%>              
                <span>${review.rscore}</span>
              </td>
              <td width=15%>
                <span>${review.rudate}</span>
              </td>
              <td width=10%>
              <span><a class="editBtn" href="">수정</a><a class="delBtn" href="">삭제</a></span>
              </td>
            </tr>
          </div>
        </table>`;

        // </table>
        // </div>`;
    });

    const $reviewList = document.querySelector('.review-list');
    $reviewList.innerHTML = content;
  }


//   $(function() {
//     $('#reply').keyup(function (e){
//         var content = $(this).val();
//         $(this).height(((content.split('\n').length + 1) * 1.5) + 'em');
//         $('#counter').html(content.length + '/30');
//     });
//     $('#reply').keyup();
// });





  //댓글등록 버튼 클릭시
  addBtn.addEventListener('click', e=>{
    const chk = document.querySelector('.form').dataset.loginChk;
    console.log(chk);

    //비로그인 상태 : 리다이렉트
    if(chk == "false"){
      console.log(chk);
      const isbn = document.querySelector('.form').dataset.isbn;
      location.href = `/login?redirectUrl=/booktest/${isbn}/detail`;
      return;
    //로그인 상태 : 댓글등록
    }else{



      // const $box = document.querySelector('.box');
    
      // $box.addEventListener('click', clickCheck);
      // function clickCheck(e) {
      //   if(e.target.tagName == 'LABEL'){
      //     document.querySelectorAll(`input[type=checkbox]`).forEach(el => el.checked = false);
  
      //     e.target.checked = true;
  
      //     console.log(e.target.parentNode.previousSibling.previousSibling.dataset.number);
  
      //     number.textContent = e.target.previousSibling.previousSibling.dataset.number;
  
      //   }
      // }
  






      const isbn = document.querySelector('.form').dataset.isbn;


      const reviewAddForm = {};
      reviewAddForm.rcontent = reply.value;
      // reviewAddForm.rscore = reply.value;


      fetch(`http://localhost:9080/api/review/${isbn}`,{
        method:'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(reviewAddForm)  // js객체를 => json 포맷 문자열 변환
      })
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        list_f(isbn);
      })
      .catch(err => console.error('Err:',err));
    }
  })

  //댓글수정 버튼 클릭시
  const $reviewArea = document.querySelector('.review-area');
  $reviewArea.addEventListener('click',e=>{
    //1) a태그 이벤트 막기
    e.preventDefault();
    const $review = e.target.closest('.review');

    //2) 수정 버튼 체크
//    if(e.target.textContent == '수정'){
//    }else if(e.target.textContent == '취소'){
//      console.log("댓글 수정 취소 버튼 클릭");
//    }else if(e.target.textContent == '취소'){
//      console.log("댓글 수정 저장 버튼 클릭");
//    }

    //a태그, button태그 아니면 리턴
    if(e.target.tagName != "A" && e.target.tagName != "BUTTON" && e.target.tagName != "LABEL"){
      console.log("리턴");
      return;
    }

    //등록이면 리턴
    if(e.target.textContent == "등록"){
      return;
    }

    //로그인 체크
    const id = document.querySelector('.form').dataset.id;
    console.log("로그인한 id : "+id);

    const rid = $review?.children[0].children[0].children[0].children[2].value;
    // const rid = $review?.children[0].children[0].children[0].children[0].children[1].value;
    console.log("댓글작성자 rid : "+rid);

    if(id != rid){
      console.log("로그인이 필요한 서비스입니다.");
      alert("로그인이 필요한 서비스입니다.");
      return;
    }

//    //로그인 체크
//    const id = document.querySelector('.form').dataset.id;
//    console.log("로그인한 id : "+id);
//
//    const rid = $review.children[0].children[1].value;
//    console.log("댓글작성자 cid : "+rid);
//
//    //로그인 상태 : 수정창 열기
//    if(id == rid){

    switch(e.target.textContent){
      case "수정":
          console.log("댓글 수정 버튼 클릭");

            //기존 수정창 닫기
            modifyModeCancel();

            //새 수정창 열기
            modifyMode($review, rid);
          break;
      case "취소":
          console.log("댓글 수정 취소 버튼 클릭");

          //기존 수정창 닫기
          modifyModeCancel();
          break;
      case "저장":
          console.log("댓글 수정 저장 버튼 클릭");

          modifyReply_f($review);
          break;
      case "삭제":
          console.log("댓글 삭제 버튼 클릭");

          if(confirm("정말 삭제하시겠습니까?")){
            deleteReply_f($review);
          }
          break;
    }//end of switch

//    //비로그인 상태 : 리턴
//    }else{
//      console.log("로그인이 필요한 서비스입니다.");
//      alert("로그인이 필요한 서비스입니다.");
//      return;
//    };

  })

  function modifyReply_f($review){
    console.log("modifyReply_f() 실행");

    const isbn = document.querySelector('.form').dataset.isbn;

    //객체 생성
    const reviewEditForm = {};
    reviewEditForm.rnum = $review.children[0].children[0].value;
    reviewEditForm.rid = $review.children[0].children[1].value;
    reviewEditForm.rcontent = $review.children[1].value;

    fetch(`http://localhost:9080/api/review/${isbn}`,{
      method:'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(reviewEditForm)  // js객체를 => json 포맷 문자열 변환
    })
    .then(res=>res.json())
    .then(res=>{
      console.log(res);
      list_f(isbn);
    })
    .catch(err => console.error('Err:',err));
  }

  function deleteReply_f($review){
    console.log("deleteReply_f() 실행");

    const rnum = $review.children[0].children[0].value;
    const isbn = document.querySelector('.form').dataset.isbn;

    fetch(`http://localhost:9080/api/review/${rnum}`,{method:'DELETE'})
    .then(res=>res.json())
    .then(res=>{
      console.log(res);
      list_f(isbn);
    })
    .catch(err => console.error('Err:',err));
  }

  //기존 수정창 닫기
  function modifyModeCancel(){
    if(document.getElementById("saveBtn") != null){
      console.log("modifyModeCancel() 실행");
      const $saveBtn = document.getElementById("saveBtn");
      const $closeReview = $saveBtn.closest('.review');
      console.log($closeReview);
      const rnum = $closeReview.children[0].children[0].value;
      fetch(`http://localhost:9080/api/review/${rnum}/detail`,{method:'GET'})
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        displayReview_f(res.data, $closeReview);
      })
      .catch(err => console.error('Err:',err));
    return;
    }
  }

  //댓글 단건 조회 : 기존 수정창 닫기용
  function displayReview_f(reviewDetailForm, $closeReview){
    $closeReview.innerHTML =
       `<div class="review">
          <div class="review-head">
            <input class="rnum" type="hidden" value="${reviewDetailForm.rnum}">
            <input class="rid" type="hidden" value="${reviewDetailForm.rid}">
            <span>${reviewDetailForm.nickname}</span>
            <span><a class="editBtn" href="">수정</a><a class="delBtn" href="">삭제</a></span>
          </div>
          <div class="review-content" style="white-space:pre-wrap;">${reviewDetailForm.rcontent}</div>
          <div><span>${reviewDetailForm.rudate}</span><a href="">대댓글쓰기</a></div>
        </div>`;
    return;
  }

  //수정창 열기
  function modifyMode($review, rid){
    console.log("modifyMode() 실행");
    const rnum = $review.children[0].children[0].value;
    const nickname = $review.children[0].children[2].textContent;
    const content = $review.children[1].textContent;
    const rudate = $review.children[2].children[0].textContent;
    console.log(rnum);
    console.log(nickname);
    console.log(content);
    console.log(rudate);

    $review.innerHTML =
    `<div class="review">
       <div class="review-head">
         <input class="rnum" type="hidden" value="${rnum}">
         <input class="rid" type="hidden" value="${rid}">
         <span>${nickname}</span>
         <span>${rudate}</span>
       </div>
       <textarea class="review-content cols="40" rows="1" maxlength="30" placeholder="30자 이내로 입력하세요"">${content}</textarea>

          <div class="box">
            <input type="checkbox" id="star-1">
            <label for="star-1"><i class="fa-solid fa-check"></i></label>
            <input type="checkbox" id="star-2">
            <label for="star-2"><i class="fa-solid fa-check"></i></label>
            <input type="checkbox" id="star-3">
            <label for="star-3"><i class="fa-solid fa-check"></i></label>
            <input type="checkbox" id="star-4">
            <label for="star-4"><i class="fa-solid fa-check"></i></label>
            <input type="checkbox" id="star-5">
            <label for="star-5"><i class="fa-solid fa-check"></i></label>
          </div>

       <div><button id="saveBtn">저장</button><button id="cancelBtn">취소</button></div>       </div>`;
    //alert("else");
    return;
  }//end of modifyMode

  //댓글삭제 버튼 클릭시

  //게시글 수정 버튼 클릭시
  const $editBtn = document.getElementById('editBtn');
  $editBtn?.addEventListener('click', e=>{
    const isbn = e.target.dataset.isbn;
    const url = `/boortest/${isbn}/edit`;
    location.href = url;
  })

  //게시글 삭제 버튼 클릭시
  const $delBtn = document.getElementById('delBtn');
  $delBtn?.addEventListener('click', e=>{
    //댓글이 달린경우 삭제가 안된다 알려야함
    if(window.confirm('댓글이 있는 게시글은 본문만 삭제됩니다.\n정말 삭제하시겠습니까?')){
      const isbn = e.target.dataset.isbn;
      const url = `/booktest/${isbn}/del`;
      location.href = url;
    }
  })