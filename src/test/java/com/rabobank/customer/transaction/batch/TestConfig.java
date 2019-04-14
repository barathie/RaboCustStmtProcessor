package com.rabobank.customer.transaction.batch;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabobank.customer.transaction.batch.listener.StepErrorLoggingListener;
import com.rabobank.customer.transaction.batch.processor.TransactionProcessor;
import com.rabobank.customer.transaction.batch.reader.TransactionReader;
import com.rabobank.customer.transaction.batch.util.BatchUtil;
import com.rabobank.customer.transaction.batch.writer.TransactionWriter;



@Configuration
public class TestConfig {
	
	@Mock
	@InjectMocks
	private TransactionWriter transactionWriter;
	 
	@Mock
	private TransactionReader transactionReader;
	
	@Mock
	private TransactionProcessor transactionProcessor;
	
	@Mock
	private StepErrorLoggingListener stepErrorLoggingListener;
	
	
	@Mock
	private StepExecution stepExecution;
	
	@Mock
	private BatchUtil batchUtil;
	
	
	 
	@Mock
	protected JobParameters jobParams;
	
	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new JobLauncherTestUtils();
	}
}
