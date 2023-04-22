/**
 * 
 */
 'use strict'
 
//리뷰 등록별점
	let one = document.querySelector('.one');
	let two = document.querySelector('.two');
	let three = document.querySelector('.three');
	let four = document.querySelector('.four');
	let five = document.querySelector('.five');


	let star = document.querySelectorAll('.reviewForm__score');

	let score=0;
	
	function score1(){
	  star.forEach(ele=>ele.classList.remove('reviewForm__checked'));
	  one.classList.add('reviewForm__checked');
	  score=1
	}
	function score2(){
	  score1();
	  two.classList.add('reviewForm__checked');
	  score=2
	}
	function score3(){
	  score2();
	  three.classList.add('reviewForm__checked');
	  score=3
	}
	function score4(){
	  score3();
	  four.classList.add('reviewForm__checked');
	  score=4
	}
	function score5(){
	  score4();
	  five.classList.add('reviewForm__checked');
	  score=5
	}
		
	one?.addEventListener('click',score1);
	two?.addEventListener('click',score2);
	three?.addEventListener('click',score3);
	four?.addEventListener('click',score4);
	five?.addEventListener('click',score5);
	


//등록후 등록란 초기화
function review_input_init(){
	let html =``;
  html+=`  <form name="reviewForm" class="reviewform" method="post" action="#" enctype="multipart/form-data">`;
  html+=`      <p class="title_star">별점과 리뷰를 남겨주세요.</p>`;
  html+=`      <div class="review_rating">`;
  html+=`          <p class="warning_msg">별점을 선택해 주세요.</p>`;
  html+=`          <div class="rscore">`;
  html+=`              <!-- 해당 별점을 클릭하면 해당 별과 그 왼쪽의 모든 별의 체크박스에 radio 적용 -->`;
  html+=`              <input type="radio" name="rscore" id="point1" value="1" title="1점" hidden>`;
  html+=`              <label for="point1"><i class="fas fa-star reviewForm__score one reviewForm__checked"></i></label>`;
  html+=`              <input type="radio" name="rscore" id="point2" value="2" title="2점" hidden>`;
  html+=`              <label for="point2"><i class="fas fa-star reviewForm__score two reviewForm__checked"></i></label>`;
  html+=`              <input type="radio" name="rscore" id="point3" value="3" title="3점" hidden>`;
  html+=`              <label for="point3"><i class="fas fa-star reviewForm__score three reviewForm__checked"></i></label>`;
  html+=`              <input type="radio" name="rscore" id="point4" value="4" title="4점" hidden>`;
  html+=`              <label for="point4"><i class="fas fa-star reviewForm__score four reviewForm__checked"></i></label>`;
  html+=`              <input type="radio" name="rscore" id="point5" value="5" title="5점" hidden checked>`;
  html+=`              <label for="point5"><i class="fas fa-star reviewForm__score five reviewForm__checked"></i></label>`;
  html+=`          </div>`;
  html+=`      </div>`;
  html+=`      <!-- 리뷰등록란 -->`;
  html+=`      <div class="review__contents">`;
  html+=`          <div class="warning_msg">5자 이상으로 작성해 주세요.</div>`;
  html+=`          <textarea rows="10" class="review__textarea" name="rcontent"></textarea>`;
  html+=`          <div class="review__contents-btns">`;
  html+=`					 <div>`
  html+=`						 <label for="files" class="review__fileBtn">사진등록</label>`
  html+=`						 <span class="file-text"></span>`
  html+=`            <input type="file" class="review__files" accept="image/*" name="files" multiple="multiple" hidden onchange="uploadChange(this);"/>`;
  html+=`						</div>`
  html+=`            <button class="review__regist" type="button">등록</button>`;
  html+=`          </div>`;
  html+=`      </div>   `;
  html+=`  </form>`;
  
  document.querySelector('.review__regi-input').innerHTML=html;
  //이벤트 재등록
  regiBtn = document.querySelector('.review__regist');  //등록하기
  regiBtn?.addEventListener('click',regiBtn_f);
  //별점 함수
  one = document.querySelector('.one');
  two = document.querySelector('.two');
  three = document.querySelector('.three');
  four = document.querySelector('.four');
  five = document.querySelector('.five');
  star = document.querySelectorAll('.reviewForm__score');
	
	one?.addEventListener('click',e=>score1());
	two?.addEventListener('click',e=>score2());
	three?.addEventListener('click',e=>score3());
	four?.addEventListener('click',e=>score4());
	five?.addEventListener('click',e=>score5());
}
 
 
 
 
//  호출하는 값
//const $id = html에서 호출	//아이디
// const bnum = document.querySelector('.selected-busi').dataset.bnum;		//업체번호
//  버튼
let regiBtn = document.querySelector('.review__regist');  //등록하기
let modiFrmBtns = document.querySelectorAll('.review__modi-frm'); //수정폼띄우기
let delBtns = document.querySelectorAll('.review__del');		//삭제하기

