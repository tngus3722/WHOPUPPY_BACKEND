package com.whopuppy.service;

import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.domain.snack.SnackImage;
import com.whopuppy.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnackService {
    BaseResponse postArticle(SnackArticle snackArticle);
    String uploadImage(MultipartFile multipartFile) throws Exception;
    List<SnackArticle> getAllSnackArticles(BaseCriteria baseCriteria);
    List<String> getImagesFromContent(String value);
}
