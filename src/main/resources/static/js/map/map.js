  // 지도생성시 초기 옵션 주기
  var mapOptions = {
    center: new naver.maps.LatLng(35.5352071674071, 129.31091449029512),
    zoom: 18,
    mapTypeId: naver.maps.MapTypeId.NORMAL
  };

  // 지도 그리기
  var map = new naver.maps.Map('map', mapOptions);

  // 마커생성하기
  var marker = new naver.maps.Marker({
    position: new naver.maps.LatLng(35.5352071674071, 129.31091449029512), //좌표값
    map: map, // 마커 표기할 지도

    // 네이버에서 제공하는 기본마커가 아닌 사용자 정의 마커 정의할때
    icon: {
        content: `<div class='mymarker'></div>`,
        //size: new naver.maps.Size(16, 16),   //마커 사이즈
        anchor: new naver.maps.Point(8, 8), //마커 중심좌표
    },
    //animation: naver.maps.Animation.BOUNCE
  });


  // 마커 클릭시
  naver.maps.Event.addListener(marker, "click", function(e) {
      // 인포창이 지도에 나타나 있으면 인포창 닫기
      if (infowindow.getMap()) {
          infowindow.close();
      // 인포창이 지도에 없으면 인포창 띄우기
      } else {

          infowindow.open(map, marker);
      }
  });


  // // 내위치
  // const $myposition = document.getElementById('myposition');
  // $myposition.addEventListener('click', myposition_f );

  // function myposition_f(e){
  //   console.log("myposition_f실행");

  //   //alert('hi'+e);
  //   if(window.navigator.geolocation){
  //     window.navigator.geolocation.getCurrentPosition(onSuccess, onError);
  //   }else{
  //     window.alert('현재 브라우저는 위치정보를 제고하지 않습니다!');
  //   }
  // }

  function onSuccess(position) {
    console.log("onSuccess 실행");

    const location = new naver.maps.LatLng(position.coords.latitude,
                                        position.coords.longitude);

    map.setCenter(location); // 얻은 좌표를 지도의 중심으로 설정합니다.
    map.setZoom(10); // 지도의 줌 레벨을 변경합니다.

    infowindow.setContent('<div style="padding:20px;">' + 'geolocation.getCurrentPosition() 위치' + '</div>');
    infowindow.open(map, location);

    console.log('Coordinates: ' + location.toString());
  }

  function onError(err) {
    console.log("onError 실행");

    const center = map.getCenter();
    console.warn(`ERROR(${err.code}): ${err.message}`);
    infowindow.setContent(
      `<div style="padding:20px;">
        <h5 style="margin-bottom:5px;color:#f00;">Geolocation failed!</h5>
          latitude: ${center.lat()} <br/>
          longitude: ${center.lng()}
      </div>`
    );

    infowindow.open(map, center);
  }

  //1)검색창 요소 가져오기////////////////////////////
  const $library = document.getElementById('library');
  const $bookstore = document.getElementById('bookstore');
  $library.addEventListener('click',searchKeyword);
  $bookstore.addEventListener('click',searchKeyword);

  //2)검색 키워드 입력////////////////////////////////
  function searchKeyword(e){
    console.log("searchKeyword 실행");

    const tagName = e.target.tagName;
    switch(tagName){

      case 'INPUT':
        if(e.key == 'Enter'){
          const ps = new kakao.maps.services.Places();
          //키워드로 장소를 검색합니다
          ps.keywordSearch($keyword.value, placesSearchCB);
        }
        break;

      case 'BUTTON':
        const ps = new kakao.maps.services.Places();

        //3)검색할 키워드 만들기////////////////////////////
        //지역값 가져오기
        const selectedCity = getCity();
        console.log(selectedCity);//시도
        const selectedState = getState();
        console.log(selectedState);//구군//서점인지 도서관인지
        console.log(e.target.textContent);//도서관 or 서점
        const searchKeyword = selectedCity + " "

         + selectedState + " " + e.target.textContent;
        console.log("검색어"+searchKeyword);

        //키워드로 장소를 검색합니다
        //ps.keywordSearch($keyword.value, placesSearchCB);
        ps.keywordSearch(searchKeyword, placesSearchCB);
        break;
      default:
        console.log('etc..');
    }
  }

  //시도 가져오기
  function getCity(){
    //지역값 가져오기
    const $cities = document.querySelectorAll('#city option');

    const selectedCity = [...$cities].find(ele=>ele.selected).value;
    console.log(selectedCity);//시도

    return selectedCity;
  }

  //구군 가져오기
  function getState(){
    const $states = document.querySelectorAll('#state option');
    const selectedState = [...$states].find(ele=>ele.selected).textContent;
    console.log(selectedState);//구군

    return selectedState;
  }

  //마커생성하기
  //parameter : 위도, 경도
  function makeMarker(lat,lng){
    console.log("makeMarker 실행");

    const marker = new naver.maps.Marker({
      position: new naver.maps.LatLng(lat,lng), //좌표값
      map: map, // 마커 표기할 지도

      // 네이버에서 제공하는 기본마커가 아닌 사용자 정의 마커 정의할때
      icon: {
          content: `<div class='marker'><img src="/img/mapMaker.png"></div>`,
          //size: new naver.maps.Size(16, 16),   //마커 사이즈
          anchor: new naver.maps.Point(8, 8), //마커 중심좌표
      },
      //animation: naver.maps.Animation.BOUNCE
    });
    return marker;
  }


  //인포창 생성하기
  //parameter : {상호명,연락처,부가정보,도로명주소}
  function makeInforWindow( info ){
    console.log("makeInforWindow 실행");

    const infowindow = new naver.maps.InfoWindow({

      content:
        `<div class='info'>
          <div> ${info.place_name}</div>
          <div>연락처 : ${info.phone}</div>
          <div>부가정보 :
            <a href='${info.place_url}' target='_blank'>${info.place_url}</a>
          </div>
          <div>도로명주소<br>${info.road_address_name}</div>
        </div>`
      // anchorSize : new naver.maps.Size(10,10)  //인포 창 크기
    });
    return infowindow;
  }

  const markers=[];
  const infos=[];

