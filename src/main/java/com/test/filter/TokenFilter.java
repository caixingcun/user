package com.test.filter;


import com.mysql.jdbc.log.LogUtils;
import com.test.exception.ForbiddenException;
import com.test.util.TextUtil;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TokenFilter implements Filter {

    private JdbcTemplate jdbcTemplate;

    public TokenFilter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String token = req.getHeader("token");//header方式
        System.out.println("token------" + token);
        if (token == null || token.length() <= 0) {
            throw new ForbiddenException("无token授权信息");
        } else {
            String account = TokenUtil.getAccount(token);

            if (account == null) {
                throw new ForbiddenException("无效token");
            }

            if (!TokenUtil.vertify(token)) {
                throw new ForbiddenException("身份过期，请重新登录");
            }

            List<String> query = jdbcTemplate.query(String.format("SELECT token FROM account WHERE account = %s", account), (resultSet, i) -> resultSet.getString("token"));
            if (query.size() == 0||query.size()==0|| TextUtil.isEmpty(query.get(0))) {
                throw new ForbiddenException("身份过期，请重新登录");
            }

        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
