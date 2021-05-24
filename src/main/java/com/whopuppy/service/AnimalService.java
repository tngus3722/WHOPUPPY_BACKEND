package com.whopuppy.service;

import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

public interface AnimalService {
    void insertAnimalJson(List animalDTOList);
    ResponseEntity insertAnimalList() throws IOException, ParseException;
}
