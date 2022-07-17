package com.itheima.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Properties;

/**
     * @create: 2022-02-21 19:28
     * @author: 张建
     * @description:邮件发送工具
     **/
@Component
@Slf4j
public class SendMailUtil {
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.nickname}")
    private String nickName;

    @Autowired
    private JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(this.getClass());

        /**
         * 发送文本
         * @param to
         * @param title
         * @param content
         */
    public void sendMyEmail(String to, String title, String content){
        String MynickName = null;
        try {
            MynickName = new String((nickName + "<" + from + ">")
                    .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MynickName); //发送人
        message.setTo(to);   //收件人
        message.setSubject(title);  //邮件名
        message.setText(content);   //邮件内容
        mailSender.send(message);
        logger.info("已经发送");
    }

        /**
         * 发送带附件的邮件
         * @param to
         * @param subject
         * @param content
         * @param filePath
         */
        public void sendTestAttachmentsMail(String subject, String content, String filePath, String fileName, String... to) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();

//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(nickName+"<"+from+">");
            helper.setTo(to[0]);
            helper.addCc(to[1]);
            log.info("to[0]"+to[0]);
            log.info("to[1]"+to[1]);
            helper.setSubject(subject);
            helper.setText(content, true);

            /*Context context = new Context();
            context.setVariable("title",subject);
            //context.setVariables(StringUtils.beanToMap(o));
            //获取模板html代码
            String content = templateEngine.process(templateName, context);*/


            FileSystemResource file = new FileSystemResource(new File(filePath));
//            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            mailSender.send(message);
        }


    public void sendAttachmentsMail(String subject, String content, String filePath, String fileName, String... to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(nickName+"<"+from+">");
        helper.setTo(to[0]);
        helper.addCc(to[1]);
        log.info("to[3]"+to[0]);
        log.info("to[4]"+to[1]);
        helper.setSubject(subject);
        helper.setText(content, true);

            /*Context context = new Context();
            context.setVariable("title",subject);
            //context.setVariables(StringUtils.beanToMap(o));
            //获取模板html代码
            String content = templateEngine.process(templateName, context);*/


        FileSystemResource file = new FileSystemResource(new File(filePath));
//            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }



    }

