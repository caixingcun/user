package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.*;
import com.test.exception.BadRequestException;
import com.test.exception.ForbiddenException;
import com.test.exception.ServiceInnerException;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class LoginController {

    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 注册
     *
     * @param  account  账号
     * @param  password  密码
     * @return {"userName":"cxc","password":"123"}
     */

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("account") String account, @RequestParam("password") String password) throws UnsupportedEncodingException {

        // 查询用户是否存在
        // 插入数据

        List<Object> query = jdbcTemplate.query(String.format("SELECT * FROM account where account = %s limit 1", account), (resultSet, i) -> "");
        if (query.size() == 1) {
            throw new BadRequestException("当前账号已存在");
        }

        String token = TokenUtil.generalToken(account);

        int update1 = jdbcTemplate.update(String.format("INSERT account(account,pwd,token)VALUES(%s,%s,\'%s\')", account, password, token));
        if (update1 == 1) {
            return new Gson().toJson(new TokenBean(account, token));
        }
        throw new ServiceInnerException("数据库操作失败");

    }

    /**
     * 登录
     *@param  account
     *@param password
     * @return {"userName":"cxc","token":"123"}
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("account")String account ,@RequestParam("password")String password) throws UnsupportedEncodingException {


        List<String> query = jdbcTemplate.query(String.format("SELECT pwd FROM account WHERE account = %s LIMIT 1",account), (resultSet, i) -> resultSet.getString("pwd"));

        if (query.size() == 1) {
            if (!query.get(0).trim().equals(password.trim())) {
                throw new BadRequestException("用户名或密码错误");
            }
            String token = TokenUtil.generalToken(account);
            int update = jdbcTemplate.update(String.format("UPDATE account SET token = \'%s\'  WHERE account = %s", token,account ));
            if (update == 1) {
                return new Gson().toJson(new TokenBean(account, token));
            }
            throw new ServiceInnerException("sql操作失败");
        }
        throw new BadRequestException("当前账户不存在");

    }

    /**
     * 登录
     *@param  token
     * @return {"userName":"cxc","token":"123"}
     */
    @RequestMapping(value = "/api/logout", method = RequestMethod.GET)
    @ResponseBody
    public String logout(@RequestHeader("token")String token ) throws UnsupportedEncodingException {
        String account = TokenUtil.getAccount(token);

        List<String> query = jdbcTemplate.query(String.format("SELECT pwd FROM account WHERE account = %s LIMIT 1",account), (resultSet, i) -> resultSet.getString("pwd"));

        if (query.size() == 1) {
            int update = jdbcTemplate.update(String.format("UPDATE account SET token = Null  WHERE account = %s",account ));
            if (update == 1) {
                return new Gson().toJson(new MsgBean("登出成功"));
            }
            throw new ServiceInnerException("sql操作失败");
        }
        throw new BadRequestException("当前账户不存在");

    }

    /**
     * 修改密码
     *
     * @param
     * @return {"msg":"123"}
     */

    @RequestMapping(value = "/user/changepwd", method = RequestMethod.POST)
    @ResponseBody
    public String forgetPwd(@RequestParam("account")String account,@RequestParam("password")String password,@RequestParam("new_password")String newPassword) {

        List<Object> query = jdbcTemplate.query(String.format("SELECT * FROM account WHERE account=%s AND pwd = %s", account, password), (resultSet, i) -> "");
        if (query.size() != 1) {
            throw new BadRequestException("账号名或密码错误");
        }

        int update = jdbcTemplate.update(String.format("UPDATE account SET pwd = %s WHERE account = %s AND pwd = %s", newPassword, account, password));

        if (update == 1) {
            MsgBean msgBean = new MsgBean();
            msgBean.setMsg("更新成功");
            return new Gson().toJson(msgBean);
        }

        throw new ServiceInnerException("sql更新异常");
    }




}
