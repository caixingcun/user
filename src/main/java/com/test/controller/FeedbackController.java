package com.test.controller;

import com.google.gson.Gson;
import com.test.bean.MsgBean;
import com.test.exception.BadRequestException;
import com.test.exception.ServiceInnerException;
import com.test.util.TokenUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
public class FeedbackController {
    @Resource
    JdbcTemplate jdbcTemplate;

    /**
     * 保存 反馈意见
     *
     * @param content
     * @param phone
     * @return
     */
    @RequestMapping(value = "/api/feedback", method = RequestMethod.POST)
    @ResponseBody
    public String feedback(
            @RequestParam("content") String content,
            @RequestParam("phone") String phone) {
        // 插入数据
        List<Long> list = jdbcTemplate.query(String.format("SELECT  * FROM feedback WHERE mobile = '%s' AND content = '%s'", phone, content), (RowMapper<Long>) (resultSet, i) ->
                resultSet.getTime("create_time").getTime()
        );
        if (list.size() > 0) {
            for (Long aLong : list) {
                if (new Date().getTime() - aLong > 1000 * 60 * 60) {
                    throw new BadRequestException("当前内容已提交，请勿重复提交");
                }
            }

        }
        int update = jdbcTemplate.update(String.format("INSERT feedback(mobile,content)VALUE('%s','%s')", phone, content));
        if (update > 0) {
            return new Gson().toJson(new MsgBean("保存成功"));
        }
        throw new ServiceInnerException("数据库操作失败");
    }


}
