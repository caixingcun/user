package com.test.db;

import com.test.bean.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserManager {



    public static List<UserInfoBean> queryInfo(JdbcTemplate jdbcTemplate, String  userName) {
        String sql = String.format("SELECT * FROM info where account = \"%s\"", userName);
        List<UserInfoBean> userInfoBeans = jdbcTemplate.query(sql, new RowMapper<UserInfoBean>() {
            public UserInfoBean mapRow(ResultSet resultSet, int i) throws SQLException {

                UserInfoBean userInfoBean = new UserInfoBean();
                userInfoBean.setAge(resultSet.getInt("age"));
                userInfoBean.setGender(resultSet.getInt("gender"));
                userInfoBean.setHeader(resultSet.getString("header"));
                userInfoBean.setName(resultSet.getString("name"));
                return userInfoBean;
            }
        });
        String sqlInsert = String.format("INSERT INTO info(name,age,header,gender,account)VALUES(\"\",0,\"\",-1,\"%s\");", userName);
        if (userInfoBeans.size() <= 0) {
            jdbcTemplate.update(sqlInsert);
        }
        return userInfoBeans;
    }

    public static int updateInfoName(JdbcTemplate jdbcTemplate,String userName,String name) {
        ;
        String sql = String.format("UPDATE info SET name = \"%s\" WHERE account = \"%s\"", name, userName);
        int update = jdbcTemplate.update(sql);
        return update;
    }


    public static int updateInfoAge(JdbcTemplate jdbcTemplate,String userName,String age) {
        String sql = String.format("UPDATE info SET age = %d WHERE account = \"%s\"", age, userName);
        int update = jdbcTemplate.update(sql);
        return update;
    }

    public static int updateInfoGender(JdbcTemplate jdbcTemplate,String userName,int gender) {
        String sql = String.format("UPDATE info SET gender = %d WHERE account = \"%s\"", gender, userName);
        int update = jdbcTemplate.update(sql);
        return update;
    }

    public static int updateInfoHeader(JdbcTemplate jdbcTemplate,String userName,String header) {
        String sql = String.format("UPDATE info SET header = \"%s\" WHERE account = \"%s\"", header, userName);
        int update = jdbcTemplate.update(sql);
        return update;
    }
}
