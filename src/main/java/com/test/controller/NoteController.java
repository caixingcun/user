package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.MsgBean;
import com.test.bean.NoteBean;
import com.test.exception.ServiceInnerException;
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

@RestController
public class NoteController {
    @Resource
    JdbcTemplate jdbcTemplate;
    @RequestMapping(value = "/api/note", method = RequestMethod.POST)
    @ResponseBody
    public String note(@RequestHeader("token") String token,
                         @RequestParam("content") String content,
                         @RequestParam("time") String time)  {
        // 插入数据
        String account = TokenUtil.getAccount(token);

        int update1 = jdbcTemplate.update(String.format("INSERT note(account,content,time)VALUES(%s,\'%s\',\'%s\')", account, content, time));
        if (update1 == 1) {
            return new Gson().toJson(new MsgBean("保存成功"));
        }
        throw new ServiceInnerException("数据库操作失败");

    }

    @RequestMapping(value = "/api/notes", method = RequestMethod.GET)
    @ResponseBody
    public String notes(@RequestHeader("token") String token) {
        // 插入数据
        String account = TokenUtil.getAccount(token);
        List<NoteBean> notes = jdbcTemplate.query(String.format("SELECT * FROM note WHERE account = %s", account), (resultSet, i) -> {
            String content = resultSet.getString("content");
            String time = resultSet.getString("time");
            int n_id = resultSet.getInt("n_id");
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setTime(time);
            noteBean.setId(n_id);
            return noteBean;
        });

        return new Gson().toJson(notes);
    }

    @RequestMapping(value = "/api/note", method = RequestMethod.GET)
    @ResponseBody
    public String note(@RequestHeader("token") String token,@RequestParam("id")String id) {
        // 插入数据
        String account = TokenUtil.getAccount(token);
        List<NoteBean> notes = jdbcTemplate.query(String.format("SELECT * FROM note WHERE n_id = %s AND account = %s",id, account), (resultSet, i) -> {
            String content = resultSet.getString("content");
            String time = resultSet.getString("time");
            int n_id = resultSet.getInt("n_id");
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setTime(time);
            noteBean.setId(n_id);
            return noteBean;
        });

        return new Gson().toJson(notes);
    }



}
