package com.whopuppy.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AnimalMapper {
    Integer insertAnimal(Map<String, Object> map);
}
