package com.example.leave.controler.account;
import com.example.leave.dto.account.AuthDTO;
import com.example.leave.endpoint.account.AccountEndpoint;
import com.example.leave.entity.account.AccessLevel;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.logging.Logger;

/**
 * Created by Medion on 2016-09-04.
 */
@Controller
public class IndexController {

    Logger log = Logger.getLogger(IndexController.class.getName());

    @Autowired
    AccountEndpoint accountEndpoint;

    @RequestMapping("/")
    public String index(Model model) {

        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String name = user.getUsername(); //get logged in username
            String role="";
            for(GrantedAuthority auth : user.getAuthorities()) {
                role+=auth.getAuthority()+" ";
            }

            model.addAttribute("user", name);
            model.addAttribute("role", role);
        }catch(Exception ex){
            log.info("User is not authenticate");
            model.addAttribute("user", "brak");
            model.addAttribute("role", "brak");
        }
        return "account/index";
    }

    @RequestMapping("/menu")
    public String menu() {
        return "account/menu";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {return user;
    }

    @RequestMapping("/error404")
    public String error404() {
        return "account/error404";
    }
    @RequestMapping("/error403")
    public String error403() {
        return "account/error403";
    }
    @RequestMapping("/error2")
    public String error2() {
        return "account/error";
    }
    @RequestMapping("/errorOptimistic")
    public String errorOptimistic() {
        return "account/errorOptimistic";
    }

    @RequestMapping("/isAuthenticated")
    public  @ResponseBody AuthDTO isAuthenticated()   {
        AuthDTO authDTO=new AuthDTO();
        if(accountEndpoint.isAuthenticated()) {
            authDTO.setLogin(accountEndpoint.getYourAccount().getLogin());
            for(AccessLevel accessLevel :accountEndpoint.getYourAccount().getAccessLevelCollection()){
                if(accessLevel.getActive()==true) {
                    switch(accessLevel.getLevel()){
                        case "ROLE_ADMINISTRATOR":
                            authDTO.setRoleAdministrator(true);
                            break;
                        case "ROLE_EMPLOYEE":
                            authDTO.setRoleEmployee(true);
                            break;
                        case "ROLE_ACCOUNTANT":
                            authDTO.setRoleAccountant(true);
                            break;
                        case "ROLE_MANAGER":
                            authDTO.setRoleManager(true);
                            break;
                    }
                }
            }
        }
        return authDTO;
    }




}
