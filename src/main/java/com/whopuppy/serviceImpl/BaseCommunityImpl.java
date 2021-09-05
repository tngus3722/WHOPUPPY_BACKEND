package com.whopuppy.serviceImpl;

import com.whopuppy.domain.community.Article;
import com.whopuppy.domain.community.ArticleComment;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.community.Board;
import com.whopuppy.domain.criteria.ArticleCriteria;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.ForbiddenException;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.CommunityMapper;
import com.whopuppy.mapper.UserMapper;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.BaseCommunity;
import com.whopuppy.service.UserService;
import com.whopuppy.util.S3Util;
import org.apache.maven.plugin.lifecycle.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BaseCommunityImpl implements BaseCommunity {
    @Resource
    private CommunityMapper communityMapper;
    @Resource
    @Qualifier("UserServiceImpl")
    private UserService userService;
    @Resource
    private S3Util s3Util;
    @Value("${default.article.image}")
    private String defaultArticleImage;

    @Override
    public Board getBoard(Long id){
        return communityMapper.getBoard(id);
    }

    @Override
    public List<Board> getBoards(){
        return communityMapper.getBoards();
    }

    // img를 입력받아 s3에 업로드한뒤 url을 반환한다.
    @Override
    public String uploadArticleImages(MultipartFile multipartFile) throws Exception{

        // multipartfile이 null인 경우
        if ( multipartFile == null){
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NULL);
        }
        //multipartfile의 content type이 jpeg, png가 아닌경우
        if( !multipartFile.getContentType().equals("image/jpeg") && !multipartFile.getContentType().equals("image/png"))
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NOT_IMAGE);

        String url = s3Util.uploadObject(multipartFile);
        // s3_url에 업로드 이력을 삽입한다.
        communityMapper.postArticleImage(url);
        return url;
    }

    @Override
    public BaseResponse postArticle(Article article){

        //이미지가 없는 경우 thumbnail에 default 이미지를 넣어준다.
        if ( article.getImages() == null || article.getImages().size() == 0){
            article.setThumbnail(defaultArticleImage);
        }
        else{
            // 섬네일을 가장 첫 사진으로 지정한다.
            article.setThumbnail(article.getImages().get(0));
        }

        // region 입력값 검증
        this.regionCheck(article.getRegion());


        article.setUser_id(userService.getLoginUserId());
        // article insert
        // 사진들 insert
        try {
            // 게시글을 작성하고, s3_url 테이블에 입력받은 url들을 사용처리한다.
            Long id = communityMapper.postArticle(article);
            article.setId(id);
            // article_image table에 삽입한다.
            communityMapper.insertImageId(article);
        }catch (Throwable e){
            e.printStackTrace();
            //사진이 unique 제약을 위반했을 경우.
            throw new RequestInputException(ErrorMessage.IMAGE_FORBIDDEN_EXCEPTION);
        }
        return new BaseResponse("게시글이 작성되었습니다.", HttpStatus.CREATED);
    }

    @Override
    public List<Article> getArticles(ArticleCriteria articleCriteria){
        Long id = articleCriteria.getBoardId();
        if ( id == null || id < 0 || id > 3 ){
            throw new RequestInputException(ErrorMessage.REQUEST_INVALID_EXCEPTION);
        }

        // region값이 null이 아니라면 검증한다. null 인경우는 그냥 전지역 검색을 해준다.
        if ( articleCriteria.getRegion() != null) {
            this.regionCheck(articleCriteria.getRegion());
        }
        articleCriteria.setBoardId(id);
        return communityMapper.getArticles(articleCriteria);
    }
    //TODO 다중 이미지 업로드 함수


    @Override
    public Article getArticle(Long articleId) {
        return communityMapper.getArticle(articleId);
    }

    // region 입력값 검증
    private boolean regionCheck(String region){

        boolean check = false;
        List<String> list = communityMapper.getRegion();
        for (int i=0; i< list.size(); i++){
            if ( region.equals(list.get(i)) ){
                check = true;
            }
        }
        if (!check){
            throw new RequestInputException(ErrorMessage.REGION_INVALID);
        }
        else{
            return true;
        }
    }
    @Override
    public BaseResponse updateArticle(Article article, Long id){

        Long userId = communityMapper.getArticleAuthor(id);

        // 실제로 있는 게시글인지?
        if ( userId == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }
        // 저자가 같은 지 검사한다.
        if ( userId != userService.getLoginUserId()){
            throw new ForbiddenException(ErrorMessage.FORBIDDEN_EXCEPTION);
        }
        article.setId(id);
        // region 체크
        this.regionCheck(article.getRegion());
        // image가 없는 경우 default 이미지를 섬네일에 넣어준다
        if ( article.getImages() == null || article.getImages().size() == 0 ){
            article.setThumbnail(defaultArticleImage);
        }
        else{
            // 섬네일을 최종 가장 첫 사진으로 변경한다
            article.setThumbnail(article.getImages().get(0));
        }

        // 사용중인 url에 대해서 s3_url 테이블에 있는 url을 모두 미사용 처리한다.
        Article tmp = new Article();
        tmp.setImages(communityMapper.getImageUrls(article.getId()));
        communityMapper.initS3UrlHistory(tmp);
        //db에 가지고있던 url들을 모두 지운다
        communityMapper.initImages(id);

        // article update
        // 사진들 다시 insert
        // 입력받은 url에 대해서 s3_url테이블 해당 url 사용처리
        // 이후 0이된 찌꺼기 이미지는 스케쥴러가 수거한다.
        communityMapper.completePostArticle(article);

        return new BaseResponse("수정이 완료되었습니다", HttpStatus.OK);
    }

    @Override
    public BaseResponse deleteArticle(Long id){

        // 해당 게시글이 실제로 있는지 검사한다.
        Long targetArticle = communityMapper.getTargetArticle(id);
        if ( targetArticle == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }

        // 저자가 같은 지 검사한다.
        if ( communityMapper.getArticleAuthor(id) != userService.getLoginUserId()){
            throw new RequestInputException(ErrorMessage.NOT_AUTHOR);
        }


        //해당 게시글의 s3_upload 테이블 url 사용안함처리 --> 이후 사용안함인 image 스케쥴러에서 수거함.
        Article article = new Article();
        article.setImages(communityMapper.getImageUrls(id));
        communityMapper.initS3UrlHistory(article);

        
        // 해당 article과 해당 article에게 속해있는 모든 image hard delete
        // 댓글, 이미지, 본문 soft delete
        communityMapper.hardDeleteArticle(id);
        return new BaseResponse("삭제가 완료되었습니다." ,HttpStatus.OK);
    }


    // Article에 달린 comment를 조회한다.
    public List<ArticleComment> getComment(Long id){

        // 없는 article에 대한 comment를 요구하는 경우
        Long target = communityMapper.getTargetArticlePosted(id);
        if ( target == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }

        return communityMapper.getArticleComment(id);
    }

    public BaseResponse postComment(ArticleComment articleComment, Long id){
        // 댓글을 달 target article id set
        articleComment.setArticle_id(id);

        //댓글을 달 target article이 존재하는 지 확인
        Long target = communityMapper.getTargetArticlePosted(id);
        if ( target == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }

        // 로그인한 유저 set
        Long userId = userService.getLoginUserId();
        articleComment.setUser_id(userId);

        communityMapper.postComment(articleComment);
        return new BaseResponse("댓글이 작성되었습니다", HttpStatus.CREATED);
    }

    @Override
    public BaseResponse deleteComment(Long id, Long commentId){

        //해당 게시글 있는지 검사한다.
        Long targetArticle = communityMapper.getTargetArticlePosted(id);
        if ( targetArticle == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_NOT_EXIST);
        }
        //해당 댓글이 있는지 검사한다.
        Long author = communityMapper.getAuthorComment(id, commentId);
        if ( author == null){
            throw new RequestInputException(ErrorMessage.TARGET_ARTICLE_COMMENT_NOT_EXIST);
        }

        //저자가 같은기 검사한다
        Long userId = userService.getLoginUserId();
        if ( userId != author ){
            throw new RequestInputException(ErrorMessage.NOT_AUTHOR);
        }
        communityMapper.softDeleteComment(id, commentId );
        return new BaseResponse("댓글이 삭제되었습니다", HttpStatus.OK);
    }

    @Override
    public List<Article> getArticlesByBoardIdAndUserId(Long boardId) {
        return communityMapper.getArticlesByBoardIdAndUserId(userService.getLoginUserId(),boardId);
    }
}
