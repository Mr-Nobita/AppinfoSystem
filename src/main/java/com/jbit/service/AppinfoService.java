package com.jbit.service;

import com.jbit.mapper.AppInfoMapper;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import org.springframework.stereotype.Service;

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
    public List<AppInfo>queryAppInfo(Long devid){
        AppInfo appInfo = new AppInfo();
        appInfo.setDevid(devid);
        List<AppInfo> appInfos=appInfoMapper.select(appInfo);
        //绑定其他数据
        dindData(appInfos);
        return appInfos;
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