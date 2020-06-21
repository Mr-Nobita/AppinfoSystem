package com.jbit.web;

import com.jbit.pojo.AppCategory;
import com.jbit.service.AppcategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;

@Controller
public class AppCategoryController {
    @Resource
    private AppcategoryService appcategoryService;
    @GetMapping("categorylevellist")
    @ResponseBody
    public List<AppCategory>queryByPid(Long id){
        return appcategoryService.queryBypid(id);
    }
}
