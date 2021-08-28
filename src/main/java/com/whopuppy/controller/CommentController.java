package com.whopuppy.controller;

import com.whopuppy.annotation.ValidationGroups;
import com.whopuppy.annotation.Xss;
import com.whopuppy.domain.CommentDTO;
import com.whopuppy.domain.criteria.CommentCriteria;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {
    /***
     * 백민수
     */
    @Resource
    private CommentService commentService;

    @PostMapping
    @ApiOperation(value = "댓글 작성", notes = "댓글 작성")
    public ResponseEntity commentInsert(@RequestBody @Validated(ValidationGroups.animalComment.class) CommentDTO commentDTO) throws Exception {
        commentService.registerComment(commentDTO);
        return new ResponseEntity(new BaseResponse(commentDTO.getContent(), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "댓글 조회", notes = "댓글 조회")
    public ResponseEntity commentRead(@ModelAttribute CommentCriteria commentCriteria) throws Exception {
        return new ResponseEntity(commentService.getCommentList(commentCriteria), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "댓글 수정", notes = "댓글 수정")
    public ResponseEntity commentUpdate(@RequestBody @Validated(ValidationGroups.animalComment.class) CommentDTO commentDTO, @PathVariable Long id) throws Exception {
        return new ResponseEntity(new BaseResponse(commentService.updateComment(commentDTO, id), HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제")
    public ResponseEntity commentDelete(@PathVariable Long id) throws Exception{
        return new ResponseEntity(new BaseResponse(commentService.deleteComment(id), HttpStatus.OK), HttpStatus.OK);
    }

}
