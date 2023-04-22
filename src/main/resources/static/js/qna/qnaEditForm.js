'use strict';

//textArea => ck_editor 대체
ClassicEditor
      .create( document.querySelector( '#qContent' ), {
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

//상세
const $cancelBtn = document.getElementById('cancelBtn');
const $editForm = document.getElementById('editForm');
$cancelBtn?.addEventListener('click',e=>{
  const url = `/qna/${qNum.value}`;
  location.href = url;
});
//목록
const $listBtn = document.getElementById('listBtn');
$listBtn?.addEventListener('click',e=>{
  location.href="/qna/list";
});

//첨부파일삭제
    const $files = document.querySelector('#attachFiles');
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