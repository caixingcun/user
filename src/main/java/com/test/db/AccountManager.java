package com.test.db;

import com.test.bean.AccountBean;
import com.test.bean.ForgetPwd;
import com.test.bean.LoginBean;
import com.test.bean.TokenBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountManager {

    public static int insertToken(JdbcTemplate jdbcTemplate,String account, final String token) {
        int tokenState= queryToken(jdbcTemplate,token);

        String newSql = String.format("INSERT INTO token(account,token)VALUES(\"%s\",\"%s\")", account, token, new SimpleDateFormat("yyyyMMdd").format(new Date()));

        if (tokenState == -1) {
            return jdbcTemplate.update(newSql);
        }
        // 0  1
        String updateSql = String.format("UPDATE  token set token = \"%s\" WHERE account = \"%s\"", token, account);
        return jdbcTemplate.update(updateSql);
    }
    public static String createToken(String account) {
        String token = account + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        return token;
    }

    /**
     *
     * @param jdbcTemplate
     * @param token
     * @return   -1 不存在  0  token过期  1 token 一致
     */
    public static int queryToken(JdbcTemplate jdbcTemplate,String token) {

        String[] split = token.split("-");
        String sql = String.format("SELECT * FROM token WHERE account = \"%s\"", split[0]);

        List<TokenBean> tokenBeans = jdbcTemplate.query(sql, new RowMapper<TokenBean>() {
            @Override
            public TokenBean mapRow(ResultSet resultSet, int i) throws SQLException {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setAccount(resultSet.getString("account"));
                tokenBean.setToken(resultSet.getString("token"));
                return tokenBean;
            }
        });
        if (tokenBeans.size() <= 0) {
            return -1; //不存在
        }
        if (!token.equals(tokenBeans.get(0).getToken())) {
            return 0;  //过期了
        }
        return 1;
    }


    public static int upDatePwd(JdbcTemplate jdbcTemplate, ForgetPwd forgetPwd) {
        String sql = String.format("UPDATE  account SET pwd = \"%s\" WHERE account = \"%s\"", forgetPwd.getNewPwd(), forgetPwd.getUserName());
        return jdbcTemplate.update(sql);

    }


    public static int insertAccount(JdbcTemplate jdbcTemplate,LoginBean loginBean) {
        String registerSql =  String.format("INSERT INTO account(account,pwd)VALUES(\"%s\",\"%s\")", loginBean.getUserName(), loginBean.getPassword());
        int update = jdbcTemplate.update(registerSql);
        return update;
    }

    public static List<AccountBean> quertAccounts(JdbcTemplate jdbcTemplate, String  userName) {
        String sql = String.format("SELECT * FROM account where account = \"%s\"", userName);
        List<AccountBean> accounts = jdbcTemplate.query(sql, new RowMapper<AccountBean>() {
            public AccountBean mapRow(ResultSet resultSet, int i) throws SQLException {
                String account = resultSet.getString("account");
                String pwd = resultSet.getString("pwd");
                AccountBean accountBean = new AccountBean();
                accountBean.setAccount(account);
                accountBean.setPwd(pwd);
                return accountBean;
            }
        });
        return accounts;
    }

}
