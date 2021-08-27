package com.whopuppy.controller;

import com.amazonaws.Response;
import com.whopuppy.annotation.Auth;
import com.whopuppy.annotation.Xss;
import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.service.SnackService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/snack")
public class SnackController {

    @Autowired
    private SnackService snackService;


    @Xss
    @PostMapping("/article")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "수제간식 레시피 글 작성", notes = "수제간식 레시피 글 작성", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity postArticle(@RequestBody  SnackArticle snackArticle){
        return new ResponseEntity(snackService.postArticle(snackArticle), HttpStatus.CREATED);
    }

    @PostMapping("/article/image")
    @Auth(authority = Auth.Authority.NONE, role = Auth.Role.NORMAL)
    @ApiOperation(value = "수제간식 레시피 글 사진 url 반환", notes = "수제간식 레시피 글 사진 url 반환", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity uploadArticleImages(@RequestBody MultipartFile multipartFile) throws Exception{
        return new ResponseEntity(snackService.uploadImage(multipartFile), HttpStatus.OK);
    }

    @GetMapping("/article")
    @ApiOperation(value = "수제간식 레시피 글 조회", notes = "수제간식 레시피 글 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity getAllSnackArticles(@ModelAttribute BaseCriteria baseCriteria){
        return new ResponseEntity(snackService.getAllSnackArticles(baseCriteria), HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}")
    @ApiOperation(value = "수제간식 레시피 글 단일 조회", notes = "수제간식 레시피 글 단일 조회", authorizations = @Authorization(value = "Bearer +accessToken"))
    public ResponseEntity getSnackArticle(@PathVariable Long articleId){
        return new ResponseEntity(snackService.getArticle(articleId), HttpStatus.OK);
    }
}
