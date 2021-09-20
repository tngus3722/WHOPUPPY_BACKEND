package com.whopuppy.service;

import com.whopuppy.domain.CommentDTO;
import com.whopuppy.domain.criteria.CommentCriteria;

import java.util.List;

public interface CommentService {

    String registerComment(CommentDTO commentDTO)throws Exception;

    String updateComment(CommentDTO commentDTO, Long id)throws Exception;

    String deleteComment(Long id)throws Exception;

    List<CommentDTO> getCommentList(CommentCriteria commentCriteria)throws Exception;

}