//4)실제 검색 시작/////////////////////////////////
//키워드 검색 완료 시 호출되는 콜백함수 입니다
function placesSearchCB (data, status, pagination) {

  console.log("placesSearchCB 실행");

  //console.log("data : "+data);
  //console.log("status : "+status);
  //console.log("pagination : "+pagination);

  //검색전 마크존재시 지도에서 제거
  markers.forEach(marker=>marker.setMap(null));

  //검색전 인포창 지도에서 제거
  infos.forEach(info=>info.close());

  //마커,인포 배열에서 제거
  markers.splice(0,markers.length);
  infos.splice(0,infos.length);

  switch (status) {

    case kakao.maps.services.Status.OK : //검색 결과 있음
        //5)검색 결과//////////////////////////////////////
        console.log(data);
        // const result = data.splice(0,10);  // 검색어에 근접한 5개만 추출
        const result = data;  // 검색어에 근접한 5개만 추출
        console.log(result);
        /////////////////////////////////////////////////



        //기타 설정///////////////////////////////////////
        //지도에 마커 표시, 인포창 생성
        result.forEach(ele => {
          //지역 필터링
          console.log(ele.address_name);
          const addArr = ele.address_name.split(" ");

          if(addArr[0] != getCity() || addArr[1] != getState()) return;
          console.log("통과한 값 : " + addArr[0]);
          console.log("통과한 값 : " + addArr[1]);
          // console.log(addArr[2]);
          // console.log(addArr[3]);
          // console.log("시도"+getCity());
          // console.log("구군"+getState());

          //카테고리 필터링
          console.log(ele.category_name);
          const catArr = ele.category_name.split(">");
          console.log("1 : "+catArr[0]);
          console.log("2 : "+catArr[0].trim());
          console.log("3 : "+catArr[1]);
          console.log("4 : "+catArr[1].trim());

          if(catArr[2].trim() != "도서관" && catArr[2].trim() != "서점") return;
          console.log("통과한 값 : "+catArr[2].trim());


          let marker = makeMarker(ele.y, ele.x)
          markers.push(marker);

          let infowindow = makeInforWindow(
            {
              place_name : ele.place_name,  //상호명
              phone : ele.phone,            //연락처
              place_url : ele.place_url,    //부가정보
              road_address_name : ele.road_address_name //도로명주소
            }
          )
          infos.push(infowindow);
        });

        const lats =[]; //경도
        const lngs =[]; //위도
        //마커 클릭시 인포창 띄우기
        markers.forEach( (marker, idx) => {
          naver.maps.Event.addListener(marker, 'click', e=>{
            //인포창이 지도에 나타나 있으면 인포창 닫기
            if (infos[idx].getMap()) {
              infos[idx].close();
            //인포창이 지도에 없으면 인포창 띄우기
            } else {
              infos[idx].open(map, marker);
            }
          });
          lats.push(marker.position.y);
          lngs.push(marker.position.x);
        });

        //위도,경도 최대 최소 구하기
        const minLat = Math.min.apply(null,lats);
        const minLng = Math.min.apply(null,lngs);
        const maxLat = Math.max.apply(null,lats);
        const maxLng = Math.max.apply(null,lngs);

        //좌표경계구하기
        const target = new naver.maps.LatLngBounds(
          new naver.maps.LatLng(minLat, minLng),
          new naver.maps.LatLng(maxLat, maxLng));

        //지도 중심 좌표 이동
        map.panToBounds(target);
        /////////////////////////////////////////////////
        break;

    case kakao.maps.services.Status.ZERO_RESULT  : //정상적으로 응답 받았으나 검색 결과는 없음
        alert('검색할 지역을 선택해 주세요.');
        break;
    case kakao.maps.services.Status.ERROR   : //서버 응답에 문제가 있는 경우
        alert('서버 응답에 문제가 있는 경우');
        break;
    default:
        alert('기타');
        break;
  }
}


