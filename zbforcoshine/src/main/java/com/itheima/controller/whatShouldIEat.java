package com.itheima.controller;

import cn.hutool.json.JSONUtil;
import com.itheima.controller.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/zbfc/tools")
public class whatShouldIEat {


    @GetMapping(value = "/eatWhat/{id}")
    public ArrayList getFoods(@PathVariable String id){
        ArrayList<Object> allMySettings = new ArrayList<>();
        ArrayList<Object> images = new ArrayList<>();
        Map<String, Object> blockMap = new HashMap<>();
        Map<String, Object> imageMap = new HashMap<>();
        blockMap.put("padding","0");
        blockMap.put("background","#000");
        imageMap.put("src","unKnow");
        imageMap.put("top","0px");
        imageMap.put("width","0px");
        imageMap.put("height","0px");
        imageMap.put("rotate",false);
        images.add(imageMap);
        blockMap.put("imgs",images);
        allMySettings.add(blockMap);

        return allMySettings;


    }

}
