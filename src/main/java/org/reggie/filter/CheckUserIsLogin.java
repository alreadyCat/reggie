package org.reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.reggie.common.BaseContext;
import org.reggie.common.Res;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebFilter(filterName = "checkUserIsLogin", urlPatterns = "/*")
public class CheckUserIsLogin implements Filter {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求{}", request.getRequestURI());

//        在白名单中

//        访问非白名单，且未登录 或 session 过期
        if (!isIncludeInWhiteList(request.getRequestURI()) && request.getSession().getAttribute("employee") == null) {
            log.info("用户未登录{}，{}", request.getRequestURI(), request.getSession().getAttribute("employee"));
            response.getWriter().write(JSON.toJSONString(Res.error("NOTLOGIN")));
        } else {
            log.info("用户已登录{}，{}", request.getRequestURI(), request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentUserId(empId);
            filterChain.doFilter(request, response);
        }


    }

    private boolean isIncludeInWhiteList(String reqUrl) {
        String[] whiteList = {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};

        boolean matched = false;
        for (String url : whiteList) {
            matched = pathMatcher.match(url, reqUrl);
            if (matched) {
                return matched;
            }
        }
        return matched;
    }
}
