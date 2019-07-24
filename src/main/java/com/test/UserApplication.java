package com.test;

import com.test.filter.CharacterEncodingFilter;
import com.test.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UserApplication {

    @Resource
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);

    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new TokenFilter(jdbcTemplate));
        filterRegistrationBean.setFilter(new CharacterEncodingFilter());
        List<String> urlPatterns = new ArrayList<String>();

        urlPatterns.add("/api/*");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }

}
