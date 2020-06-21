package com.jbit.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbit.mapper.AppInfoMapper;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppinfoService {
    @Resource
    private AppInfoMapper appInfoMapper;
    @Resource
    private DataDictionaryService dataDictionaryService;
    @Resource
    private AppcategoryService appcategoryService;
    @Resource
    private AppVersionService appVersionService;
    /**
     * App列表查询每一个Dev登录后只查看属于自己的AppInfo
     * @return
     */
    public PageInfo queryAppInfo(Integer pagenum, Long devId, String querySoftwareName, Long queryStatus, Long queryFlatformId, Long queryCategoryLevel1, Long queryCategoryLevel2, Long queryCategoryLevel3){
        PageHelper.startPage(pagenum,5);
        Example example =new Example(AppInfo.class);
        Example.Criteria criteria=example.createCriteria();
        if (!StringUtils.isEmpty(querySoftwareName)){
            criteria.andLike("softwarename","%"+querySoftwareName+"%");
        }
        if (queryStatus!=null&&queryStatus!=0){
            criteria.andEqualTo("status",queryStatus);
        }
        if (queryFlatformId!=null&&queryFlatformId!=0){
            criteria.andEqualTo("flatformid",queryFlatformId);
        }
        if (queryCategoryLevel1!=null&&queryCategoryLevel1!=0){
            criteria.andEqualTo("categorylevel1",queryCategoryLevel1);
        }
        if (queryCategoryLevel2!=null&&queryCategoryLevel2!=0){
            criteria.andEqualTo("categorylevel2",queryCategoryLevel2);
        }
        if (queryCategoryLevel3!=null&&queryCategoryLevel3!=0){
            criteria.andEqualTo("categorylevel3",queryCategoryLevel3);
        }
        criteria.andEqualTo("devid",devId);
        List<AppInfo> appInfos=appInfoMapper.selectByExample(example);
        //绑定其他数据
        dindData(appInfos);
        //处理分页
        return new PageInfo<>(appInfos);
    }

    /**
     * 绑定数据
     * @param appInfos
     */
    private void dindData(List<AppInfo> appInfos) {

        appInfos.forEach(app ->{
            //所属平台
            app.setFlatformname(dataDictionaryService.queryData("APP_FLATFORM",app.getFlatformid()).getValuename());
            //加载分类
            app.setCategorylevel1name(appcategoryService.queryById(app.getCategorylevel1()).getCategoryname());
            app.setCategorylevel2name(appcategoryService.queryById(app.getCategorylevel2()).getCategoryname());
            app.setCategorylevel3name(appcategoryService.queryById(app.getCategorylevel3()).getCategoryname());
            //状态
            app.setStatusname(dataDictionaryService.queryData("APP_STATUS",app.getStatus()).getValuename());
            //版本号
            AppVersion appVersion = appVersionService.queryById(app.getVersionid());
            if (appVersion!=null){
                app.setVersionno(appVersion.getVersionno());
            }
        });

    }
}