//  리뷰등록
const regiBtn_f = e =>{
	
	console.log('리뷰등록');
	
	const rcontent = document.querySelector('.reviewform .review__textarea');
	const rscore = document.querySelector('.reviewform input[name="rscore"]:checked');
	const risbn = document.querySelector('.form').dataset.isbn;
	const rid = document.querySelector('review__column ')
	

	//리뷰입력체크
	if(!rcontent.value) {
			alert("리뷰 내용을 입력하세요");
			return;
	}	
	
	const URL = `/review/`;
/*	const data = {
			"bnum" : bnum,
			"rcontent": rcontent.value,
			"rscore" : rscore.value,
			"id" : $id //,
			//"files" : files.files };*/

	const formData = new FormData();
	formData.append('rcontent',rcontent.value);
	console.log("rcontent : "+rcontent);
	formData.append('rscore',rscore.value);
	console.log("rscore : "+rscore);
	formData.append('risbn',risbn);
	console.log("risbn : "+risbn);
	formData.append('rid', $rid);
	console.log("rid : "+rid);
	// for(let i=0; i<$files.files.length; i++){
	// 	console.log($files.files[i])
	// 	formData.append('files',$files.files[i]);
	// }													 
										 	
	request.mpost(URL,formData)
			.then(res=>res.json())
			.then(res=>{
					console.log('0',res);
					if(res.rtcd == '00'){
							//성공로직처리
							const data = res.data;
							//리뷰목록갱신
							refreshReview(data);
							//리뷰입력창 초기화
							review_input_init();
					}else{
						alert(res.rtmsg);
						console.log('1',res.rtmsg);
						throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
					//오류로직 처리
					console.log('2',err.message);
					alert(err.message);
			});
};



//수정폼 버튼 클릭시, 수정폼+수정버튼 출력
const modiFrmBtns_f = e=> {
	let modiForms = document.querySelectorAll('.review__modiForm');
	
	//다른 수정폼 있을시
	if(modiForms.length!=0){
		if(confirm("이미 수정 중인 내용은 삭제됩니다.")){
			modiForms.forEach(ele=>ele.remove());
		}
	}
	//텍스트 수정폼, 기존 리뷰 아래 생성
	const modiForm = document.createElement('div');
	modiForm.classList.add('review__modiForm');
	e.target.closest('.review__column').append(modiForm);
	//이미지 수정폼, 새로운 열로 생성
	const $review_modi_img = document.createElement('div');
	$review_modi_img.classList.add('review__img-modiForm');
	$review_modi_img.classList.add('review__column');
	const $row = e.target.closest('.review__row');
	$row.append($review_modi_img);
	
	const rnum = e.target.dataset.rnum;
	const rid = e.target.dataset.rid;

	const URL = `/review/?rnum=${rnum}`;
	// &id=${rid}`;

	request.get(URL)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == "00"){
							//성공로직처리
							const data = res.data;
							//원래 내용은 숨김처리
							e.target.closest('.review__main-text').classList.add('hidden');
							$row.querySelector('.review__imgs').classList.add('hidden');
							//리뷰폼띄우기
							reviewModiForm(data);
							reviewImgModiForm(data);
					}else{
							throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
				//오류로직 처리
					console.log(err.message);
					alert(err.message);
			})
	
}

