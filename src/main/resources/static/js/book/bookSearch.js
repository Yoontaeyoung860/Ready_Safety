'use strict';
//상세화면 가기
const $infoClick = document.querySelector('.search-result');
$infoClick.addEventListener('click', e => {
    if (e.target.tagName != 'IMG') return;

    const inputWord = document.querySelector('.search-word');
    const inputIsbn = document.querySelector('.search-isbn');
    const inputTitle = document.querySelector('.search-title');
    const inputContents = document.querySelector('.search-contents');
    const inputAuthors = document.querySelector('.search-authors');
    const inputPublisher = document.querySelector('.search-publisher');
    const inputTranslators = document.querySelector('.search-translators');
    const inputThumbnail = document.querySelector('.search-thumbnail');
    const inputPublisher_dt = document.querySelector('.search-publisher_dt');

    const msg = event.target.closest('.img-list');

    console.log(msg);
    console.log(document.querySelector('.word').value);

    inputWord.value = document.querySelector('.word').value;
    inputIsbn.value = msg.dataset.isbn;
    inputTitle.value = msg.children[1].children[1].children[0].textContent;
    inputAuthors.value = msg.children[1].children[2].textContent;
    inputPublisher.value = msg.children[1].children[5].textContent;
    inputTranslators.value = msg.children[1].children[6].textContent;
    inputThumbnail.value = msg.children[0].attributes[0].value;
    inputPublisher_dt.value = msg.children[1].children[4].textContent
    inputContents.value = msg.children[1].children[3].attributes[1].textContent;
    const $infoForm = document.querySelector('.search-form')
    const url = `/book/info`;
    $infoForm.action = url;
    $infoForm.submit();
});

searchBtn.addEventListener('click', event => {
    let i = 0;
    let j = 0;
    $('div').remove('.img-list');
    $.ajax({
        method: "GET",
        url: "https://dapi.kakao.com/v3/search/book?target=title",
        data: { query: $(".word").val(), size: "50" },
        headers: { Authorization: "KakaoAK f75e6a117839fe46761b0b28cf75a7d1" }
    })
        .done(function (msg) {

            let authors;
            let translators;
            for (i = 0; i < msg.documents.length; i++) {
                authors = msg.documents[i].authors;
                authors = undifinded_remove_f(authors);
                translators = msg.documents[i].translators;
                translators = undifinded_remove_f(translators);
                $(".search-result").append('<div class="img-list" id="imgList" data-isbn="' + msg.documents[i].isbn + '" data-contents="' + msg.documents[i].contents + '"><img src="' + msg.documents[i].thumbnail + '"><div class="content-list" id="contentList"><input type="hidden" value="' + msg.documents[i].isbn + '"></input><p><strong>' + msg.documents[i].title + '</strong></p><p>' + authors + '</p><input type="hidden" value="' + msg.documents[i].contents + '"></input><p>' + msg.documents[i].datetime.slice(0, 10) + '</p><p>' + msg.documents[i].publisher + '</p><p>' + translators + '</p></div></div>');
                //            $(".search-result").append('<div class="imgList" id="imgList" onclick="imgClick(event);" data-isbn="' + msg.documents[i].isbn + '" data-contents="' + msg.documents[i].contents + '"><img src="' + msg.documents[i].thumbnail + '"><div class="contentlist"><input type="hidden" value="' + msg.documents[i].isbn + '"></input><p><strong>' + msg.documents[i].title + '</strong></p><p>' + authors + '</p><input type="hidden" value="' + msg.documents[i].contents + '"></input><p>' + msg.documents[i].datetime.slice(0, 10) + '</p><p>' + msg.documents[i].publisher + '</p><p>' + translators + '</p></div></div>');
            }
            $(".word").val = null;
        });
});


//이전 검색어 존재유무
const keyword = document.getElementById('search-result').dataset.keyword;
if (keyword && keyword.length > 0) {
    word.value = keyword;
    searchBtn.click();
}

function undifinded_remove_f(obj) {
    let i = 0;
    let str = '';
    for (i = 0; i < obj.length; i++) {
        if (i != 0) {
            str += ', ';
            str += obj[i];
        } else {
            str += obj[i];
        }
    }
    if (str == '') {
        str = '-';
    }
    return str;
}
const key = $(".word").val();