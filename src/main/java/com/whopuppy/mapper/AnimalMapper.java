package com.whopuppy.mapper;

import com.whopuppy.domain.AnimalDTO;
import com.whopuppy.domain.criteria.AnimalListCriteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnimalMapper {
    Integer insertAnimal(Map<String, Object> map);
    List<AnimalDTO> searchAnimal(AnimalListCriteria animalListCriteria);
    AnimalDTO selectAnimal(Long idx);
    Long findById(Long idx);
}
