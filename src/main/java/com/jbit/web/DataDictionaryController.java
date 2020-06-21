package com.jbit.web;

import com.jbit.pojo.DataDictionary;
import com.jbit.service.DataDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;

@RestController
public class DataDictionaryController {
    @Resource
    private DataDictionaryService dataDictionaryService;
    @GetMapping("datadictionarylist")
    public List<DataDictionary>queryList(String tcode){
        return dataDictionaryService.querDataList(tcode);
    }
}
