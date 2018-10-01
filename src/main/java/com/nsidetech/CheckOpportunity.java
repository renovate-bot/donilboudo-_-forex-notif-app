package com.nsidetech;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CheckOpportunity {
    public static void main(String[] args) throws Exception {

        JobDetail job = JobBuilder.newJob(CheckOpportunityJob.class)
                .withIdentity("dummyJobName", "group1").build();

        // Trigger the job to run on the next round minute
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10).repeatForever())
                .build();

        // schedule it
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

    }
}