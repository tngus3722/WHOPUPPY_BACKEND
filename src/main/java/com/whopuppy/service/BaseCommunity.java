package com.whopuppy.service;

import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleComment;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.community.Board;
import com.whopuppy.domain.criteria.ArticleCriteria;
import com.whopuppy.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface BaseCommunity {


    Board getBoard(Long id);
    List<Board> getBoards();
    String uploadArticleImages(MultipartFile multipartFile) throws Exception;
    BaseResponse postArticle(Article article);
    List<Article> getArticles(ArticleCriteria articleCriteria);
    BaseResponse updateArticle(Article article, Long id);
    BaseResponse deleteArticle(Long id);
    List<ArticleComment> getComment(Long id);
    BaseResponse postComment(ArticleComment articleComment, Long id);
    BaseResponse deleteComment(Long id, Long commentId);
}
