package com.whopuppy.util;

import com.whopuppy.service.AnimalService;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class EventScheduler{

    @Autowired
    private AnimalService animalService;

    static Integer count = 0;
    private static Logger logger = LoggerFactory.getLogger(EventScheduler.class);

    //every 12'o clock
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void execute() throws IOException, ParseException, URISyntaxException {
        animalService.insertAnimalList();
        logger.info("scheduler : " + count++);
    }
}