package com.rabobank.customer.transaction.batch;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rabobank.customer.transaction.batch.Job.CustomerTransactionReportJob;
import com.rabobank.customer.transaction.batch.listener.StepErrorLoggingListener;
import com.rabobank.customer.transaction.batch.processor.TransactionProcessor;
import com.rabobank.customer.transaction.batch.reader.CSVReader;
import com.rabobank.customer.transaction.batch.reader.TransactionReader;
import com.rabobank.customer.transaction.batch.reader.XMLReader;
import com.rabobank.customer.transaction.batch.util.BatchUtil;
import com.rabobank.customer.transaction.batch.writer.TransactionWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TransactionWriter.class, TransactionReader.class, TransactionProcessor.class,
		StepErrorLoggingListener.class, BatchUtil.class, CSVReader.class, XMLReader.class,
		CustomerTransactionReportJob.class, TestConfig.class })
public class CustomerTransactionTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@InjectMocks
	private CustomerTransactionReportJob customerTransactionReportJob = new CustomerTransactionReportJob();

	@Autowired
	private Job failedCustomerTransactionsJob;

	@Before
	public void setup() {
		String targetPath = System.getProperty("java.io.tmpdir") + File.separator + "records.xml";
		String sourcePath = System.getProperty("user.dir") + File.separator + "records.xml";
		Path path = Paths.get(targetPath);
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(sourcePath));
			Files.write(path, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testJobWithNonExistingFile() throws Exception {
		// testing a job
		JobExecution jobExecution = jobLauncherTestUtils.getJobLauncher().run(failedCustomerTransactionsJob,
				getJobParameters("invalidFile.csv"));
		assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
	}

	@Test
	public void testJobWithInvalidExt() throws Exception {
		// testing a job
		JobExecution jobExecution = jobLauncherTestUtils.getJobLauncher().run(failedCustomerTransactionsJob,
				getJobParameters("inavlidExt.rar"));
		assertEquals(BatchStatus.FAILED, jobExecution.getStatus());

	}

	@Test
	public void testJob() throws Exception {
		// testing a job
		JobExecution jobExecution = jobLauncherTestUtils.getJobLauncher().run(failedCustomerTransactionsJob,
				getJobParameters("records_upload.xml"));
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	private JobParameters getJobParameters(String param) {
		JobParametersBuilder jobParameters = new JobParametersBuilder();
		jobParameters.addString("fileName", param);
		return jobParameters.toJobParameters();
	}

}
