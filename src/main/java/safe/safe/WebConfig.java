package safe.safe;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import safe.safe.web.interceptor.AdminCheckInterceptor;
import safe.safe.web.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //cors허용하기위한 글로벌 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")            //요청url
                .allowedOrigins("http://192.168.0.8:5500","http://localhost:5500")    //요청 client
                .allowedMethods("*")                            //모든 메소드
                .maxAge(3000);                                  //캐쉬시간
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .addPathPatterns("/member/**")
                .addPathPatterns("/board/**")
                .addPathPatterns("/notices/**")


                .excludePathPatterns(
                        "/css/**",
                        "/js/**",
                        "/img/**",

                        "/",                          // 메인화면
                        "/login",                     // 로그인
                        "/logout",                    // 로그아웃
                        "/signup",                    // 회원가입
                        "/signupSuccess",             // 회원가입 완료
                        "/member/outCompleted",       // 회원탈퇴 완료
                        "/find/**",                   // id 및 pw 찾기 관련 일체

                        "/memberexist/**",            // id 및 pw 찾기 관련 일체

                        "/restart",                   //홈화면 복귀

                        "/board",
                        "/board/*",                   // 게시판 목록 보기
                        "/board/*/TC/*",              // 게시판 제목+내용 검색
                        "/board/*/T/*",               // 게시판 제목 검색
                        "/board/*/C/*",               // 게시판 내용 검색
                        "/board/*/N/*",               // 게시판 닉네임 검색
                        "/board/*/detail",            // 게시판 내용 보기
                        "/api/comment/*",             // 게시판 댓글 보기
                        "/notices/all",               // 공지사항 목록 보기
                        "/error/**",
                    "/weather",
            "/getWeather.do",
            "/weatherStep.do",
                        "/emergency/EA",
                    "/emergency/EB",
                    "/emergency/EC",
                    "/emergency/ED",
                        "/emergencymedicallist",
                        "/emergency-rooms",
            "/api/emergency-rooms",
                    "/emergency-rooms/api",
                        "/map"                        //지도 검색

                );


        registry.addInterceptor(new AdminCheckInterceptor())
                .order(2)
                .addPathPatterns("/admin/**")       // 관리자 전용 화면
                .excludePathPatterns(
                );
    }
}