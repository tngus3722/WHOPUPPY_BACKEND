package com.whopuppy.controller;

import com.whopuppy.service.AnimalService;
import io.swagger.annotations.ApiOperation;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping(value = "/animal")
public class AnimalController {

    /***
     * 백민수
     */
    @Resource
    AnimalService animalService;

    @PostMapping
    @ApiOperation(value = "유기견 리스트 입력")
    public ResponseEntity saveAnimalList() throws IOException, ParseException {
        return animalService.insertAnimalList();
    }
}
