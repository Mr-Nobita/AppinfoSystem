package com.jbit.web;

import com.jbit.pojo.DevUser;
import com.jbit.service.DevUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("dev")//区分前缀
public class DevUserController {
    @Resource
    private DevUserService devUserService;

    /**
     * 开发者登录
     * @return
     */
    @PostMapping("login")
    public String login(Model model, HttpSession session, String devcode, String devpassword){
        DevUser devUser = devUserService.queryLogin(devcode, devpassword);
        if (devUser!=null){
            session.setAttribute("devUser",devUser);
            return "developer/main.jsp";//重定向
        }
        model.addAttribute("error","用户名或者密码有误，请重新登录");
        return "devlogin";
    }
}
