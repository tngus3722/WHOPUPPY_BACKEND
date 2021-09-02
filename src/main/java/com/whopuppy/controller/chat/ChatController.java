package com.whopuppy.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.whopuppy.annotation.Auth;
import com.whopuppy.domain.chat.ChatRoom;
import com.whopuppy.service.ChatService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/chat")
public class ChatController {
    @Autowired
    @Qualifier("stompChatServiceImpl")
    private ChatService chatService;


    @GetMapping(value="")
    //@Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    //@ApiOperation(value = "챗 홈 페이지 로딩", notes = "수제간식 레시피 글 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public String rooms() throws JsonProcessingException {
        return "/chat/home";

    }

    @GetMapping("/enter/room/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) throws JsonProcessingException {
        model.addAttribute("roomId",roomId);

        return "/chat/roomdetail";

    }

}
