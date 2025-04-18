package com.hcl.book.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String fileName = jobExecution.getJobParameters().getString("fileName");
        jobExecution.getExecutionContext().putString("fileName", fileName);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        
    }
}
