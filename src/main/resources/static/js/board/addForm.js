'use strict';
//* textArea => ck_editor 대체
ClassicEditor
		.create( document.querySelector( '#bcontent' ), {
		 plugin:['ListStyle','Markdown','MediaEmbed','MediaEmbedToolbar'],
			toolbar: {
				items: [
					'heading',
					'|',
					'underline',
					'bold',
					'italic',
					'link',
					'bulletedList',
					'numberedList',
					'todoList',
					'|',
					'outdent',
					'indent',
					'|',
					'imageInsert',
					'imageUpload',
					'blockQuote',
					'insertTable',
					'mediaEmbed',
					'markdown',
					'undo',
					'redo',
					'|',
					'highlight',
					'fontFamily',
					'fontColor',
					'fontBackgroundColor',
					'fontSize',
					'|',
					'htmlEmbed',
					'specialCharacters'
				]
			},
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
		} )
		.catch( error => {
			console.error( error );
		} );

//* 이벤트 막기(Capturing) : 작성 도중 페이지를 나갈 때 작성 종료 여부 질문
document.body.addEventListener('click', e=>{
  //1. addBtn 버튼이 아닌 a 및 button 태그일 때 실행
  if(e.target.id == "addBtn") return;
  if(e.target.tagName == "A" || e.target.tagName == "BUTTON"){
    //2. 작성 종료에 동의하지 않으면 이후 이벤트(페이지 이동)를 막음
    if(!confirm("작성을 종료하시겠습니까?")){
      //동일한 DOM 에 걸린 이벤트를 막기(a 태그)
      if (e.preventDefault) e.preventDefault();
      else e.returnValue = false;//IE 대응

      //동일한 DOM 에 걸린 이벤트를 막기(button 태그)
      if (e.stopImmediatePropagation) e.stopImmediatePropagation();
      else e.isImmediatePropagationEnabled = false;//IE 대응
    }
  }
}, true)//이벤트 캡쳐링(Capturing)

//* 등록 버튼(Bubbling) : 제목과 본문을 입력 후 등록
addBtn.addEventListener('click', e=>{
  //1. 제목 입력 확인
  if(document.querySelector('.title').value.trim() == ''){
    alert("제목을 입력하세요.");
    return;
  }
  //2. 본문 입력 확인
  if(document.querySelector('.ck-content').textContent.trim() == ''){
    alert("본문을 입력하세요.");
    return;
  }
  //3. 제목과 본문 입력시 form 태그 submit
  const $formTag = document.querySelector('.addForm');
  const url = "/board/add";
  $formTag.action = url;
  $formTag.submit();
}, false)//이벤트 버블링(Bubbling)

//* 목록 버튼(Bubbling)
listBtn.addEventListener('click', e=>{
  const url = "/board";
  location.href = url;
})