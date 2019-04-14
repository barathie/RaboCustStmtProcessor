package com.rabobank.customer.transaction.batch.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.rabobank.customer.transaction.batch.listener.StepErrorLoggingListener;
import com.rabobank.customer.transaction.batch.processor.TransactionProcessor;
import com.rabobank.customer.transaction.batch.reader.TransactionReader;
import com.rabobank.customer.transaction.batch.writer.TransactionWriter;
import com.rabobank.customer.transaction.model.CustomerTransactionDetail;

/**
 * This class holds the configuration of a spring batch job and it enables the processing the batch job.
 * 
 * @author Bharathi Elumalai
 *
 */
@Component
@EnableBatchProcessing
public class CustomerTransactionReportJob extends JobExecutionListenerSupport {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerTransactionReportJob.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private TransactionWriter transactionWriter;

	@Autowired
	private TransactionReader transactionReader;

	@Autowired
	private TransactionProcessor transactionProcessor;
	
	@Autowired
	private StepErrorLoggingListener stepErrorLoggingListener;

	@Bean(name = "failedCustomerTransactionsJob")
	public Job failedCustomerTransactionsJob() {
		Step step = stepBuilderFactory.get("step-1")
				.<CustomerTransactionDetail, CustomerTransactionDetail>chunk(10)
				.reader(transactionReader)
				.processor(transactionProcessor)
				.writer(transactionWriter)
				.listener(stepErrorLoggingListener)
				.build();

		Job job = jobBuilderFactory
				.get("failedCustomerTransactions-job")
				.incrementer(new RunIdIncrementer())
				.listener(this)
				.start(step)
				.build();

		return job;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("BATCH JOB COMPLETED SUCCESSFULLY");	
		} else {
			LOGGER.info("BATCH JOB FAILED");
		}
	}

}
