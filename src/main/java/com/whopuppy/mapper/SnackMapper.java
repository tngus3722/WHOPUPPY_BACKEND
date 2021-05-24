package com.whopuppy.mapper;

import com.whopuppy.domain.criteria.BaseCriteria;
import com.whopuppy.domain.snack.SnackArticle;
import com.whopuppy.domain.snack.SnackImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SnackMapper {

    Long uploadImage(String url);
    void insertImage(Long id, List<String> images);
    Long postArticle(SnackArticle snackArticle);
    List<SnackArticle> getAllSnackArticles(BaseCriteria baseCriteria);
}
