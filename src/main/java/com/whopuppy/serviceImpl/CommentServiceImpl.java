package com.whopuppy.serviceImpl;

import com.whopuppy.domain.CommentDTO;
import com.whopuppy.domain.criteria.CommentCriteria;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.AnimalMapper;
import com.whopuppy.mapper.CommentMapper;
import com.whopuppy.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private AnimalMapper animalMapper;

    @Override
    public String registerComment(CommentDTO commentDTO) {
        if (animalMapper.findById(commentDTO.getArticle_id()) == null)
            throw new RequestInputException(ErrorMessage.ANIMAL_NOT_FOUND_EXCEPTION);
        commentMapper.insertComment(commentDTO);
        return "댓글이 작성되었습니다.";
    }

    @Override
    public String updateComment(CommentDTO commentDTO, Long id){

        if (commentMapper.isCommentCreated(id) == 0)
            throw new RequestInputException(ErrorMessage.ANIMAL_COMMENT_NOT_FOUND_EXCEPTION); //TODO 수정필요
        commentDTO.setId(id);
        commentMapper.updateComment(commentDTO);
        return "댓글이 수정되었습니다.";
    }

    @Override
    public String deleteComment(Long id){
        if (commentMapper.isCommentCreated(id) == 0)
            throw new RequestInputException(ErrorMessage.ANIMAL_COMMENT_NOT_FOUND_EXCEPTION);
        commentMapper.deleteComment(id);
        return "댓글이 삭제되었습니다.";
    }

    @Override
    public List<CommentDTO> getCommentList(CommentCriteria commentCriteria){
        if (animalMapper.findById(commentCriteria.getArticle_id()) == null)
            throw new RequestInputException(ErrorMessage.ANIMAL_COMMENT_NOT_FOUND_EXCEPTION);
        return commentMapper.selectCommentList(commentCriteria);
    }
}
