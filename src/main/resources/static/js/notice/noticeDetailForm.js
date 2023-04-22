'use strict';

//textArea => ck_editor 대체
ClassicEditor
      .create( document.querySelector( '#nContent' ), {
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
         toolbar: [],/*상단툴바 제거*/
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
         editor.isReadOnly = true;  //읽기모드적용
      } )
      .catch( error => {
         console.error( error );
      } );

  const $editBtn  = document.getElementById('editBtn');
  const $delBtn   = document.getElementById('delBtn');
  const $listBtn  = document.getElementById('listBtn');

//수정
  $editBtn.addEventListener('click',e=>{
    const noticeId = e.target.closest('form').dataset.nNum;
    const url = `/notices/${noticeId}`;
    location.href = url;
  });

  //삭제
  $delBtn.addEventListener('click', e => {
    if(!confirm('삭제하시겠습니까?')) return;

      const $form =  e.target.closest('form');
      const nNum = $form.dataset.nNum;
      $form.action = `/notices/${nNum}`;
      $form.submit();
  });
