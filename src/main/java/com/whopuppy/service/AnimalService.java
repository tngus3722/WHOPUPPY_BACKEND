package com.whopuppy.service;

import com.whopuppy.domain.criteria.AnimalListCriteria;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface AnimalService {
    void insertAnimalJson(List animalDTOList);
    ResponseEntity insertAnimalList() throws IOException, ParseException, URISyntaxException;
    ResponseEntity searchAnimal(AnimalListCriteria animalListCriteria) throws Exception;
}
