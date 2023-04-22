'use strict';

//  Kakao.init('be3be3d05a9fb8c064843f56f3e5a8e6'); //발급받은 키 중 javascript키를 사용해준다.
  // console.log(Kakao.isInitialized()); // sdk초기화여부판단
  // //카카오로그인
  // function kakaoLogin() {
  //     Kakao.Auth.login({
  //       success: function (response) {
  //         Kakao.API.request({
  //           url: '/v2/user/me',
  //           success: function (response) {
  //             console.log(response)
  //           },
  //           fail: function (error) {
  //             console.log(error)
  //           },
  //         })
  //       },
  //       fail: function (error) {
  //         console.log(error)
  //       },
  //     })
  //   }
  // //카카오로그아웃
  // function kakaoLogout() {
  //     if (Kakao.Auth.getAccessToken()) {
  //       Kakao.API.request({
  //         url: '/v1/user/unlink',
  //         success: function (response) {
  //           console.log(response)
  //         },
  //         fail: function (error) {
  //           console.log(error)
  //         },
  //       })
  //       Kakao.Auth.setAccessToken(undefined)
  //     }
  //   }

  //   //자동로그인
  //  var check = $("input[type='checkbox']");
  //   check.click(function(){
  //     $("p").toggle();
  //   });
    