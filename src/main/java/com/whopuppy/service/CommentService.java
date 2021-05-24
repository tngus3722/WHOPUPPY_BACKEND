package com.whopuppy.service;

import com.whopuppy.domain.CommentDTO;

import java.util.List;

public interface CommentService {

    String registerComment(CommentDTO commentDTO)throws Exception;

    String updateComment(CommentDTO commentDTO, Long id)throws Exception;

    String deleteComment(Long id)throws Exception;

    List<CommentDTO> getCommentList(Long article_id)throws Exception;

}