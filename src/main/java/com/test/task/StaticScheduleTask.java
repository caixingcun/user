package com.test.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Configuration
@EnableScheduling
public class StaticScheduleTask {
    //3.添加定时任务
//    @Scheduled(cron = "0/5 * * * * ?")
    //或者指定间隔时间  一分钟执行一次
    @Scheduled(fixedRate =   60 * 1000)
    private void upDatePic() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date())+"定时任务测试");

    }
}
