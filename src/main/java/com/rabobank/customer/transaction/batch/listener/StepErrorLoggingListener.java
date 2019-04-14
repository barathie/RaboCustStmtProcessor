package com.rabobank.customer.transaction.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;


/**
 * This class helps to track the unsuccessful completion of the step through the logger.
 *  
 */
@Component
public class StepErrorLoggingListener implements StepExecutionListener {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StepErrorLoggingListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // do nothing.
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> exceptions = stepExecution.getFailureExceptions();
        if (exceptions.isEmpty()) {
            return ExitStatus.COMPLETED;
        }

        LOGGER.info("The Step has the following exceptions [step-name:{}] [size:{}]", stepExecution.getStepName(), exceptions.size());
        exceptions.forEach(ex -> LOGGER.error("Exception Occured ", ex));
        return ExitStatus.FAILED;
    }
}  