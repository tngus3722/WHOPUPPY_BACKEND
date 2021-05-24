package com.whopuppy.util;

import com.whopuppy.controller.AnimalController;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class EventScheduler implements Job {

    @Resource
    AnimalController animalController;

    static Integer count = 0;
    /**
     * Logger.
     */
    private static Logger logger = LoggerFactory.getLogger(EventScheduler.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //Save animal api data
        //every 12 o'clock

        try {
            animalController.saveAnimalList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logger.info("scheduler : " + count++);
    }
}