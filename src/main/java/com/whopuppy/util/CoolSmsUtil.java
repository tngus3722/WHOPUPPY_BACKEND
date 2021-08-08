package com.whopuppy.util;

import net.nurigo.java_sdk.api.GroupMessage;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.UUID;


@Component
public class CoolSmsUtil {

    @Value("${coolsms.api.key}")
    private String coolSmsApiKey;

    @Value("${coolsms.api.secret}")
    private String coolSmSapiSecret;

    @Value("${coolsms.from.number}")
    private String from;


    //단일 문자 발송 method
    public void singleSms(String number, String text) {
        Message coolsms = new Message(coolSmsApiKey, coolSmSapiSecret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", number);
        params.put("from", from);
        params.put("type", "SMS");
        params.put("text", text);
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
    //create Group
    public void createGroup(){
        GroupMessage coolsms = new GroupMessage(coolSmsApiKey, coolSmSapiSecret);

        // Optional parameters for your own needs
        HashMap<String, String> params = new HashMap<String, String>();

        //UUID uuid = UUID.UUID();
        UUID uuid = UUID.randomUUID();
        params.put("charset", "utf8"); // utf8, euckr default value is utf8
        params.put("srk", uuid.toString()); // Solution key
        params.put("mode", "test"); // If 'test' value, refund cash to point
        params.put("delay", "10"); // '0~20' delay messages
        params.put("force_sms", "true"); // true is always send sms ( default true )
        params.put("app_version", "test app 1.2"); 	// App version

        try {
            JSONObject createdGroup = (JSONObject) coolsms.createGroup(params); // create group
            System.out.println(createdGroup.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());

        }


    }

    //group의 List출력.
    public void getGroupList(){
        GroupMessage coolsms = new GroupMessage(coolSmsApiKey, coolSmSapiSecret);

        try {
            JSONObject obj = (JSONObject) coolsms.getGroupList();
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    // 그룹에 메세지 저장 
    public void putMessageToGroup(String groupId) {

        GroupMessage coolsms = new GroupMessage(coolSmsApiKey, coolSmSapiSecret);

        HashMap<String, String> params1 = new HashMap<String, String>();
        HashMap<String, String> params2 = new HashMap<String, String>();
        HashMap<String, String> params3 = new HashMap<String, String>();
        // options(to, from, text) are mandatory. must be filled
        params1.put("to", "01037220151");
        params1.put("from", from);
        params1.put("text", "Springboot group sms send test by soohyeon");
        params1.put("group_id", groupId); // Group ID


        // options(to, from, text) are mandatory. must be filled
        params2.put("to", from);
        params2.put("from", from);
        params2.put("text", "Springboot group sms send test by soohyeon");
        params2.put("group_id", groupId); // Group ID



        try {
            JSONObject obj = (JSONObject) coolsms.addMessages(params1);
            System.out.println(obj.toString());
            JSONObject obj2 = (JSONObject) coolsms.addMessages(params2);
            System.out.println(obj2.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    //그룹에 저장된 메세지를 단체  전송
    public void sendGroupSms(String groupId){
        GroupMessage coolsms = new GroupMessage(coolSmsApiKey, coolSmSapiSecret);

        // group_id, message_ids are mandatory.
        String group_id = groupId; // Group ID

        try {
            JSONObject obj = (JSONObject) coolsms.sendGroupMessage(group_id);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    //그룹 삭제 메소드
    public void deleteGroup(String groupIds){
        GroupMessage coolsms = new GroupMessage(coolSmsApiKey, coolSmSapiSecret);

        // group_ids are mandatory
        String group_ids = groupIds; // Group IDs
        try {
            JSONObject obj = (JSONObject) coolsms.deleteGroups(group_ids);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

}
