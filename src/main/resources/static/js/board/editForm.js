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

//이벤트 막기(Capturing)
document.body.addEventListener('click', e=>{
  if(e.target.id == "saveBtn") return;
  if(e.target == $files) return;
  if(e.target.tagName == "A" || e.target.tagName == "BUTTON"){
    if(!confirm("수정을 취소하시겠습니까?")){
      //동일한 DOM 에 걸린 이벤트를 막기(a 태그)
      if (e.preventDefault) e.preventDefault();
      else e.returnValue = false;//IE 대응

      //동일한 DOM 에 걸린 이벤트를 막기(button 태그)
      if (e.stopImmediatePropagation) e.stopImmediatePropagation();
      else e.isImmediatePropagationEnabled = false;//IE 대응
    }
  }
}, true)//이벤트 캡쳐링(Capturing)

//저장 버튼
saveBtn.addEventListener('click', e=>{
  if(!confirm("수정사항을 저장하시겠습니까?")) return;
  const $formTag = document.querySelector('.editForm');
  const bnum = e.target.dataset.bnum;
  const url = "/board/" + bnum + "/edit";
  $formTag.action = url;
  $formTag.submit();
})

//취소 버튼
cancelBtn.addEventListener('click', e=>{
//  if(!confirm("수정을 취소하시겠습니까?")) return;
  const bnum = e.target.dataset.bnum;
  const url = `/board/${bnum}/detail`;
  location.href = url;
});

//첨부파일삭제
const $files = document.querySelector('#accordion-item');
$files?.addEventListener('click', e=>{
  if(e.target.tagName != 'I') return;
  if(!confirm('삭제하시겠습니까?')) return;

  const $i = e.target;
  const url = `/attach/${$i.dataset.fid}`;
  fetch(url,{
    method:'DELETE'
  }).then(res=>res.json())
    .then(res=>{
      if(res.rtcd == '00'){
        //첨부파일 정보 화면에서 제거
        removeAttachFileFromView(e);
      }else{
        console.log(res.rtmsg);
      }
    })
    .catch(err=>console.log(err));
});
function removeAttachFileFromView(e){
    const $parent = document.getElementById('attachFiles');
    const $child = e.target.closest('.attachFile');
    $parent.removeChild($child);
}