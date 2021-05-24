package com.whopuppy.serviceImpl;

import com.amazonaws.services.xray.model.Http;
import com.whopuppy.domain.community.ArticleImage;
import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.domain.snack.SnackImage;
import com.whopuppy.enums.ErrorMessage;
import com.whopuppy.exception.RequestInputException;
import com.whopuppy.mapper.SnackMapper;
import com.whopuppy.response.BaseResponse;
import com.whopuppy.service.SnackService;
import com.whopuppy.service.UserService;
import com.whopuppy.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service
public class SnackServiceImpl implements SnackService {
    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    private SnackMapper snackMapper;
    @Autowired
    private S3Util s3Util;
    @Value("${default.article.image}")
    private String defaultArticleImage;
    


    //게시글에 업로드하기 위한 이미지의 링크를 반환한다
    @Override
    public String uploadImage(MultipartFile multipartFile) throws Exception{
        // multipartfile이 null인 경우
        if ( multipartFile == null){
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NULL);
        }
        //multipartfile의 content type이 jpeg, png가 아닌경우
        if( !multipartFile.getContentType().equals("image/jpeg") && !multipartFile.getContentType().equals("image/png"))
            throw new RequestInputException(ErrorMessage.MULTIPART_FILE_NOT_IMAGE);

        String url = s3Util.uploadObject(multipartFile);

        // 추후 s3에서 최종 게시글 업로드 되지 않은 이미지를 삭제할 수 있도록 이력을 관리한다.
        snackMapper.uploadImage(url);
        return url;
    }

    //게시글을 완성한다
    @Override
    public BaseResponse postArticle(SnackArticle snackArticle){

        //본문의 삽입된 이미지 추출
        List<String> imgList = this.getImagesFromContent(snackArticle.getContent());

        //이미지가 옴따면 default 이미지를 넣어준다
        if (imgList == null || imgList.size() == 0) {
            snackArticle.setThumbnail(defaultArticleImage);
        }else{
            //있다면 제일 처음 이미지를 섬네일로 잡는다.
            snackArticle.setThumbnail(imgList.get(0));
        }
        snackArticle.setUser_id(userService.getLoginUserId());


        // 섬네일 등록
        snackArticle.setThumbnail(imgList.get(0));
        
        //내용삽입
        Long id = snackMapper.postArticle(snackArticle);
        // 이미지들 삽입
        snackMapper.insertImage(id, imgList);
        return new BaseResponse("게시글이 작성되었습니다", HttpStatus.CREATED);
    }

    // 게시글의 img 태그의 src="" 내용을 추출한다
    public List<String> getImagesFromContent(String value){
        Pattern nonValidPattern = Pattern
                .compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

        List<String> result = new ArrayList();
        Matcher matcher = nonValidPattern.matcher(value);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    @Override
    public List<SnackArticle> getAllSnackArticles(BaseCriteria baseCriteria){
        return snackMapper.getAllSnackArticles(baseCriteria);
    }
}
