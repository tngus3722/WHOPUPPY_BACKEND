package com.whopuppy.util;

import javax.annotation.PostConstruct;

import org.quartz.*;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class SampleJobExecutor {
    // quartz
    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;

    @PostConstruct
    public void start() throws SchedulerException {
        schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        // job
        JobDetail job = JobBuilder.newJob(EventScheduler.class).withIdentity("testJob").build();

        // trigger every 12 o'clock
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/12 * * ?")).build();

        scheduler.scheduleJob(job, trigger);
    }
}