//리뷰수정폼출력 //생성된 디브의 inner에 삽입
function reviewModiForm(review) {

	let html ='';
	switch(review.rscore){
		case 1:
			html += `<div class="rscore" ${review.rscore}>`;
			html += `	<input type="radio" name="rscore" id="mpoint1" value="1" title="1점" hidden checked>`;
			html += `	<label for="mpoint1"><i class="fas fa-star reviewModiForm__score modi-one reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint2" value="2" title="2점" hidden>`;
			html += `	<label for="mpoint2"><i class="fas fa-star reviewModiForm__score modi-two"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint3" value="3" title="3점" hidden>`;
			html += `	<label for="mpoint3"><i class="fas fa-star reviewModiForm__score modi-three"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint4" value="4" title="4점" hidden>`;
			html += `	<label for="mpoint4"><i class="fas fa-star reviewModiForm__score modi-four"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint5" value="5" title="5점" hidden>`;
			html += `	<label for="mpoint5"><i class="fas fa-star reviewModiForm__score modi-five"></i></label>`;
			html += `</div>`;
		break;
		case 2:
			html += `<div class="rscore" ${review.rscore}>`;
			html += `	<input type="radio" name="rscore" id="mpoint1" value="1" title="1점" hidden>`;
			html += `	<label for="mpoint1"><i class="fas fa-star reviewModiForm__score modi-one reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint2" value="2" title="2점" hidden checked>`;
			html += `	<label for="mpoint2"><i class="fas fa-star reviewModiForm__score modi-two reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint3" value="3" title="3점" hidden>`;
			html += `	<label for="mpoint3"><i class="fas fa-star reviewModiForm__score modi-three"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint4" value="4" title="4점" hidden>`;
			html += `	<label for="mpoint4"><i class="fas fa-star reviewModiForm__score modi-four"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint5" value="5" title="5점" hidden>`;
			html += `	<label for="mpoint5"><i class="fas fa-star reviewModiForm__score modi-five"></i></label>`;
			html += `</div>`;
		break;
		case 3:
			html += `<div class="rscore" ${review.rscore}>`;
			html += `	<input type="radio" name="rscore" id="mpoint1" value="1" title="1점" hidden>`;
			html += `	<label for="mpoint1"><i class="fas fa-star reviewModiForm__score modi-one reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint2" value="2" title="2점" hidden>`;
			html += `	<label for="mpoint2"><i class="fas fa-star reviewModiForm__score modi-two reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint3" value="3" title="3점" hidden checked>`;
			html += `	<label for="mpoint3"><i class="fas fa-star reviewModiForm__score modi-three reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint4" value="4" title="4점" hidden>`;
			html += `	<label for="mpoint4"><i class="fas fa-star reviewModiForm__score modi-four"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint5" value="5" title="5점" hidden>`;
			html += `	<label for="mpoint5"><i class="fas fa-star reviewModiForm__score modi-five"></i></label>`;
			html += `</div>`;
		break;
		case 4:
			html += `<div class="rscore" ${review.rscore}>`;
			html += `	<input type="radio" name="rscore" id="mpoint1" value="1" title="1점" hidden>`;
			html += `	<label for="mpoint1"><i class="fas fa-star reviewModiForm__score modi-one reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint2" value="2" title="2점" hidden>`;
			html += `	<label for="mpoint2"><i class="fas fa-star reviewModiForm__score modi-two reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint3" value="3" title="3점" hidden>`;
			html += `	<label for="mpoint3"><i class="fas fa-star reviewModiForm__score modi-three reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint4" value="4" title="4점" hidden checked>`;
			html += `	<label for="mpoint4"><i class="fas fa-star reviewModiForm__score modi-four reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint5" value="5" title="5점" hidden>`;
			html += `	<label for="mpoint5"><i class="fas fa-star reviewModiForm__score modi-five"></i></label>`;
			html += `</div>`;
		break;
		case 5:
			html += `<div class="rscore" ${review.rscore}>`;
			html += `	<input type="radio" name="rscore" id="mpoint1" value="1" title="1점" hidden>`;
			html += `	<label for="mpoint1"><i class="fas fa-star reviewModiForm__score modi-one reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint2" value="2" title="2점" hidden>`;
			html += `	<label for="mpoint2"><i class="fas fa-star reviewModiForm__score modi-two reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint3" value="3" title="3점" hidden>`;
			html += `	<label for="mpoint3"><i class="fas fa-star reviewModiForm__score modi-three reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint4" value="4" title="4점" hidden>`;
			html += `	<label for="mpoint4"><i class="fas fa-star reviewModiForm__score modi-four reviewForm__checked"></i></label>`;
			html += `	<input type="radio" name="rscore" id="mpoint5" value="5" title="5점" hidden checked>`;
			html += `	<label for="mpoint5"><i class="fas fa-star reviewModiForm__score modi-five reviewForm__checked"></i></label>`;
			html += `</div>`;
		break;

	} 
	html += `<div class="review__contents">`;
	html += `	<div class="warning_msg">5자 이상으로 작성해 주세요.</div>`;
	html += `	<textarea cols="50" rows="10" class="review__textarea-modi" name="rcontent">${review.rcontent}</textarea>`;
	html += ` <div class="review__modiBtns">`
	html += `		<button data-rnum="${review.rnum}" class="review__modi" type="button">수정</button>`;
	html += `		<button class="review__modiCancle" type="button">취소</button>`;
	html += ` </div>`
	html += `</div>`;

	document.querySelector('.review__modiForm').innerHTML = html;
	modi_score();
	const modiBtn = document.querySelector('.review__modi');		//수정처리
	const cancleBtn = document.querySelector('.review__modiCancle');		//수정취소
	modiBtn.addEventListener('click', modiBtn_f);
	cancleBtn.addEventListener('click',e=>{ 
		//텍스트 수정폼 되돌리기
		document.querySelector('.review__modiForm').remove()
		document.querySelectorAll('.review__main-text').forEach(ele=>ele.classList.remove('hidden'));
		//이미지 수정폼 되돌리기
		document.querySelector('.review__img-modiForm').remove();
		document.querySelectorAll('.review__imgs').forEach(ele=>ele.classList.remove('hidden'));
	});
}