//검색 지역 select
function gugunChange(e){
  console.log("gugunChange실행");

  const state = document.getElementById("state");

  const seoul = ['강남구','강동구','강북구','강서구','관악구','광진구','구로구','금천구','노원구','도봉구','동대문구','동작구','마포구','서대문구','서초구','성동구','성북구','송파구','양천구','영등포구','용산구','은평구','종로구','중구','중랑구'];
  const busan = ['강서구','금정구','기장군','남구','동구','동래구','부산진구','북구','사상구','사하구','서구','수영구','연제구','영도구','중구','해운대구'];
  const daegu = ['남구','달서구','달성군','동구','북구','서구','수성구','중구'];
  const incheon = ['강화군','계양구','남동구','동구','부평구','서구','연수구','옹진군','중구'];
  const gwangju = ['광산구','남구','동구','북구','서구'];
  const daejeon = ['대덕구','동구','서구','유성구','중구'];
  const ulsan = ['남구','동구','북구','울주군','중구'];
  const gyeonggi = ['가평군','고양시','과천시','광명시','광주시','구리시','군포시','김포시','남양주시','동두천시','부천시','성남시','수원시','시흥시','안산시','안성시','안양시','양주시','양평군','여주시','연천군','오산시','용인시','의왕시','의정부시','이천시','파주시','평택시','포천시','하남시','화성시'];
  const gangwon = ['강릉시','고성군','동해시','삼척시','속초시','양구군','양양군','영월군','원주시','인제군','정선군','철원군','춘천시','태백시','평창군','홍천군','화천군','횡성군'];
  const chungnam = ['계룡시','공주시','금산군','논산시','당진시','보령시','부여군','서산시','서천군','아산시','예산군','천안시','청양군','태안군','홍성군'];
  const chungbuk = ['괴산군','단양군','보은군','영동군','옥천군','음성군','제천시','증평군','진천군','청원군','청주시','충주시'];
  const jeonnam = ['강진군','고흥군','곡성군','광양시','구례군','나주시','담양군','목포시','무안군','보성군','순천시','신안군','여수시','영광군','영암군','완도군','장성군','장흥군','진도군','함평군','해남군','화순군'];
  const jeonbuk = ['고창군','군산시','김제시','남원시','무주군','부안군','순창군','완주군','익산시','임실군','장수군','전주시','정읍시','진안군'];
  const gyeongnam = ['거제시','거창군','고성군','김해시','남해군','밀양시','사천시','산청군','양산시','의령군','진주시','창녕군','창원시','통영시','하동군','함안군','함양군','합천군'];
  const gyeongbuk = ['경산시','경주시','고령군','구미시','군위군','김천시','문경시','봉화군','상주시','성주군','안동시','영덕군','영양군','영주시','영천시','예천군','울릉군','울진군','의성군','청도군','청송군','칠곡군','포항시'];
  const jeju = ['서귀포시','제주시'];
  const sejong = ['세종특별자치시'];

  if(e.value =="서울"){
    add = seoul;
  } else if (e.value == "부산") {
    add = busan;
  } else if (e.value == "대구") {
    add = daegu;
  } else if (e.value == "인천") {
    add = incheon;
  } else if (e.value == "광주") {
    add = gwangju;
  } else if (e.value == "대전") {
    add = daejeon;
  } else if (e.value == "울산") {
    add = ulsan;
  } else if (e.value == "경기") {
    add = gyeonggi;
  } else if (e.value == "강원") {
    add = gangwon;
  } else if (e.value == "충남") {
    add = chungnam;
  } else if (e.value == "충북") {
    add = chungbuk;
  } else if (e.value == "전남") {
    add = jeonnam;
  } else if (e.value == "전북") {
    add = jeonbuk;
  } else if (e.value == "경남") {
    add = gyeongnam;
  } else if (e.value == "경북") {
    add = gyeongbuk;
  } else if (e.value == "제주") {
    add = jeju;
  } else if (e.value == "세종특별자치시") {
    add = sejong;
  }

  state.options.length = 1;
  //군/구 갯수;

  for(property in add){
    let opt = document.createElement("option");
    opt.value=add[property];
    opt.innerHTML = add[property];
    state.appendChild(opt);
  }

}
