package com.itheima.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.domain.dto.TimeDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itheima.mapper.ZbforcoshineMapper;
import com.itheima.domain.ZbforcoshineEntity;
import com.itheima.service.ZbforcoshineService;

import javax.servlet.http.HttpServletResponse;


@Service("zbforcoshineService")
@Slf4j
public class ZbforcoshineServiceImpl extends ServiceImpl<ZbforcoshineMapper, ZbforcoshineEntity> implements ZbforcoshineService {

    @Autowired
    private ZbforcoshineMapper zbforcoshineMapper;


    @Override
    public IPage<ZbforcoshineEntity> getPage(int currentPage, int pageSize, TimeDto timeDto) {
        LambdaQueryWrapper<ZbforcoshineEntity> lqw = new LambdaQueryWrapper<ZbforcoshineEntity>();
        lqw.orderByDesc(ZbforcoshineEntity::getProposetime);
        lqw.between(Strings.isNotEmpty(timeDto.getStarttime())&&Strings.isNotEmpty(timeDto.getEndtime()),ZbforcoshineEntity::getProposetime,timeDto.getStarttime(),timeDto.getEndtime());
        lqw.like(Strings.isNotEmpty(timeDto.getSerial()),ZbforcoshineEntity::getSerial,timeDto.getSerial());
        lqw.like(Strings.isNotEmpty(timeDto.getDescription()),ZbforcoshineEntity::getDescription,timeDto.getDescription());
        lqw.like(Strings.isNotEmpty(timeDto.getRstatus()),ZbforcoshineEntity::getRstatus,timeDto.getRstatus());
        IPage page = new Page(currentPage,pageSize);
        zbforcoshineMapper.selectPage(page,lqw);
        return page;
    }

    @Override
    public List<ZbforcoshineEntity> getByDate(TimeDto timeDto) {
        log.info("timedto:"+timeDto.toString());
        LambdaQueryWrapper<ZbforcoshineEntity> lqw = new LambdaQueryWrapper<ZbforcoshineEntity>();
        lqw.like(Strings.isNotEmpty(timeDto.getSerial()),ZbforcoshineEntity::getSerial,timeDto.getSerial());
        lqw.like(Strings.isNotEmpty(timeDto.getDescription()),ZbforcoshineEntity::getDescription,timeDto.getDescription());
        lqw.like(Strings.isNotEmpty(timeDto.getRstatus()),ZbforcoshineEntity::getRstatus,timeDto.getRstatus());
        lqw.between(Strings.isNotEmpty(timeDto.getStarttime())&&Strings.isNotEmpty(timeDto.getEndtime()),ZbforcoshineEntity::getProposetime,timeDto.getStarttime(),timeDto.getEndtime());
        List<ZbforcoshineEntity> list = zbforcoshineMapper.selectList(lqw);
        return list;
    }

    @Override
    public void exportZb(HttpServletResponse response, String filename, List<ZbforcoshineEntity> list) {
        // ?????? ????????????????????????class??????????????????????????????sheet?????????????????? ??????????????????????????????
//        EasyExcel.write(filename, ZbforcoshineEntity.class).sheet("????????????")
//                .doWrite(list);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            log.info("listlist"+list.toString());
            EasyExcel.write(response.getOutputStream(),ZbforcoshineEntity.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // ?????? column ?????????????????????????????? 255 ??????
                    .sheet("????????????").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        EasyExcel.write(filename, ZbforcoshineEntity.class).sheet("????????????").doWrite(list);

    }

}
