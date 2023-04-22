'use strict';
//* textArea => ck_editor 대체
ClassicEditor
		.create( document.querySelector( '#bcontent' ), {
		 plugin:['ListStyle','Markdown','MediaEmbed','MediaEmbedToolbar'],
            toolbar: [],//상단툴바 제거
			language: 'en',
			image: {
				toolbar: [
					'imageTextAlternative',
					'imageStyle:full',
					'imageStyle:side'
				]
			},
			table: {
				contentToolbar: [
					'tableColumn',
					'tableRow',
					'mergeTableCells',
					'tableCellProperties',
					'tableProperties'
				]
			},
		})
		.then( editor => {
			window.editor = editor;
			editor.isReadOnly = true;//읽기모드적용
		} )
		.catch( error => {
			console.error( error );
		} );

//게시글 수정 버튼 클릭시
const $editBtn = document.getElementById('editBtn');
$editBtn?.addEventListener('click', e=>{
  const bnum = e.target.dataset.bnum;
  const url = `/board/${bnum}/edit`;
  location.href = url;
})

//게시글 삭제 버튼 클릭시
const $delBtn = document.getElementById('delBtn');
$delBtn?.addEventListener('click', e=>{
  //댓글이 달린경우 삭제가 안된다 알려야함
  if(window.confirm("삭제하시겠습니까?")){
    const bnum = e.target.dataset.bnum;
    const url = `/board/${bnum}/del`;
    location.href = url;
  }
})


  //dom 이벤트 : 댓글목록 불러오기
  document.addEventListener('DOMContentLoaded', e=>{
    console.log('DOM 생성');

    const bnum = document.querySelector('.form').dataset.bnum;
    list_f(bnum);
  });

  //댓글목록 조회 함수
  function list_f(bnum){
    console.log("list_f 호출됨!");
    const chk = document.querySelector('.form').dataset.loginChk;
    console.log("확인용"+chk);

    fetch(`http://localhost:9080/api/comment/${bnum}`,{method:'GET'})
    .then(res => res.json())
    .then(res => {
    console.log(res);
    displayComments_f(res.data);
    })
    .catch(err => console.error('Err:',err))
  }

  //댓글목록 출력 함수
  function displayComments_f(comments){
    console.log("displayComments_f 호출됨!");
    const id = document.querySelector('.form').dataset.id;

    let content = ``;
    comments.forEach(comment => {

    //작성자 본인일시
    if(id == comment.cid){
      console.log("일치");
      content +=
       `<div class="comment">
          <div class="comment-header">
            <input class="cnum" type="hidden" value="${comment.cnum}">
            <input class="cid" type="hidden" value="${comment.cid}">
            <span>${comment.nickname}</span>
            <span><button class="editBtn" href="">수정</button><button class="delBtn" href="">삭제</button></span>
          </div>
          <div class="comment-content" style="white-space:pre-wrap;">${comment.ccontent}</div>
          <div class="comment-footer"><span>${comment.cudate}</span></div>
        </div>`;

    //작성자가 아닐시
    }else if(id != comment.cid){
        console.log("불일치");
        content +=
         `<div class="comment">
            <div class="comment-header">
              <input class="cnum" type="hidden" value="${comment.cnum}">
              <input class="cid" type="hidden" value="${comment.cid}">
              <span>${comment.nickname}</span>
              <span></span>
            </div>
            <div class="comment-content" style="white-space:pre-wrap;">${comment.ccontent}</div>
            <div class="comment-footer"><span>${comment.cudate}</span></div>
          </div>`;
    }

//      content +=
//       `<div class="comment">
//          <div class="comment-head">
//            <input class="cnum" type="hidden" value="${comment.cnum}">
//            <input class="cid" type="hidden" value="${comment.cid}">
//            <span>${comment.nickname}</span>
//            <span><a class="editBtn" href="">수정</a><a class="delBtn" href="">삭제</a></span>
//          </div>
//          <div class="comment-content" style="white-space:pre-wrap;">${comment.ccontent}</div>
//          <div><span>${comment.cudate}</span></div>
//        </div>`;
    });

    const $commentList = document.querySelector('.comment-list');
    $commentList.innerHTML = content;
  }

  //댓글등록 버튼 클릭시
  addBtn.addEventListener('click', e=>{
    const chk = document.querySelector('.form').dataset.loginChk;
    console.log(chk);

    //비로그인 상태 : 리다이렉트
    if(chk == "false"){
      console.log(chk);
      alert("로그인을 하신 후 이용해 주시기 바랍니다.");
//      const bnum = document.querySelector('.form').dataset.bnum;
//      location.href = `/login?redirectUrl=/board/${bnum}/detail`;
      return;
    //로그인 상태 : 댓글등록
    }else{
      const bnum = document.querySelector('.form').dataset.bnum;
      const $commentTextarea = document.querySelector('.comment-textarea');

      const addForm = {};
      addForm.ccontent = $commentTextarea.value;


      fetch(`http://localhost:9080/api/comment/${bnum}`,{
        method:'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(addForm)  // js객체를 => json 포맷 문자열 변환
      })
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        list_f(bnum);
        $commentTextarea.value = '';//댓글창 비우기
      })
      .catch(err => console.error('Err:',err));
    }
  })

  //댓글수정 버튼 클릭시
  const $commentArea = document.querySelector('.comment-area');
  $commentArea.addEventListener('click',e=>{
//    //1) a태그 이벤트 막기
//    e.preventDefault();
    const $comment = e.target.closest('.comment');

    //2) 수정 버튼 체크
//    if(e.target.textContent == '수정'){
//    }else if(e.target.textContent == '취소'){
//      console.log("댓글 수정 취소 버튼 클릭");
//    }else if(e.target.textContent == '취소'){
//      console.log("댓글 수정 저장 버튼 클릭");
//    }

    //a태그, button태그 아니면 리턴
//    if(e.target.tagName != "A" && e.target.tagName != "BUTTON"){
    if(e.target.tagName != "BUTTON"){
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

    const cid = $comment?.children[0].children[1].value;
    console.log("댓글작성자 cid : "+cid);

    if(id != cid){
      console.log("로그인이 필요한 서비스입니다.");
      alert("로그인이 필요한 서비스입니다.");
      return;
    }

//    //로그인 체크
//    const id = document.querySelector('.form').dataset.id;
//    console.log("로그인한 id : "+id);
//
//    const cid = $comment.children[0].children[1].value;
//    console.log("댓글작성자 cid : "+cid);
//
//    //로그인 상태 : 수정창 열기
//    if(id == cid){

    switch(e.target.textContent){
      case "수정":
          console.log("댓글 수정 버튼 클릭");

            //기존 수정창 닫기
            modifyModeCancel();

            //새 수정창 열기
            modifyMode($comment, cid);
          break;
      case "취소":
          console.log("댓글 수정 취소 버튼 클릭");

          //기존 수정창 닫기
          modifyModeCancel();
          break;
      case "저장":
          console.log("댓글 수정 저장 버튼 클릭");

          modifyReply_f($comment);
          break;
      case "삭제":
          console.log("댓글 삭제 버튼 클릭");

          if(confirm("정말 삭제하시겠습니까?")){
            deleteReply_f($comment);
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

  function modifyReply_f($comment){
    console.log("modifyReply_f() 실행");

    const bnum = document.querySelector('.form').dataset.bnum;

    //객체 생성
    const editForm = {};
    editForm.cnum = $comment.children[0].children[0].value;
    editForm.cid = $comment.children[0].children[1].value;
    editForm.ccontent = $comment.children[1].value;

    fetch(`http://localhost:9080/api/comment/${bnum}`,{
      method:'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(editForm)  // js객체를 => json 포맷 문자열 변환
    })
    .then(res=>res.json())
    .then(res=>{
      console.log(res);
      list_f(bnum);
    })
    .catch(err => console.error('Err:',err));
  }

  //댓글 삭제
  function deleteReply_f($comment){
    console.log("deleteReply_f() 실행");

    const cnum = $comment.children[0].children[0].value;
    const bnum = document.querySelector('.form').dataset.bnum;

    fetch(`http://localhost:9080/api/comment/${cnum}`,{method:'DELETE'})
    .then(res=>res.json())
    .then(res=>{
      console.log(res);
      list_f(bnum);
    })
    .catch(err => console.error('Err:',err));
  }

  //기존 수정창 닫기
  function modifyModeCancel(){
    if(document.getElementById("saveBtn") != null){
      console.log("modifyModeCancel() 실행");
      const $saveBtn = document.getElementById("saveBtn");
      const $closeComment = $saveBtn.closest('.comment');
      console.log($closeComment);
      const cnum = $closeComment.children[0].children[0].value;
      fetch(`http://localhost:9080/api/comment/${cnum}/detail`,{method:'GET'})
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        displayComment_f(res.data, $closeComment);
      })
      .catch(err => console.error('Err:',err));
    return;
    }
  }

  //댓글 단건 조회 : 기존 수정창 닫기용
  function displayComment_f(detailForm, $closeComment){
    $closeComment.innerHTML =
       `<div class="comment">
          <div class="comment-header">
            <input class="cnum" type="hidden" value="${detailForm.cnum}">
            <input class="cid" type="hidden" value="${detailForm.cid}">
            <span>${detailForm.nickname}</span>
            <span><button class="editBtn" href="">수정</button><button class="delBtn" href="">삭제</button></span>
          </div>
          <div class="comment-content" style="white-space:pre-wrap;">${detailForm.ccontent}</div>
          <div class="comment-footer"><span>${detailForm.cudate}</span></div>
        </div>`;
    return;
  }

  //수정창 열기
  function modifyMode($comment, cid){
    console.log("modifyMode() 실행");
    const cnum = $comment.children[0].children[0].value;
    const nickname = $comment.children[0].children[2].textContent;
    const content = $comment.children[1].textContent;
    const cudate = $comment.children[2].children[0].textContent;
    console.log(cnum);
    console.log(nickname);
    console.log(content);
    console.log(cudate);

    $comment.innerHTML =
    `<div class="comment">
       <div class="comment-header">
         <input class="cnum" type="hidden" value="${cnum}">
         <input class="cid" type="hidden" value="${cid}">
         <span>${nickname}</span>
         <span>${cudate}</span>
       </div>
       <textarea class="comment-textarea">${content}</textarea>
       <div class="comment-footer"><div class="btn-right"><button id="saveBtn">저장</button><button id="cancelBtn">취소</button></div></div>
     </div>`;
    //alert("else");
    return;
  }//end of modifyMode