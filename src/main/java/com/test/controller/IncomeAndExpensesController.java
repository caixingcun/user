package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.MoneyBean;
import com.test.bean.MsgBean;
import com.test.bean.TokenBean;
import com.test.exception.ForbiddenException;
import com.test.exception.ServiceInnerException;
import com.test.util.TextUtil;
import com.test.util.TokenUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 收支录入查询
 */

@RestController
public class IncomeAndExpensesController {

    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/api/income", method = RequestMethod.POST)
    @ResponseBody
    public String income(@RequestHeader("token") String token,
                         @RequestParam("money") String money,
                         @RequestParam("reason") String reason,
                         @RequestParam("create_time") String create_time) throws UnsupportedEncodingException {
        // 插入数据
        String account = TokenUtil.getAccount(token);

        System.out.println(reason+"---reason");
        int update1 = jdbcTemplate.update(String.format("INSERT in_out_money(account,money,reason,create_time,is_in_come)VALUES(%s,%s,\'%s\',\'%s\',%s)", account, money, reason, create_time,"1"));
        if (update1 == 1) {
            return new Gson().toJson(new MsgBean("提交成功"));
        }
        throw new ServiceInnerException("数据库操作失败");

    }

    @RequestMapping(value = "/api/outcome", method = RequestMethod.POST)
    @ResponseBody
    public String outcome(@RequestHeader("token") String token,
                         @RequestParam("money") String money,
                         @RequestParam("reason") String reason,
                         @RequestParam("create_time") String create_time) throws UnsupportedEncodingException {
        // 插入数据
        String account = TokenUtil.getAccount(token);

        int update1 = jdbcTemplate.update(String.format("INSERT in_out_money(account,money,reason,create_time,is_in_come)VALUES(%s,%s,\'%s\',\'%s\')", account, money, reason, create_time, "0"));

        if (update1 == 1) {
            return new Gson().toJson(new MsgBean("提交成功"));
        }
        throw new ServiceInnerException("数据库操作失败");

    }

    @RequestMapping(value = "/api/income", method = RequestMethod.GET)
    @ResponseBody
    public String getIncome(@RequestHeader("token") String token,
                            @RequestParam("a_id") String a_id,
                            @RequestParam("money_limit_down") String money_limit_down,
                            @RequestParam("money_limit_up") String money_limit_up,
                            @RequestParam("reason") String reason,
                            @RequestParam("create_time_dowm") String create_time_down,
                            @RequestParam("create_time_up") String create_time_up) throws UnsupportedEncodingException {

        // 插入数据
        String account = TokenUtil.getAccount(token);

        String sql = "SELECT * FROM  in_out_money WHERE is_in_come = 1 ";
        if (!TextUtil.isEmpty(money_limit_down)) {
            sql += String.format(" WHERE money>100 ", money_limit_down);
        }
        if (!TextUtil.isEmpty(money_limit_up)) {
            sql += String.format("AND money<%s ", money_limit_up);
        }
        if (TextUtil.isEmpty(create_time_down)) {
            sql += String.format("AND create_time >= \'%s\'", create_time_down);
        }
        if (TextUtil.isEmpty(create_time_up)) {
            sql += String.format("AND create_time<=\'%s\' ", create_time_up);
        }
        if (!TextUtil.isEmpty(reason)) {
            sql += String.format("AND reason = \'%s\'", reason);
        }
        sql += ";";

        List<MoneyBean> query = jdbcTemplate.query(sql, (resultSet, i) -> {
            MoneyBean moneyBean = new MoneyBean();
            moneyBean.setCreate_time(resultSet.getString("create_time"));
            moneyBean.setReason(resultSet.getString("reason"));
            moneyBean.setMoney(resultSet.getString("money"));
            return moneyBean;
        });

        return new Gson().toJson(query);

    }

    @RequestMapping(value = "/api/outcome", method = RequestMethod.GET)
    @ResponseBody
    public String getoutcome(@RequestHeader("token") String token,
                            @RequestParam("a_id") String a_id,
                            @RequestParam("money_limit_down") String money_limit_down,
                            @RequestParam("money_limit_up") String money_limit_up,
                            @RequestParam("reason") String reason,
                            @RequestParam("create_time_dowm") String create_time_down,
                            @RequestParam("create_time_up") String create_time_up) throws UnsupportedEncodingException {

        // 插入数据
        String account = TokenUtil.getAccount(token);

        String sql = "SELECT * FROM  in_out_money WHERE is_in_come = 0 ";
        if (!TextUtil.isEmpty(money_limit_down)) {
            sql += String.format(" WHERE money>100 ", money_limit_down);
        }
        if (!TextUtil.isEmpty(money_limit_up)) {
            sql += String.format("AND money<%s ", money_limit_up);
        }
        if (TextUtil.isEmpty(create_time_down)) {
            sql += String.format("AND create_time >= \'%s\'", create_time_down);
        }
        if (TextUtil.isEmpty(create_time_up)) {
            sql += String.format("AND create_time<=\'%s\' ", create_time_up);
        }
        if (!TextUtil.isEmpty(reason)) {
            sql += String.format("AND reason = \'%s\'", reason);
        }
        sql += ";";

        List<MoneyBean> query = jdbcTemplate.query(sql, (resultSet, i) -> {
            MoneyBean moneyBean = new MoneyBean();
            moneyBean.setCreate_time(resultSet.getString("create_time"));
            moneyBean.setReason(resultSet.getString("reason"));
            moneyBean.setMoney(resultSet.getString("money"));
            return moneyBean;
        });

        return new Gson().toJson(query);

    }


    @RequestMapping(value = "/api/income", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteIncome(@RequestHeader("token") String token,
                               @RequestParam("a_id") String a_id) throws UnsupportedEncodingException {

        String account = TokenUtil.getAccount(token);
        String sql = String.format("DELETE FROM in_out_come where a_id = %s AND account = %s WHERE is_in_come = 1 ", a_id,account);
        jdbcTemplate.update(sql);
        return new Gson().toJson(new MsgBean("删除成功"));
    }

    @RequestMapping(value = "/api/outcome", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteOutcome(@RequestHeader("token") String token,
                               @RequestParam("a_id") String a_id) throws UnsupportedEncodingException {

        String account = TokenUtil.getAccount(token);
        String sql = String.format("DELETE FROM in_out_come where a_id = %s AND account = %s WHERE is_in_come = 0", a_id,account);
        jdbcTemplate.update(sql);
        return new Gson().toJson(new MsgBean("删除成功"));
    }
}
