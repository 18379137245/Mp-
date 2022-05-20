package com.ding.project.controller;

import com.ding.project.dto.LoginDTO;
import com.ding.project.service.AccountService;
import com.ding.project.service.ResourceService;
import com.ding.project.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ResourceService resourceService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @param model
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    @PostMapping("/login")
    public String login(String username, String password, HttpSession session,
                        RedirectAttributes attributes, Model model){
        LoginDTO loginDTO = accountService.login(username,password);
        String error = loginDTO.getError();

        if (error == null){
            session.setAttribute("account",loginDTO.getAccount());
            List<ResourceVO> resourceVOS = resourceService.listResourceByRoleId(loginDTO.getAccount().getRoleId());
            model.addAttribute("resources",resourceVOS);

//            HashSet<String> modele = resourceService.convert(resourceVOS);
        }else {
            attributes.addFlashAttribute("error",error);
        }

        return loginDTO.getPath();
    }
//    登出方法
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
//    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
