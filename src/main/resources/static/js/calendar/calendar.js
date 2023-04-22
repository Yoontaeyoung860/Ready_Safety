'use strict';
// Date 객체 생성
let date = new Date();

const renderCalendar = () => {
  const viewYear = date.getFullYear();
  const viewMonth = date.getMonth();

  // year-month 채우기
  document.querySelector('.year-month').textContent = `${viewYear}년 ${viewMonth + 1}월`;

  // 지난 달 마지막 Date, 이번 달 마지막 Date
  const prevLast = new Date(viewYear, viewMonth, 0);
  const thisLast = new Date(viewYear, viewMonth + 1, 0);

  const PLDate = prevLast.getDate();
  const PLDay = prevLast.getDay();

  const TLDate = thisLast.getDate();
  const TLDay = thisLast.getDay();

  // Dates 기본 배열들
  const prevDates = [];
  const thisDates = [...Array(TLDate + 1).keys()].slice(1);
  const nextDates = [];

  //년도
//  console.log(viewYear);
  //이번달
//  console.log(viewMonth+1)
  //첫날 날짜
//  console.log(thisDates.indexOf(1)+1)
  //마지막 날짜
//  console.log(thisDates.lastIndexOf(TLDate)+1)

//    const startDate = viewYear +"/"+ (viewMonth+1) +"/"+ (thisDates.indexOf(1)+1);
////    console.log(startDate);
//    const endDate = viewYear +"/"+ (viewMonth+1) +"/"+ (thisDates.lastIndexOf(TLDate)+1);
////    console.log(endDate);
//
//    const calendarDTO = {};
//    calendarDTO.startDate = startDate;
//    calendarDTO.endDate = endDate;
//
//
//
//      fetch('http://localhost:9080/api/calendar',{
//        method:'POST',
//        headers: {
//          'Content-Type': 'application/json',
//        },
//        body: JSON.stringify(calendarDTO)  // js객체를 => json 포맷 문자열 변환
//      })
//      .then(res=>res.json())
//      .then(res=>{
//        console.log(res);
//
//        res.data.forEach(ele=>{
//
//            let item = document.querySelectorAll('.date .this');
//
//            for(var i=0; i<item.length; i++){
//                if(item[i].className.trim() == 'other') return;
//                console.log(i+"는"+item[i].className.trim()+"임");
//                console.log("json날짜"+ele.ddate);
//
//            }
//        })
//      })
//      .catch(err => console.error('Err:',err));



  // prevDates 계산
  if (PLDay !== 6) {
    for (let i = 0; i < PLDay + 1; i++) {
      prevDates.unshift(PLDate - i);
    }
  }

  // nextDates 계산
  for (let i = 1; i < 7 - TLDay; i++) {
    nextDates.push(i)
  }

  // Dates 합치기
  const dates = prevDates.concat(thisDates, nextDates);

  // Dates 정리
  const firstDateIndex = dates.indexOf(1);
  // console.log("첫날 인덱스:"+firstDateIndex);//첫날 인덱스
  const lastDateIndex = dates.lastIndexOf(TLDate);
  // console.log("마지막날 인덱스:"+lastDateIndex);//마지막날 인덱스
  dates.forEach((date, i) => {
//    console.log("date:"+date);
//    console.log("i:"+i);
    const condition = i >= firstDateIndex && i < lastDateIndex + 1
                      ? 'this'
                      : 'other';

    if(condition == 'this'){
//      console.log(i+"는 this");
      dates[i] = `<div class="date thisM d${date}"><span class="${condition}">${date}</span></div>`;
    }else if(condition == 'other'){
//      console.log(i+"는 other");
      dates[i] = `<div class="date otherM d${date}"><span class="${condition}">${date}</span></div>`;
    }
  })

  // Dates 그리기
  document.querySelector('.dates').innerHTML = dates.join('');

    // 오늘 날짜 그리기
    const today = new Date();
    if (viewMonth === today.getMonth() && viewYear === today.getFullYear()) {
      for (let date of document.querySelectorAll('.this')) {
        if (+date.innerText === today.getDate()) {
          date.classList.add('today');
          break;
        }
      }
    }


    const startDate = viewYear +"/"+ (viewMonth+1) +"/"+ (thisDates.indexOf(1)+1);
//    console.log(startDate);
    const endDate = viewYear +"/"+ (viewMonth+1) +"/"+ (thisDates.lastIndexOf(TLDate)+1);
//    console.log(endDate);

    const calendarDTO = {};
    calendarDTO.startDate = startDate;
    calendarDTO.endDate = endDate;

  // json 불러오기
    selectImg(calendarDTO);
}

renderCalendar();

const prevMonth = () => {
  date.setMonth(date.getMonth() - 1);
  renderCalendar();
}

const nextMonth = () => {
  date.setMonth(date.getMonth() + 1);
  renderCalendar();
}

const goToday = () => {
  date = new Date();
  renderCalendar();
}

function selectImg(calendarDTO){
  fetch('http://localhost:9080/api/calendar',{
    method:'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(calendarDTO)  // js객체를 => json 포맷 문자열 변환
  })
  .then(res=>res.json())
  .then(res=>{
    console.log(res);

    res.data.forEach(ele=>{
        let date = ele.ddate.split('-');
        let targetDate = date[2];
        if(date[2].charAt(0) == '0'){
            targetDate = date[2].substr(1,1);
        }
        console.log("json날짜"+targetDate);
//        console.log(`.date .thisM`);
//        let str = `.date.thisM.d${date[2]}`;
        let str = `.date.thisM.d${targetDate}`;
        console.log("str="+str);
        console.log(""+document.querySelector(str));
        console.log("ele.thumbnail="+ele.thumbnail);
//        let imageUrl = `url("ele.thumbnail")`;
//        console.log(imageUrl);
//        let imageUrl = `url("${ele.thumbnail}")`;
//        console.log(imageUrl);


        document.querySelector(str).style.backgroundImage = `url(${ele.thumbnail})`;
//        let item = document.querySelector(str);
//        item.style.backgroundImage = `url(${imageUrl})`;


//        console.log("결과2"+item);
//                let item3 = document.querySelector(`${str}`);
//        console.log("결과3"+item3);
//                        let item4 = document.querySelector(`.dates .date.thisM.d${date[2]}`);
//                console.log("결과4"+item4);
    })
  })
  .catch(err => console.error('Err:',err));
}