//리뷰사진 X버튼 클릭시 사진 삭제
const review_img_delBtn_f = e=>{
	const $fnum = e.target.dataset.fnum;
	const $rnum = e.target.dataset.rnum;
	
	const URL = `/review/del?fnum=${$fnum}&rnum=${$rnum}`;
	
	request.delete(URL)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == "00"){
							//성공로직처리
							const data = res.data;
							//댓글목록갱신
							reviewImgModiForm(data);
					}else{
						alert(res.rtmsg);
						throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
					//오류로직 처리
					console.log(err.message);
					alert(err.message);
			});
	
}

function reviewImgModiForm(review){
	let html = '';
		review.files.forEach(img=>{
			html+=`     <div class="review__modi-img" name="files">`;
			html+=`       <img class="review__img" src="/images/${img.store_fname}" alt="첨부이미지">`;
			html+=`       <i data-fnum=${img.fnum} data-rnum=${review.rnum} class="fas fa-times-circle review_img_delBtn"></i>`;
			html+=`     </div>`;
		})
		html+=`	 <label for="modi_files" class="file-btn">사진추가</label>`;
		html+=`		<input class="review__modi-file" type="file" value="${review.files}"  name="files"  id="modi_files"  multiple onchange="uploadChange(this);">`
	//html삽입
	document.querySelector('.review__img-modiForm').innerHTML = html;
	//이벤트 등록
	document.querySelectorAll('.review_img_delBtn').forEach(ele=>
	ele.addEventListener('click',review_img_delBtn_f));
}

