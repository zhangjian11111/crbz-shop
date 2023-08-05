package com.itheima.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.controller.utils.R;
import com.itheima.mapper.ZbforcoshineMapper;
import com.itheima.domain.dto.TimeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.itheima.domain.ZbforcoshineEntity;
import com.itheima.service.ZbforcoshineService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


/**
 *
 *
 * @author zhangjian
 * @email 1097951193@qq.com
 * @date 2022-05-18 22:27:09
 */
@Slf4j
@RestController
@RequestMapping("zbfc")
public class ZbforcoshineController {
    @Autowired
    private ZbforcoshineService zbforcoshineService;


    @Autowired
    ZbforcoshineMapper zbforcoshineMapper;

    @Value("${zbbb.nacos.namespace}")
    public String nacosnamespace;

    @GetMapping("/configup")
    public String getUpConfig() {
        System.out.println("看看更新了吗："+nacosnamespace);
        return nacosnamespace;
    }

    @GetMapping("/all")
    public List<HashMap<Object, Object>> getAll() {
        System.out.println("前台调用借口，直接返回SQL查询结果");
        List<HashMap<Object, Object>> list = zbforcoshineMapper.weGet();
        return list;
    }



    /**
     * 列表
     */
    /*@RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = zbforcoshineService.queryPage(params);

        return R.ok().put("page", page);
    }*/

    @GetMapping("/list/{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, TimeDto timeDto){
        System.out.printf("C1被调用了");
        System.out.println(timeDto.toString());
        if (timeDto.getStarttime() == null || timeDto.getEndtime() == null
                || timeDto.getStarttime()=="" || timeDto.getEndtime()==""
                || "null".equals(timeDto.getStarttime()) || "null".equals(timeDto.getEndtime())) {
            timeDto.setStarttime("");
            timeDto.setEndtime("");
        }
        IPage<ZbforcoshineEntity> page = zbforcoshineService.getPage(currentPage, pageSize,timeDto);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if( currentPage > page.getPages()){
            page = zbforcoshineService.getPage((int)page.getPages(), pageSize,timeDto);
        }
        return new R(true, page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
        ZbforcoshineEntity id1 = zbforcoshineService.getById(id);
        return new R(true,zbforcoshineService.getById(id));
    }

    @GetMapping("/info")
    public R info(){
        return new R(true,zbforcoshineService.list());
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody ZbforcoshineEntity zbforcoshine){
        boolean flag = zbforcoshineService.save(zbforcoshine);
        return new com.itheima.controller.utils.R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody ZbforcoshineEntity zbforcoshine){
        boolean flag = zbforcoshineService.updateById(zbforcoshine);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }
    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Integer id){
        boolean flag = zbforcoshineService.removeById(id);
        return new R(flag);
    }

    /**
     * 导出周报
     */
    @GetMapping("/export")
    public void exportZb(HttpServletResponse response, TimeDto timeDto){
        String filename = null;
        if (
                timeDto.getStarttime() == null || timeDto.getEndtime() == null
                || timeDto.getStarttime()=="" || timeDto.getEndtime()==""
                || "null".equals(timeDto.getStarttime()) || "null".equals(timeDto.getEndtime())){
            timeDto.setStarttime("");
            timeDto.setEndtime("");
            filename = "张建周报All.xlsx";
        }else {
//            filename = "周报"+timeDto.getStarttime()+"---"+timeDto.getEndtime()+".xlsx";
            filename = "张建周报"+timeDto.getEndtime()+".xlsx";
        }
        List<ZbforcoshineEntity> list = zbforcoshineService.getByDate(timeDto);
        zbforcoshineService.exportZb(response,filename,list);
    }

}
