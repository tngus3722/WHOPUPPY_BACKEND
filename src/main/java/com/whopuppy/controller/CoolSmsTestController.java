package com.whopuppy.controller;

import com.whopuppy.service.UserService;
import com.whopuppy.util.CoolSmsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class CoolSmsTestController {

    /*
    @Resource
    private CoolSmsUtil coolSmsUtil;

    @PostMapping("/single")
    public void singleSms(@RequestParam String number)  {
        coolSmsUtil.singleSms(number, "Test");
    }
    @PostMapping(value = "/group")
    public void groupSms(){
        coolSmsUtil.createGroup();
    }

    @PostMapping(value = "/groupMessagePut")
    public void groupMessagePut(@RequestParam String groupId){
        coolSmsUtil.putMessageToGroup(groupId);
    }
    @PostMapping(value = "/groupMessageSend")
    public void groupMessageSend(@RequestParam String groupId){
        coolSmsUtil.sendGroupSms(groupId);
    }

    @GetMapping(value = "/group")
    public void getGroupList(){
        coolSmsUtil.getGroupList();
    }

    @DeleteMapping(value = "/group")
    public void deleteGroup(@RequestParam String groupId){
        coolSmsUtil.deleteGroup(groupId);
    }
     */
}
