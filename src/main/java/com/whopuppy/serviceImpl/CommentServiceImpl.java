package com.whopuppy.serviceImpl;

import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.whopuppy.domain.CommentDTO;
import com.whopuppy.mapper.CommentMapper;
import com.whopuppy.service.CommentService;
import com.whopuppy.domain.criteria.CommentCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public String registerComment(CommentDTO commentDTO) throws Exception{
        int queryResult = 0;
        queryResult = commentMapper.insertComment(commentDTO);

        return (queryResult == 1) ? "댓글이 작성되었습니다." : "댓글 작성에 실패했습니다.";
    }

    @Override
    public String updateComment(CommentDTO commentDTO, Long id) throws Exception{
        int queryResult = 0;

        if (commentMapper.isCommentCreated(id) == null) {
            queryResult = commentMapper.updateComment(commentDTO, id);
        }

        return (queryResult == 1) ? "댓글이 수정되었습니다." : "댓글 수정에 실패했습니다.";
    }
    @Override
    public String deleteComment(Long id) throws Exception{
        int queryResult = 0;

        CommentDTO commentDTO = commentMapper.selectCommentDetail(id);

        if (commentDTO != null && "N".equals(commentDTO.getIs_deleted())) {
            queryResult = commentMapper.deleteComment(id);
        }
        else
            throw new Exception();


        return "댓글이 삭제되었습니다.";
    }

    @Override
    public List<CommentDTO> getCommentList(CommentCriteria commentCriteria) throws Exception{
        List<CommentDTO> commentList = Collections.emptyList();

        Long commentTotalCount = commentMapper.selectCommentTotalCount(commentCriteria.getArticle_id());

        if (commentTotalCount > 0) {
            commentList = commentMapper.selectCommentList(commentCriteria);
        }
        else
            throw new Exception();

        return commentList;
    }
}
