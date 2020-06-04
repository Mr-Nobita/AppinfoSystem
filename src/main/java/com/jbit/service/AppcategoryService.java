package com.jbit.service;

import com.jbit.mapper.AppCategoryMapper;
import com.jbit.pojo.AppCategory;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import javax.annotation.Resource;
@Service
public class AppcategoryService {

    @Resource
    private AppCategoryMapper appCategoryMapper;

    public AppCategory queryById(Long id){
        return appCategoryMapper.selectByPrimaryKey(id);
    }
}
