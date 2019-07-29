package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.MsgBean;
import com.test.bean.NoteBean;
import com.test.exception.BadRequestException;
import com.test.exception.ServiceInnerException;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class NoteController {
    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 保存 新数据
     *
     * @param token
     * @param content
     * @param time
     * @return
     */
    @RequestMapping(value = "/api/note", method = RequestMethod.POST)
    @ResponseBody
    public String note(@RequestHeader("token") String token,
                       @RequestParam("id") int id,
                       @RequestParam("content") String content,
                       @RequestParam("time") String time) {
        // 插入数据

        String account = TokenUtil.getAccount(token);

        int update1 = 0;
        if (id == 0) {
            update1 = jdbcTemplate.update(String.format("INSERT note(account,content,time)VALUES(%s,\'%s\',\'%s\')", account, content, time));
        }else{
            update1 = jdbcTemplate.update(String.format("UPDATE note SET content = \'%s\',time = \'%s\' WHERE account = %s AND n_id = %d"), content, time, account, id);
        }
        if (update1 == 1) {
            return new Gson().toJson(new MsgBean("保存成功"));
        }
        throw new ServiceInnerException("数据库操作失败");

    }

    /**
     * 获取所有数据
     *
     * @param token
     * @return
     */
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

    /**
     * 获取单条数据
     *
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/note/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String note(@RequestHeader("token") String token, @PathVariable("id") int id) {
        // 插入数据
        String account = TokenUtil.getAccount(token);
        List<NoteBean> notes = jdbcTemplate.query(String.format("SELECT * FROM note WHERE n_id = %d AND account = %s", id, account), (resultSet, i) -> {
            String content = resultSet.getString("content");
            String time = resultSet.getString("time");
            int n_id = resultSet.getInt("n_id");
            NoteBean noteBean = new NoteBean();
            noteBean.setContent(content);
            noteBean.setTime(time);
            noteBean.setId(n_id);
            return noteBean;
        });

        if (notes.size() <= 0) {
            throw new BadRequestException("查询不到数据");
        }
        return new Gson().toJson(notes.get(0));
    }

    /**
     * 删除单条数据
     *
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/note/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteNote(@RequestHeader("token") String token, @PathVariable("id") int id) {
        // 插入数据
        String account = TokenUtil.getAccount(token);
        int update = jdbcTemplate.update(String.format("DELETE  FROM note WHERE n_id = %d AND account = %s", id, account));

        if (update == 1) {
            return new Gson().toJson(new MsgBean("删除成功"));
        }
        throw new BadRequestException("删除失败");

    }


}
