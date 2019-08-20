package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.*;
import com.test.db.AccountManager;
import com.test.exception.BadRequestException;
import com.test.exception.ServiceInnerException;
import com.test.util.TokenUtil;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
        } else {
            return new Gson().toJson(new UserInfoBean());
        }
    }


    /**
     * 查看是否是管理员
     *
     * @return {"userName":"cxc","password":"123"}
     */

    @RequestMapping(value = "/api/is_admin", method = RequestMethod.GET)
    @ResponseBody
    public String getIsAdmin(@RequestHeader("token") String token) throws UnsupportedEncodingException {
        String account = TokenUtil.getAccount(token);

        List<Boolean> query = jdbcTemplate.query(String.format("SELECT * FROM account WHERE account = %s LIMIT 1", account), (resultSet, i) -> {
            int is_admin = resultSet.getInt("is_admin");
            return is_admin == 1;
        });
        if (query.size() > 0 && query.get(0)) {
            return new Gson().toJson(new MsgBean("当前账户是管理员"));
        } else {
            throw new BadRequestException("当前账户不是管理员");
        }
    }

    /**
     * 提交用户信息
     *
     * @return {"userName":"cxc","password":"123"}
     */

    @RequestMapping(value = "/api/name", method = RequestMethod.POST)
    @ResponseBody
    public String updateInfo(@RequestHeader("token") String token, @RequestParam("nickname") String nickname) throws UnsupportedEncodingException {
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
            update = jdbcTemplate.update(String.format("UPDATE info SET nickname=\'%s\' , header = \'%s\' WHERE id = \'%s\' AND account = \'%s\'", nickname, "", id, account));
        } else {
            update = jdbcTemplate.update(String.format("INSERT info(nickname,header,account) VALUES(\'%s\',\'%s\',\'%s\')", nickname, "", account));
        }

        if (update > 0) {
            return new Gson().toJson(new MsgBean("更新成功"));
        } else {
            throw new ServiceInnerException("数据库更新失败");
        }

    }


    @RequestMapping(value = "/api/header", method = RequestMethod.POST)
    @ResponseBody
    public String uploadHeader(@RequestHeader("token") String token, @RequestParam("file") MultipartFile file) throws IOException {
        String account = TokenUtil.getAccount(token);
        File dirFile = new File(PicController.PICTURE_PATH + "/header/");
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File localFile = new File(PicController.PICTURE_PATH + "/header/" + new Date().getTime() + ".jpg");
        file.transferTo(localFile);
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
            update = jdbcTemplate.update(String.format("UPDATE info SET  header = \'%s\' WHERE id = \'%s\' AND account = \'%s\'", localFile.getName(), id, account));
        } else {
            update = jdbcTemplate.update(String.format("INSERT info(nickname,header,account) VALUES(\'%s\',\'%s\',\'%s\')", "", localFile.getName(), account));
        }

        if (update > 0) {
            return new Gson().toJson(new MsgBean("更新成功"));
        } else {
            throw new ServiceInnerException("数据库更新失败");
        }
    }


}
