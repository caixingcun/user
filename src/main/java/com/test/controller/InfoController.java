package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.*;
import com.test.db.UserManager;
import com.test.exception.ServiceInnerException;
import com.test.util.TextUtil;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
public class InfoController {

    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 获取用户信息
     *
     * @return {"userName":"cxc","password":"123"}
     */

    @RequestMapping(value = "/api/info", method = RequestMethod.GET)
    @ResponseBody
    public String getInfo(@RequestHeader("token") String token) throws UnsupportedEncodingException {
        String account = TokenUtil.getAccount(token);
        List<UserInfoBean> query = jdbcTemplate.query(String.format("SELECT * FROM person_info WHERE account = %s LIMIT 1", account), (resultSet, i) -> {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setName(resultSet.getString("name"));
            userInfoBean.setAge(resultSet.getInt("age"));
            userInfoBean.setHeader(resultSet.getString("header"));
            userInfoBean.setGender(resultSet.getInt("gender"));
            return userInfoBean;
        });

        if (query.size() == 1) {
            return new Gson().toJson(query.get(0));
        }

        String insertNewInfo = String.format("INSERT person_info(account) VALUES(%S)", account);

        int update = jdbcTemplate.update(insertNewInfo);
        if (update == 1) {
            return new Gson().toJson(new UserInfoBean());
        } else {
            throw new ServiceInnerException("未查询到数据");
        }


    }

    /**
     * 提交用户信息
     *
     * @return {"userName":"cxc","password":"123"}
     */

    @RequestMapping(value = "/api/info", method = RequestMethod.POST)
    @ResponseBody
    public String updateInfo(@RequestHeader("token") String token, HttpServletRequest request) throws UnsupportedEncodingException {
        String account = TokenUtil.getAccount(token);

        String name = request.getParameter("name");
        String agender = request.getParameter("agender");
        String header = request.getParameter("header");
        String age = request.getParameter("age");


        List<UserInfoBean> query = jdbcTemplate.query(String.format("SELECT * FROM person_info WHERE account = %s LIMIT 1", account), new RowMapper<UserInfoBean>() {
            @Override
            public UserInfoBean mapRow(ResultSet resultSet, int i) throws SQLException {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setName(resultSet.getString("name"));
                userInfoBean.setAge(resultSet.getInt("age"));
                userInfoBean.setHeader(resultSet.getString("header"));
                userInfoBean.setGender(resultSet.getInt("gender"));
                return userInfoBean;
            }
        });

        if (query.size() == 0) {
            UserInfoBean userInfoBean = new UserInfoBean();
            if (!TextUtil.isEmpty(name)) {
                userInfoBean.setName(name);
            }
            if (!TextUtil.isEmpty(agender)) {
                userInfoBean.setGender(Integer.valueOf(agender));
            }
            if (!TextUtil.isEmpty(age)) {
                userInfoBean.setGender(Integer.valueOf(age));
            }
            if (!TextUtil.isEmpty(header)) {
                userInfoBean.setHeader(header);
            }
            String insertNewInfo = String.format("INSERT person_info(account,name,gender,header,age) VALUES(\'%s\',\'%s\',%d,\'%s\',%d)", account, userInfoBean.getName(), userInfoBean.getGender(), userInfoBean.getHeader(), userInfoBean.getAge());
            int update = jdbcTemplate.update(insertNewInfo);
            if (update == 1) {
                return new Gson().toJson(userInfoBean);
            }
            throw new ServiceInnerException("更新异常");
        }

        if (query.size() == 1) {
            //更新
            UserInfoBean userInfoBean1 = query.get(0);
            if (!TextUtil.isEmpty(name)) {
                userInfoBean1.setName(name);
            }
            if (!TextUtil.isEmpty(agender)) {
                userInfoBean1.setGender(Integer.valueOf(agender));
            }
            if (!TextUtil.isEmpty(age)) {
                userInfoBean1.setGender(Integer.valueOf(age));
            }
            if (!TextUtil.isEmpty(header)) {
                userInfoBean1.setHeader(header);
            }

            int update = jdbcTemplate.update(String.format("UPDATE person_info SET `name`=\"%s\",age=%d ,gender =%d,header=\"%s\" WHERE account = \"%s\" LIMIT 1 \n",
                    userInfoBean1.getName(), userInfoBean1.getAge(), userInfoBean1.getGender(), userInfoBean1.getHeader(), account));
            if (update == 1) {
                return new Gson().toJson(userInfoBean1);
            }

        }

        throw new ServiceInnerException("数据异常");
    }


}
