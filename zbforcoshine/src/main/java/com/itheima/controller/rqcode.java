package com.itheima.controller;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
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



    public static void main(String[] args) {
//        R r = new rqcode().deQrcode();
//        System.out.println(r);
        HttpUtil.get("http://192.168.2.192:8012/zbfc/export");


    }
}
