package com.jbit.web;

import com.jbit.entity.JsonResult;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.DevUser;
import com.jbit.service.AppcategoryService;
import com.jbit.service.AppinfoService;
import com.jbit.service.DataDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("dev/app")
public class AppInfoController {
    @Resource
    private AppinfoService appInfoService;
    @Resource
    private AppcategoryService appcategoryService;
    @Resource
    private DataDictionaryService dataDictionaryService;

    /**
     * 删除图片
     * @param id
     * @param flag
     * @return
     */
    @GetMapping("delfile")
    @ResponseBody
    public JsonResult delfile(Long id ,String flag){
        if(flag.equals("logo")){
        AppInfo appInfo = appInfoService.queryById(id);
        try {
            File file=new File(appInfo.getLogolocpath());
            file.delete();//删除文件
            appInfo.setLogopicpath("");
            appInfo.setLogolocpath("");
            appInfoService.update(appInfo);
            return new JsonResult(true);
        } catch (Exception e) {
            return new JsonResult(false);
         }
        }
        return new JsonResult(false);
    }
    /**
     * 修改查询
     * @param id
     * @return
     */
    @GetMapping("appinfomodify/{id}")
    public String appinfomodify(Model model,@PathVariable Long id){
        model.addAttribute("appInfo",appInfoService.queryById(id));
        return "developer/appinfomodify";
    }

    /**
     *  验证apk是否注册
     * @param apkname
     * @return
     */
    @GetMapping("apkexist")
    @ResponseBody
    public JsonResult apkexist(String apkname){
        AppInfo appInfo = appInfoService.queryApkexist(apkname);
        if (appInfo==null){
            return new JsonResult(true);
        }
        return new JsonResult(false);
    }

    /**
     * App修改
     * @return
     */
    @PostMapping("/appinfomodify")
    public String appinfomodify(HttpSession session, AppInfo appinfo, MultipartFile attach){
        System.out.println("attach = " + attach.isEmpty());
        if (!attach.isEmpty()){
            //1.实现文件夹上传
            String server_path=session.getServletContext().getRealPath("/statics/uploadfiles/");
            appinfo.setLogopicpath("/statics/uploadfiles/"+attach.getOriginalFilename());//相对路径
            appinfo.setLogolocpath(server_path+""+attach.getOriginalFilename());
            try {
                attach.transferTo(new File(server_path,attach.getOriginalFilename()));
            } catch (IOException e) {
            }
        }
        //2.app修改
        DevUser devuser =(DevUser) session.getAttribute("devuser");
        appinfo.setUpdatedate(new Date());
        appinfo.setDevid(devuser.getId());
        appinfo.setModifyby(devuser.getId());
        appinfo.setModifydate(new Date());
        appInfoService.update(appinfo);
        return "redirect:/dev/app/list";
    }
    /**
     * App新增
     * @return
     */
    @PostMapping("/appinfoadd")
    public String appinfoadd(HttpSession session, AppInfo appinfo, MultipartFile a_logoPicPath){
        //1.实现文件夹上传
        String server_path=session.getServletContext().getRealPath("/statics/uploadfiles/");
       //验证大小和图片规格
        try {
            a_logoPicPath.transferTo(new File(server_path,a_logoPicPath.getOriginalFilename()));
        } catch (IOException e) {
        }
        //2.app添加
        DevUser devuser =(DevUser) session.getAttribute("devuser");
        appinfo.setDevid(devuser.getId());
        appinfo.setCreatedby(devuser.getId());
        appinfo.setCreationdate(new Date());
        appinfo.setLogopicpath("/statics/uploadfiles/"+a_logoPicPath.getOriginalFilename());//相对路径
        appinfo.setLogolocpath(server_path+""+a_logoPicPath.getOriginalFilename());
        appInfoService.save(appinfo);
        return "redirect:/dev/app/list";
    }


    /**
     * App信息列表
     * @param session
     * @param model
     * @param pagenum
     * @param querySoftwareName
     * @param queryStatus
     * @param queryFlatformId
     * @param queryCategoryLevel1
     * @param queryCategoryLevel2
     * @param queryCategoryLevel3
     * @return
     */
    @RequestMapping("/list")
    public String list(HttpSession session, Model model
            ,@RequestParam(defaultValue = "1",value = "pageIndex")  Integer pagenum,
                       String querySoftwareName,
                       Long queryStatus,
                       Long queryFlatformId,
                       Long queryCategoryLevel1,
                       Long queryCategoryLevel2,
                       Long queryCategoryLevel3
                       ){
        DevUser devuser =(DevUser) session.getAttribute("devuser");
        /*List<AppInfo> appInfos = appInfoService.queryAppInfo(devuser.getId());*/
        //防止session丢失
        model.addAttribute("pageinfo",appInfoService.queryAppInfo(pagenum,devuser.getId(),querySoftwareName,queryStatus,queryFlatformId,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3));
        /*System.out.println(devuser.getId());*/
        //处理一级分类
        model.addAttribute("categoryLevel1List",appcategoryService.queryBypid(null));
        //处理二级分类
        if (queryCategoryLevel1!=null){
            model.addAttribute("categoryLevel2List",appcategoryService.queryBypid(queryCategoryLevel1));
        }
        //处理三级分类
        if (queryCategoryLevel2!=null){
            model.addAttribute("categoryLevel3List",appcategoryService.queryBypid(queryCategoryLevel2));
        }
        //处理状态与平台
        model.addAttribute("statusList",dataDictionaryService.querDataList("APP_STATUS"));
        model.addAttribute("flatFormList",dataDictionaryService.querDataList("APP_FLATFORM"));
        //参数重新封装传递到页面
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);
        return "developer/appinfolist";
    }
}
