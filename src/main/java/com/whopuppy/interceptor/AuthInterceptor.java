package com.whopuppy.interceptor;

import com.whopuppy.annotation.Auth;
import com.whopuppy.domain.user.User;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.AccessTokenInvalidException;
import com.whopuppy.exception.ForbiddenException;
import com.whopuppy.service.UserService;
import com.whopuppy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.whopuppy.enums.ErrorMessage.FORBIDDEN_EXCEPTION;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwt;
    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;


        Auth auth= handlerMethod.getMethod().getDeclaredAnnotation(Auth.class); // 메소드의 어노테이션
        // Auth auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class); // 클래스의 어노테이션

        if ( auth == null){ // auth annotation 이 없다면
            return true;
        }

        else if ( auth != null ) {// auth 어노테이션이 있다면
            String accessToken = request.getHeader("Authorization");

            int result = jwt.isValid(accessToken, 0); // flag 0 -> access / 1 refresh

            if (result == 0) { // access token이며 valid 하다면
                if (auth.role() == Auth.Role.NORMAL || auth.role() == null) // 일반유저 용이면
                    return true; // return

                else if (auth.role() == Auth.Role.ROOT) {
                    User user = userService.getMe();
                    if (user.getRole() != null && user.getRole().equals(auth.role().toString()))  // 로그인한 유저가 root 관리자 라면
                        return true;
                }
                else if (auth.role() == Auth.Role.MANAGER) { // 매니저 용 이라면
                    User user = userService.getMe();
                    // 해당 유저가 매니저 유저 혹은 루트유저가 맞는가?
                    if (user.getRole() != null && user.getRole().equals(auth.role().toString()) ) {
                        // 매니저 유저이고
                        if ( auth.authority().toString().equals(Auth.Authority.NONE.toString())){
                            return true; // 필요한 따로 권한이 없는 경우
                        }

                        //매니저 유저이고
                        for (int i = 0; i < user.getAuthority().size(); i++) {
                            if (user.getAuthority().get(i).getAuthority().equals(auth.authority().toString())) {
                                return true; // 권한이 있다면 true
                            }
                        }
                    }
                    // 해당 유저가 매니저 유저는 아니지만 루트 유저라면
                    else if ( user.getRole() != null && user.getRole().equals(Auth.Role.ROOT.toString()) )
                        return true; // true
                }
                throw new ForbiddenException(ErrorMessage.FORBIDDEN_EXCEPTION);
            }else // valid 하지만 access token이 아닌 경우
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
        }
        return true;
    }
}
