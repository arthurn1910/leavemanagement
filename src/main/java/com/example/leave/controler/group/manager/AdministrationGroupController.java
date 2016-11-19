package com.example.leave.controler.group.manager;

import com.example.leave.dto.group.TeamGroupDTO;
import com.example.leave.endpoint.group.GroupEndpoint;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by arthurn on 14.11.16.
 */
@Controller
public class AdministrationGroupController {
    @Autowired
    GroupEndpoint groupEndpoint;

    @RequestMapping(value = "/getAdministrationGroup", method = RequestMethod.GET)
    public @ResponseBody String getAdministrationGroup() {
        return JSONParser.quote("/administrationGroup");
    }

    @RequestMapping(value = "/administrationGroup", method = RequestMethod.GET)
    public String administrationGroup() {
        return "group/manager/groupAdministration";
    }

    @RequestMapping(value = "/listOfApplication", method = RequestMethod.GET)
    public String listMemberGroup() {
        return "group/manager/listOfApplication";
    }

    @RequestMapping(value = "/acceptApplication", method = RequestMethod.POST)
    public @ResponseBody String acceptApplication(@RequestBody String login){
        groupEndpoint.acceptApplication(login);
        return JSONParser.quote("Application user:"+login+" was accepted");
    }
    @RequestMapping(value = "/rejectApplication", method = RequestMethod.POST)
    public @ResponseBody String rejectApplication(@RequestBody String login){
        groupEndpoint.removeMember(login);
        return JSONParser.quote("Application user: "+login+" was reject");
    }

    @RequestMapping(value = "/removeGroup", method = RequestMethod.GET)
    public @ResponseBody String removeGroup(){
        groupEndpoint.removeGroup();
        return JSONParser.quote("groupList");
    }

    @RequestMapping(value = "/removeGroupView", method = RequestMethod.GET)
    public String importantDatesView() {
        return "group/manager/removeGroup";
    }
}
