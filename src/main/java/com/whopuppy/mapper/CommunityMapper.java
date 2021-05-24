package com.whopuppy.mapper;

import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleComment;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.community.Board;
import com.whopuppy.domain.criteria.ArticleCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface CommunityMapper {

    void initS3UrlHistory(Article article);
    void insertImageId(Article article);
    Long postArticle(Article article);
    List<Board> getBoards();
    Board getBoard(Long id);
    void postArticleImage(String url);
    Long getTargetArticle(Long id);
    void completePostArticle(Article article);
    List<String> getImageUrls(Long id);
    List<Article> getArticles(ArticleCriteria articleCriteria);
    Long getArticleAuthor(Long id);
    void hardDeleteArticle(Long id);
    void initImages(Long id);
    Long getTargetArticlePosted(Long id);
    void postComment(ArticleComment articleComment);
    List<ArticleComment> getArticleComment(Long id);
    Long getAuthorComment(Long id, Long commentId);
    void softDeleteComment(Long id, Long commentId);
    List<String> getRegion();
}
