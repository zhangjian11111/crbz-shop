package com.itheima.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

    /**
     * @Auther: 张建
     * @DateTime :  2021-12-23 上午 11:01
     * @descriptoin:定时任务的代码
     */
@Component
@Slf4j
public class MyScheduledJob {

    @Autowired
    private SendMailUtil mailService;

    @Value("${spring.mail.to1}")
    private String to1;
    @Value("${spring.mail.to2}")
    private String to2;
    @Value("${spring.mail.to3}")
    private String to3;
    @Value("${spring.mail.to4}")
    private String to4;

    /*定时任务的方法*/
    @Scheduled(cron = "09 09 09 ? * FRI")//每周五上午9:09:09
    //@Scheduled(cron = "00 22 19 * * ?")
    public void scheduledMetgod() {
        SimpleDateFormat timeName = new SimpleDateFormat("yyyy-MM-dd");
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"定时器被触发!!!");
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endTime);
        cal.add(Calendar.DATE,-7);
        Date startTime = cal.getTime();
        String formatEndTime = timeName.format(endTime);
        String formatStartTime = timeName.format(startTime);
        log.info(formatEndTime+",,,"+formatStartTime);

        /*try {
            *//*Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+
                    "http://localhost:8012/zbfc/export/?starttime="+formatStartTime+"&endtime="+formatEndTime);*//*
            Runtime.getRuntime().exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe "+
                    "http://localhost:8012/zbfc/export/?starttime="+formatStartTime+"&endtime="+formatEndTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        try {
            /*try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            mailService.sendAttachmentsMail(timeName.format(new Date())+"张建周报",
                    "<h1 style=\"color: #f78989\">早上好呀，这是张建的本周周报。</h1>",
                    "D:\\data\\应用数据\\桌面\\百度云同步空间\\BaiduNetdiskWorkspace\\开先软件\\周报和月度统计\\",
                    "张建周报"+timeName.format(new Date())+".xlsx",to3,to4);
            log.info("邮件发送成功!!!");
        } catch (MessagingException e) {
            log.error("邮件发送失败!!!");
            e.printStackTrace();
        }

    }


    /******发送前测试************/
    /*定时任务的方法*/
    @Scheduled(cron = "07 07 07 ? * FRI")//每周五上午7:07:07
    //@Scheduled(cron = "00 18 19 * * ?")
    public void TestScheduledMetgod() {
        SimpleDateFormat timeName = new SimpleDateFormat("yyyy-MM-dd");
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"测试定时器被触发!!!");

        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endTime);
        cal.add(Calendar.DATE,-7);
        Date startTime = cal.getTime();
        String formatEndTime = timeName.format(endTime);
        String formatStartTime = timeName.format(startTime);
        log.info(formatEndTime+",,,"+formatStartTime);

        try {
            /*Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+
                    "http://localhost:8012/zbfc/export/?starttime="+formatStartTime+"&endtime="+formatEndTime);*/
            Runtime.getRuntime().exec("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe "+
                    "http://localhost:8012/zbfc/export/?starttime="+formatStartTime+"&endtime="+formatEndTime);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mailService.sendTestAttachmentsMail(timeName.format(new Date())+"周报",
                    "<h1 style=\"color: #f78989\">早上好呀，这是张建的本周周报。</h1>",
                    "D:\\data\\应用数据\\桌面\\百度云同步空间\\BaiduNetdiskWorkspace\\开先软件\\周报和月度统计\\",
                    "张建周报"+timeName.format(new Date())+".xlsx",to1,to2);
            log.info("邮件发送成功!!!");
        } catch (MessagingException e) {
            log.error("邮件发送失败!!!");
            e.printStackTrace();
        }

    }

}