//평점수정 함수
	//리뷰 수정별점
	function modi_score(){
		let mone = document.querySelector('.modi-one');
		let mtwo = document.querySelector('.modi-two');
		let mthree = document.querySelector('.modi-three');
		let mfour = document.querySelector('.modi-four');
		let mfive = document.querySelector('.modi-five');
	
	
		const mstar = document.querySelectorAll('.reviewModiForm__score');
	
		let score=0;
		
		function mscore1(){
			mstar.forEach(ele=>ele.classList.remove('reviewForm__checked'));
			mone.classList.add('reviewForm__checked');
			score=1
		}
		function mscore2(){
			mscore1();
			mtwo.classList.add('reviewForm__checked');
			score=2
		}
		function mscore3(){
			mscore2();
			mthree.classList.add('reviewForm__checked');
			score=3
		}
		function mscore4(){
			mscore3();
			mfour.classList.add('reviewForm__checked');
			score=4
		}
		function mscore5(){
			mscore4();
			mfive.classList.add('reviewForm__checked');
			score=5
		}
	
		mone.addEventListener('click',mscore1);
		mtwo.addEventListener('click',mscore2);
		mthree.addEventListener('click',mscore3);
		mfour.addEventListener('click',mscore4);
		mfive.addEventListener('click',mscore5);
	}
	

//리뷰수정처리
const modiBtn_f = e =>{

	console.log('리뷰수정');
	
  const rnum = e.target.dataset.rnum;
	const modiContent = document.querySelector('.review__textarea-modi');
	const modiRscore = document.querySelector('.review__modiForm input:checked');
	const $files = document.querySelector('.review__modi-file');
	
	//리뷰입력체크
	if(!modiContent.value) {
			alert("리뷰 내용을 입력하세요");
			return;
	}	
	
	const URL = `/review/`;
/*	const data = {
			"bnum": bnum,
			"rnum": rnum,
			"rcontent": modiContent.value,
			"rscore" : modiRscore.value,
			"id" : $id
													 };*/
	const formData = new FormData();
	formData.append('bnum',bnum);
	formData.append('rnum',rnum);
	formData.append('rcontent',modiContent.value);
	formData.append('rscore',modiRscore.value);
	formData.append('id', $id);
	for(let i=0; i<$files.files.length; i++){
		console.log($files.files[i])
		formData.append('files',$files.files[i]);
	}
	
	request.mpatch(URL,formData)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == '00'){
							//성공로직처리
							const data = res.data;
							//댓글목록갱신
							refreshReview(data);
					}else{
							throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
					//오류로직 처리
					console.log(err.message);
					alert(err.message);
			});
};//수정처리

//리뷰삭제
const delBtn_f = e => {
	if(!confirm('한번 삭제한 리뷰는 되돌릴 수 없어요. 정말 리뷰를 지우시겠어요?')){
		return;
	}
	
	const rnum = e.target.dataset.rnum;
	
	const URL = `/review/?rnum=${rnum}`;
	
	request.delete(URL)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == "00"){
							//성공로직처리
							const data = res.data;
							//댓글목록갱신
							refreshReview(data);
					}else{
						alert(res.rtmsg);
						throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
					//오류로직 처리
					console.log(err.message);
					alert(err.message);
			});
	
};

