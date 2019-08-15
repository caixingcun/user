package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.MsgBean;
import com.test.bean.TemplaterBean;
import com.test.exception.BadRequestException;
import com.test.global.Constants;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 长投温度相关
 */
@RestController
public class InvestTemperatureController {
    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 保存/更新 新数据
     *
     * @param token
     * @param index_type
     * @param temperature
     * @return
     */
    @RequestMapping(value = "/api/temperature", method = RequestMethod.POST)
    @ResponseBody
    public String template(@RequestHeader("token") String token,
                           @RequestParam("create_time") String create_time,
                           @RequestParam("index_type") String index_type,
                           @RequestParam("temperature") String temperature,
                           @RequestParam("code_in") String code_in,
                           @RequestParam("code_out") String code_out) {
        // 插入数据

        String account = TokenUtil.getAccount(token);
        List<Integer> admin_list = jdbcTemplate.query(String.format("SELECT * FROM account WHERE account = \'%s\'", account), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                int is_admin = resultSet.getInt("is_admin");
                return is_admin;
            }
        });
        if (admin_list.size() == 0 || admin_list.get(0) != 1) {
            throw new BadRequestException("当前账户没有管理员操作权限，不能提交数据");
        }

        if (!Arrays.asList(Constants.INDEXS).contains(index_type)) {
            throw new BadRequestException("提交的指数不存在");
        }

        List<TemplaterBean> list = jdbcTemplate.query(String.format("SELECT * FROM invest_temperature WHERE create_time = \'%s\' AND index_type = \'%s\'", create_time, index_type),
                (resultSet, i) -> {
                    TemplaterBean templaterBean = new TemplaterBean();
                    templaterBean.setTemplature(resultSet.getDouble("temperature"));
                    templaterBean.setCode_in(resultSet.getString("code_in"));
                    templaterBean.setCode_out(resultSet.getString("code_out"));
                    templaterBean.setCreate_time(resultSet.getString("create_time"));
                    templaterBean.setIndex_type(resultSet.getString("index_type"));
                    templaterBean.setA_id(resultSet.getInt("a_id"));
                    return templaterBean;
                });

        if (list.size() == 0) {
            //插入sql
            int result = jdbcTemplate.update(String.format("INSERT invest_temperature(create_time,index_type,temperature,code_in,code_out) VALUES(\'%s\',\'%s\',%s,\'%s\',\'%s\')", create_time, index_type, temperature, code_in, code_out));
            if (result == 1) {
                return new Gson().toJson(new MsgBean("保存成功"));
            } else {
                throw new BadRequestException("数据库操作失败");
            }
        } else {
            //更新sql
            long a_id = list.get(0).getA_id();
            int result = jdbcTemplate.update(String.format("UPDATE invest_temperature SET temperature = \'%s\',code_in = \'%s\' ,code_out = \'%s\',index_type = \'%s\',create_time = \'%s\' WHERE a_id = %s", temperature, code_in, code_out, index_type, create_time, a_id));
            if (result == 1) {
                return new Gson().toJson(new MsgBean("更新成功"));
            } else {
                throw new BadRequestException("数据库操作失败");

            }
        }
    }


    /**
     * 提交 某一日 获取所有数据
     *
     * @return
     */
    @RequestMapping(value = "/api/temperatures", method = RequestMethod.POST)
    @ResponseBody
    public String postTemplates(@RequestHeader("token") String token, @RequestBody List<TemplaterBean> beans) {
        // 插入数据
        // 插入数据
        String account = TokenUtil.getAccount(token);

        for (TemplaterBean bean : beans) {
            String index_type = bean.getIndex_type();
            String create_time = bean.getCreate_time();
            double temperature = bean.getTemplature();
            String code_in = bean.getCode_in();
            String code_out = bean.getCode_out();

            List<Integer> admin_list = jdbcTemplate.query(String.format("SELECT * FROM account WHERE account = \'%s\'", account), new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                    int is_admin = resultSet.getInt("is_admin");
                    return is_admin;
                }
            });

            if (admin_list.size() == 0 || admin_list.get(0) != 1) {
                throw new BadRequestException("当前账户没有管理员操作权限，不能提交数据");
            }

            if (!Arrays.asList(Constants.INDEXS).contains(index_type)) {
                throw new BadRequestException("提交的指数不存在");
            }

            List<TemplaterBean> list = jdbcTemplate.query(String.format("SELECT * FROM invest_temperature WHERE create_time = \'%s\' AND index_type = \'%s\'", create_time, index_type),
                    (resultSet, i) -> {
                        TemplaterBean templaterBean = new TemplaterBean();
                        templaterBean.setTemplature(resultSet.getDouble("temperature"));
                        templaterBean.setCode_in(resultSet.getString("code_in"));
                        templaterBean.setCode_out(resultSet.getString("code_out"));
                        templaterBean.setCreate_time(resultSet.getString("create_time"));
                        templaterBean.setIndex_type(resultSet.getString("index_type"));
                        templaterBean.setA_id(resultSet.getInt("a_id"));
                        return templaterBean;
                    });

            if (list.size() == 0) {
                //插入sql
                int result = jdbcTemplate.update(String.format("INSERT invest_temperature(create_time,index_type,temperature,code_in,code_out) VALUES(\'%s\',\'%s\',%s,\'%s\',\'%s\')", create_time, index_type, temperature, code_in, code_out));
                if (result != 1) {
                    throw new BadRequestException("数据库操作失败");
                }
            } else {
                //更新sql
                long a_id = list.get(0).getA_id();
                int result = jdbcTemplate.update(String.format("UPDATE invest_temperature SET temperature = \'%s\',code_in = \'%s\' ,code_out = \'%s\',index_type = \'%s\',create_time = \'%s\' WHERE a_id = %s", temperature, code_in, code_out, index_type, create_time, a_id));
                if (result != 1) {
                    throw new BadRequestException("数据库操作失败");
                }
            }
        }
        return new Gson().toJson(new MsgBean("提交成功"));
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    @RequestMapping(value = "/api/temperatures", method = RequestMethod.GET)
    @ResponseBody
    public String templates() {
        // 插入数据

        List<TemplaterBean> list = jdbcTemplate.query(String.format("SELECT * FROM invest_temperature"), (resultSet, i) -> {
            TemplaterBean templaterBean = new TemplaterBean();
            templaterBean.setTemplature(resultSet.getDouble("temperature"));
            templaterBean.setCode_in(resultSet.getString("code_in"));
            templaterBean.setCode_out(resultSet.getString("code_out"));
            templaterBean.setCreate_time(resultSet.getString("create_time"));
            templaterBean.setIndex_type(resultSet.getString("index_type"));
            templaterBean.setA_id(resultSet.getInt("a_id"));
            return templaterBean;
        });
        return new Gson().toJson(list);
    }


    /**
     * 获取所有数据
     *
     * @return
     */
    @RequestMapping(value = "/api/temperature/{create_time}", method = RequestMethod.GET)
    @ResponseBody
    public String getTemperature(@PathVariable("create_time") int create_time) {
        // 插入数据

        List<TemplaterBean> list = jdbcTemplate.query(String.format("SELECT * FROM invest_temperature WHERE create_time = \'%s\'", create_time), (resultSet, i) -> {
            TemplaterBean templaterBean = new TemplaterBean();
            templaterBean.setTemplature(resultSet.getDouble("temperature"));
            templaterBean.setCode_in(resultSet.getString("code_in"));
            templaterBean.setCode_out(resultSet.getString("code_out"));
            templaterBean.setCreate_time(resultSet.getString("create_time"));
            templaterBean.setIndex_type(resultSet.getString("index_type"));
            templaterBean.setA_id(resultSet.getInt("a_id"));
            return templaterBean;
        });
        return new Gson().toJson(list);
    }

    /**
     * 返回所有指数类型
     *
     * @return
     */
    @RequestMapping(value = "/api/indexs", method = RequestMethod.GET)
    @ResponseBody
    public String indexs() {
        List<String> list = Arrays.asList(Constants.INDEXS);
        return new Gson().toJson(list);
    }


}
