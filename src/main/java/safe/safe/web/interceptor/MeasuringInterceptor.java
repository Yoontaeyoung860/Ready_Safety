package safe.safe.web.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

// 로그를 사용하기 위한 어노테이션
@Slf4j
public class MeasuringInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    // 컨트롤러가 호출되기 전에 실행되는 메서드
    // 반환값이 true이면 다음 인터셉터 혹은 컨트롤러를 실행하고,
    // 반환값이 false이면 컨트롤러 실행을 중지합니다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("MeasuringInterceptor.preHandle");

        // 요청의 URI를 가져옵니다.
        String requestURI = request.getRequestURI();
        // 고유한 식별자를 생성합니다.
        String uuid = UUID.randomUUID().toString();

        // 요청에 logId와 현재 시간을 저장합니다.
        request.setAttribute(LOG_ID, uuid);
        request.setAttribute("beginTime", System.currentTimeMillis());

        // handler가 HandlerMethod 인스턴스인 경우
        if(handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod)handler;  // 호출할 컨트롤러 메서드의 모든 정보를 갖고 있습니다.
        }

        // 로그를 출력합니다.
        log.info("Request [{}][{}][{}]", uuid, requestURI, handler);
        return true;
    }

    // 컨트롤러가 실행된 후 호출되는 메서드
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("MeasuringInterceptor.postHandle");

        String uuid = (String) request.getAttribute(LOG_ID);
        log.info("Request [{}][{}][{}]", uuid, modelAndView);
    }

    // 뷰가 렌더링되고 클라이언트에 응답한 후 호출되는 메서드
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("MeasuringInterceptor.afterCompletion");

        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);

        // 요청 시작 시간과 현재 시간을 가져와 실행 시간을 계산합니다.
        long beginTime = (long)request.getAttribute("beginTime");
        long endTime = System.currentTimeMillis();

        // 로그를 출력합니다.
        log.info("Response [{}][{}][실행시간:{}][{}]", uuid, requestURI, (endTime - beginTime), handler);

        // 예외가 발생한 경우 에러 로그를 출력합니다.
        if(ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}