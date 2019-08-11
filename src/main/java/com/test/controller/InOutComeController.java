package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.InOutComeBean;
import com.test.bean.MsgBean;
import com.test.exception.BadRequestException;
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

/**
 * 收支录入查询
 */

@RestController
public class InOutComeController {

    @Resource
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/api/in-out-come", method = RequestMethod.POST)
    @ResponseBody
    public String income(@RequestHeader("token") String token,
                         @RequestParam("money") String money,
                         @RequestParam("reason") String reason,
                         @RequestParam("id") Long i_id,
                         @RequestParam("create_time") String create_time) throws UnsupportedEncodingException {
        // 插入数据
        String account = TokenUtil.getAccount(token);

        if (i_id == 0) {
            //插入
            int update = jdbcTemplate.update(String.format("INSERT in_out_money(account,create_time,reason,money) VALUES(\'%s\',\'%s\',\'%s\',\'%s\')", account, create_time, reason, money));
            if (update == 1) {
                return new Gson().toJson(new MsgBean("提交成功"));
            }else{
                throw new ServiceInnerException("数据库操作失败");
            }
        }else{
            //更新
            int update = jdbcTemplate.update(String.format("UPDATE in_out_money SET money = \'%s\',reason = \'%s\',create_time = \'%s\' WHERE i_id = \'%s\' AND account = \'%s\'", money, reason, create_time, i_id, account));
            if (update == 1) {
                return new Gson().toJson(new MsgBean("更新成功"));
            }else{
                throw new ServiceInnerException("数据库操作失败");
            }

        }
    }

    @RequestMapping(value = "/api/in-out-come", method = RequestMethod.GET)
    @ResponseBody
    public String inoutcome(@RequestHeader("token") String token) throws UnsupportedEncodingException {
        // 插入数据
        String account = TokenUtil.getAccount(token);

        List<InOutComeBean> list = jdbcTemplate.query(String.format("SELECT * FROM in_out_money WHERE account = \'%s\'", account), new RowMapper<InOutComeBean>() {
            @Override
            public InOutComeBean mapRow(ResultSet resultSet, int i) throws SQLException {
                InOutComeBean inOutComeBean = new InOutComeBean();
                inOutComeBean.setCreate_time(resultSet.getString("create_time"));
                inOutComeBean.setReason(resultSet.getString("reason"));
                inOutComeBean.setMoney(resultSet.getDouble("money"));
                inOutComeBean.setI_id(resultSet.getLong("i_id"));
                return inOutComeBean;
            }
        });
        return new Gson().toJson(list);
    }


    @RequestMapping(value = "/api/in-out-come/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestHeader("token") String token,@PathVariable("id") int id) throws UnsupportedEncodingException {
     //删除数据
        String account = TokenUtil.getAccount(token);
        int update = jdbcTemplate.update(String.format("DELETE  FROM in_out_money WHERE i_id = \'%d\' AND account = \'%s\'", id, account));
        if (update == 1) {
            return new Gson().toJson(new MsgBean("删除成功"));
        }
        throw new BadRequestException("删除失败");
    }


}
