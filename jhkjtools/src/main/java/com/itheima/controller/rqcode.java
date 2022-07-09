package com.itheima.controller;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itheima.controller.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value ="qrcode")
public class rqcode {

    @GetMapping("/analysis")
    public R analysisQrcode(){
        Map<String, String> meat = new HashMap<>();
        meat.put("","tomatoBeefRice");
        meat.put("meat2","potatoSoup");
        String beefNoodle = JSONUtil.toJsonStr(meat);
        BufferedImage bufferedImage = QrCodeUtil.generate(beefNoodle, 200, 200);
        //QrCodeUtil.generate("wxp://f2f02evYrkZoPwginn2-CUz_yTAXJHRzjDVEi1EQKRPXgrE", 200, 200,FileUtil.file("d:/qrcode.jpg"));
        log.info("bufferedImage:::"+bufferedImage);

        long total = OshiUtil.getMemory().getTotal();
        log.info("total:"+(double)total/1024/1024/1024);
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        Console.log(cpuInfo);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            return new R(e.getMessage());
        }
        log.info("image:::"+Base64.getEncoder().encodeToString(os.toByteArray()));
        //decodeBase64File(Base64.getEncoder().encodeToString(os.toByteArray()),"d:/666.png");

        //如果二维码要在前端显示需要转成Base64
        return new R(true,(Object) Base64.getEncoder().encodeToString(os.toByteArray()));
    }

    @GetMapping("/decode")
    public R deQrcode(){
        Map<String, String> meat = new HashMap<>();
        meat.put("meat1","tomatoBeefRice");
        meat.put("meat2","potatoSoup");
        String beefNoodle = JSONUtil.toJsonStr(meat);
        BufferedImage bufferedImage = QrCodeUtil.generate(beefNoodle, 200, 200);
        //QrCodeUtil.generate("wxp://f2f02evYrkZoPwginn2-CUz_yTAXJHRzjDVEi1EQKRPXgrE", 200, 200,FileUtil.file("d:/qrcode.jpg"));
        log.info("bufferedImage:::"+bufferedImage);

        long total = OshiUtil.getMemory().getTotal();
        log.info("total:"+(double)total/1024/1024/1024);
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        Console.log(cpuInfo);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            return new R(e.getMessage());
        }
        log.info("image:::"+Base64.getEncoder().encodeToString(os.toByteArray()));
        String decode = QrCodeUtil.decode(FileUtil.file("D:\\data\\应用数据\\桌面\\公章申请二维码.png"));
        log.info("decode:::"+decode);

        //如果二维码要在前端显示需要转成Base64
        return new R(true,(Object) decode);
    }

    public static void decodeBase64File(String base64Code, String targetPath) {
        // 输出流
        FileOutputStream out =null;
        // 将base 64 转为字节数字
        byte[] buffer = new byte[0];
        try {
            buffer = new BASE64Decoder().decodeBuffer(base64Code);
            // 创建输出流
            out = new FileOutputStream(targetPath);
            // 输出
            out.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        R r = new rqcode().deQrcode();
        System.out.println(r);


    }
}