// 리뷰 새로 고침
function refreshReview(data){
	let html ='';
	html += `<div class="review__cnt"><i class="far fa-comment-dots"></i><span>리뷰수 : ${data.length}</span></div>`;
	html += `<section class="review">`;
	data.forEach(review=>{
		html += `<div class="review__row" data-rnum="${review.rnum}">`;
		/* 프로필 시작*/
		html += `	<div class="review__column">`;
		review.store_fname
				 ? html += `<img class="profile__sm" src="/img/upload/member/${review.store_fname}" alt="프로필사진">` 
				 : html += `<img class="profile__sm" src="/img/upload/member/puppy2.png" alt="기본프로필사진">`;
		html += `		<span class="review__nickname">${review.nickname}</span>`;
		html += ` </div>`;
		/* 프로필 종료*/
		html += `	<div class="review__column">`;
		html += `		<div class="review__main-text">`
		/*별점*/
		switch(review.rscore){
			case 1:
				html += `			<div class="review__star-score">`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `			</div>`;
			break;
			case 2:
				html += `			<div class="review__star-score">`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `			</div>`;
			break;
			case 3:
				html += `			<div class="review__star-score">`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `			</div>`;
			break;
			case 4:
				html += `			<div class="review__star-score">`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="far fa-star"></i>`;
				html += `			</div>`;
			break;
			case 5:
				html += `			<div class="review__star-score">`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `				<i class="fas fa-star"></i>`;
				html += `			</div>`;
			break;
		}
		/*내용 및 수정/삭제 버튼*/
		html += `		<p class="review__content">${review.rcontent}</p>`;
		if($id == review.id){
			html += `		<div>`;
			html += `			<p class="review__userBtns">`;
			html += `				<span data-rnum="${review.rnum}" data-id="${review.id}" class="review__userBtn review__modi-frm">수정</span>`;
			html += `				<span>|</span>`;
			html += `				<span data-rnum="${review.rnum}" data-id="${review.id}" class="review__userBtn review__del">삭제</span>`;
			html += `			</p>`;
			html += `		</div>`;
		}
			/* 사장님 댓글 시작	*/
		html+= `<div class="review__reply" data-rnum="${review.rnum}">`
		//리댓달기 버튼
		if($id==$busi.id && review.rvReply==null){	
			html += `<p class="review__replyBtn">답글달기</p>`
		}
		//리댓 있을 시 출력
		if(review.rvReply){
			html += `<p class="review__reply-text">└─>사장님 : ${review.rvReply}</p>`
			html += `<p class="review__ownerBtns">`
			html +=	`	<span class="review__ownerModiBtn">수정</span>`
			html +=	`	<span>|</span>`	
			html +=	`	<span class="review__ownerDelBtn">삭제</span>`	
			html +=	`</p>`	
		}
		html+= `</div>`
			/* 사장님 댓글	종료 */
		html += `		<div>`//작성일시작
		html += `			<span class="review__date">작성일자 : ${review.rvcdate}</span>`;
		if(review.rvudate) {
			html += `			<span class="review__isUpdate">수정됨</span>`;
		}
		html += `		</div>`;//작성일 종료
		html += `		</div>`//메인텍스트 종료
		html += `	</div>`;
		/*첨부 파일 시작*/
		html += `	<div class="review__column review__imgs">`;
  	review.files.forEach(imgs=>{
    	html+=`     <img class="review__img" src="/images/${imgs.store_fname}" alt="첨부 이미지" />`;
    });
    html += `	</div>`;
		/*첨부파일 종료*/
		html += `</div>`;
		/*각 리뷰row 종료*/
	})
	html += `</section>`;
	/*리뷰 종료*/
	document.querySelector('.review__container').innerHTML = html;
			
	//버튼 재호출 + 이벤트리스너 재등록
	modiFrmBtns = document.querySelectorAll('.review__modi-frm'); //수정폼띄우기
	modiFrmBtns?.forEach(ele=>ele.addEventListener('click',modiFrmBtns_f));

	delBtns = document.querySelectorAll('.review__del');		//삭제하기
	delBtns?.forEach(ele=>ele.addEventListener('click',delBtn_f));
	
	/*사장님 버튼*/
	//등록버튼
	replyBtns = document.querySelectorAll('.review__replyBtn');
	replyBtns?.forEach(ele=>ele.addEventListener('click',replyBtns_f));
	//수정버튼
	replyModiBtns = document.querySelectorAll('.review__ownerModiBtn')
	replyModiBtns?.forEach(ele=>ele.addEventListener('click',replyBtns_f));
	//삭제버튼
	replyDelBtns = document.querySelectorAll('.review__ownerDelBtn')
	replyDelBtns?.forEach(ele=>ele.addEventListener('click',replyDelBtns_f));
	
}
//각 버튼 이벤트리스너 등록
regiBtn.addEventListener('click',regiBtn_f);
delBtns.forEach(ele=>ele.addEventListener('click',delBtn_f));
modiFrmBtns.forEach(ele=>ele.addEventListener('click',modiFrmBtns_f));




//회원리뷰에 사장님 댓글

//댓글 등록 처리

const addBtn_f = e=> {

	const content = document.querySelector('.rvReply__content');
	const rnum = e.target.closest('.review__reply').dataset.rnum;
	const bnum = $busi.bnum;

		//리뷰입력체크
		if(!content.value) {
			alert("댓글 내용을 입력하세요");
			return;
	}	
	//TODO 댓글 등록하는 폼만들고 해당값 입력
	const URL = `/review/reply`;
	const data = {
			"rnum" : rnum,
			"bnum" : bnum,
			"rvReply": content.value,
													 };
													 	
	request.patch(URL,data)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == '00'){
							//성공로직처리
							const data = res.data;
							//리뷰목록갱신
							refreshReview(data);
					}else{
						alert(res.rtmsg);
						throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
					//오류로직 처리
					console.log(err.message);
					alert(err.message);
			});
}


//댓글폼 출력
const replyBtns_f = e =>{

	if(document.querySelector('.review__reply-form')!=null){
		if(confirm('기존 작성 중인 내용은 삭제됩니다.')){
			document.querySelector('.review__reply-form').remove()
		} else {
			return
		}
	}
		const replyForm = document.createElement('div');
		replyForm.classList.add('review__reply-form');
		e.target.closest('.review__reply').append(replyForm);

	const rnum = e.target.closest('.review__reply').dataset.rnum;

	const URL = `/review/reply?rnum=${rnum}`;

	request.get(URL)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == "00"){
							//성공로직처리
							const data = res.data;
							//리댓폼 출력
							addReply(data);
							//수정시, 기존 내용부분 숨김처리
							e.target.closest('.review__reply').querySelector('.review__reply-text')?.classList.add('hidden');
							e.target.closest('.review__reply').querySelector('.review__ownerBtns')?.classList.add('hidden');
					}else{
							throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
				//오류로직 처리
					console.log(err.message);
					alert(err.message);
			})
}


//댓글폼 출력 함수
function addReply(review){
	let html='';
	html += `<div class="rvReply">`;
	html += `	<div class="warning_msg">5자 이상으로 작성해 주세요.</div>`;
	html += `	<textarea class="rvReply__content" name="rvReply" id="" cols="30" rows="10">${review.rvReply}</textarea>`;
	html += `	<div class="rvReply__btns">`;
	html += `		<button class="rvReply__btn rvReply__addBtn">확인</button>`;
	html += `		<button class="rvReply__btn rvReply__cancle">취소</button>`;
	html += `	</div>`;
	html += `</div>`;

	const replyForm = document.querySelector('.review__reply-form')
	replyForm.innerHTML=html;
	//등록버튼-등록처리
	const addBtn = document.querySelector('.rvReply__addBtn');
	addBtn.addEventListener('click',addBtn_f);
	//취소버튼
	const cancleBtn = document.querySelector('.rvReply__cancle');
	cancleBtn.addEventListener('click',e=>{
		//숨김한 기존 내용 보이기
		e.target.closest('.review__reply').querySelector('.review__reply-text')?.classList.remove('hidden');
		e.target.closest('.review__reply').querySelector('.review__ownerBtns')?.classList.remove('hidden');
		//수정폼 삭제
		replyForm.remove();
		});
}
//사장님 답글 삭제(리뷰의 리댓값 null로 수정)
const replyDelBtns_f = e=> {

	const rnum = e.target.closest('.review__reply').dataset.rnum;
	const bnum = $busi.bnum;
	const bid = $busi.id;//사장아이디

	//TODO 댓글 등록하는 폼만들고 해당값 입력
	const URL = `/review/reply/del?bid=${bid}`;
	const data = {
			"rnum" : rnum,
			"bnum" : bnum,
													 };
													 	
	request.patch(URL,data)
			.then(res=>res.json())
			.then(res=>{
					if(res.rtcd == '00'){
							//성공로직처리
							const data = res.data;
							//리뷰목록갱신
							refreshReview(data);
					}else{
						alert(res.rtmsg);
						throw new Error(res.rtmsg);
					}
			})
			.catch(err=>{
					//오류로직 처리
					console.log(err.message);
					alert(err.message);
			});
}

	/*사장님 버튼*/
//등록버튼
let replyBtns = document.querySelectorAll('.review__replyBtn');
replyBtns?.forEach(ele=>ele.addEventListener('click',replyBtns_f));
//수정버튼
let replyModiBtns = document.querySelectorAll('.review__ownerModiBtn')
replyModiBtns?.forEach(ele=>ele.addEventListener('click',replyBtns_f));
//삭제버튼
let replyDelBtns = document.querySelectorAll('.review__ownerDelBtn')
replyDelBtns?.forEach(ele=>ele.addEventListener('click',replyDelBtns_f));
