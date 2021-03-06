package com.whopuppy.mapper;

import com.whopuppy.domain.CommentDTO;
import com.whopuppy.domain.criteria.CommentCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.hibernate.Criteria;

import java.util.List;

@Mapper
public interface CommentMapper {
    Integer insertComment(CommentDTO commentDTO);
    Integer updateComment(CommentDTO commentDTO);
    Integer deleteComment(Long comment_id);
    List<CommentDTO> selectCommentList(CommentCriteria criteria);
    Long isCommentCreated(Long id);
}
