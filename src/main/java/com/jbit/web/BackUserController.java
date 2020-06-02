package com.jbit.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("backend")
public class BackUserController {
    /**
     * 后台登录
     * @return
     */
    @PostMapping("login")
    public String login(){
        return "";
    }
}
