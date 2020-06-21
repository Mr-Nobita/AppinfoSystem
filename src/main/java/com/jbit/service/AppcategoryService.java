package com.jbit.service;

import com.jbit.mapper.AppCategoryMapper;
import com.jbit.pojo.AppCategory;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppcategoryService {

    @Resource
    private AppCategoryMapper appCategoryMapper;

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public AppCategory queryById(Long id){
        return appCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询分类
     * @param id
     * @return
     */
    public List<AppCategory>queryBypid(Long id){
        Example example=new Example(AppCategory.class);
        Example.Criteria criteria=example.createCriteria();
        if (id==null){
            //第一级
            criteria.andIsNull("parentid");
        }else {
            //第二级或第三级
            criteria.andEqualTo("parentid",id);
        }
        return appCategoryMapper.selectByExample(example);
    }
}
