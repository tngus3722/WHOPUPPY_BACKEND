package com.whopuppy.mapper;

import com.whopuppy.domain.CommentDTO;
import com.whopuppy.domain.criteria.CommentCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.hibernate.Criteria;

import java.util.List;

@Mapper
public interface CommentMapper {
    Integer insertComment(CommentDTO commentDTO);
    CommentDTO selectCommentDetail(Long comment_id);
    Integer updateComment(CommentDTO commentDTO, Long id);
    Integer deleteComment(Long comment_id);
    List<CommentDTO> selectCommentList(CommentCriteria criteria);
    Long isCommentCreated(Long id);
}
