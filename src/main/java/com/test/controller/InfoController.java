package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.*;
import com.test.exception.ServiceInnerException;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

        List<UserInfoBean> query = jdbcTemplate.query(String.format("SELECT * FROM info WHERE account = %s LIMIT 1", account), (resultSet, i) -> {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setHeader(resultSet.getString("header"));
            userInfoBean.setNickname(resultSet.getString("nickname"));
            userInfoBean.setId(resultSet.getInt("id"));
            return userInfoBean;
        });
        if (query.size() > 0) {
        return new Gson().toJson(query.get(0));
        }else{
            return new Gson().toJson(new UserInfoBean());
        }


    }

    /**
     * 提交用户信息
     *
     * @return {"userName":"cxc","password":"123"}
     */

    @RequestMapping(value = "/api/info", method = RequestMethod.POST)
    @ResponseBody
    public String updateInfo(@RequestHeader("token") String token, @RequestBody UserInfoBean infoBean) throws UnsupportedEncodingException {
        String account = TokenUtil.getAccount(token);

        //插入新数据
        List<UserInfoBean> query = jdbcTemplate.query(String.format("SELECT * FROM info WHERE account = \'%s\'", account), new RowMapper<UserInfoBean>() {
            @Override
            public UserInfoBean mapRow(ResultSet resultSet, int i) throws SQLException {
                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setId(resultSet.getInt("id"));
                userInfoBean.setNickname(resultSet.getString("nickname"));
                userInfoBean.setHeader(resultSet.getString("header"));
                return userInfoBean;
            }
        });

        int update = 0;
        if (query.size() > 0) {
            int id = query.get(0).getId();
            update = jdbcTemplate.update(String.format("UPDATE info SET nickname=\'%s\' , header = \'%s\' WHERE id = \'%s\' AND account = \'%s\'", infoBean.getNickname(), infoBean.getHeader(), id, account));
        }else{
            update = jdbcTemplate.update(String.format("INSERT info(nickname,header,account) VALUES(\'%s\',\'%s\',\'%s\')", infoBean.getNickname(), infoBean.getHeader(), account));

        }

        if (update > 0) {
            return new Gson().toJson(new MsgBean("更新成功"));
        }else{
            throw new ServiceInnerException("数据库更新失败");
        }

    }


}
