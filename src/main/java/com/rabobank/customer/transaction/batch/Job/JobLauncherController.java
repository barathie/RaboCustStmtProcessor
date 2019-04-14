package com.rabobank.customer.transaction.batch.Job;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;

/**
 * This REST service facilitates triggering of the batch job failedCustomerTransactionsJob.
 * failedCustomerTransactionsJob(job instance) = failedCustomerTransactionsJob(job) + job params.
 * filename and current time are two job params
 *
 */
@RestController
public class JobLauncherController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("failedCustomerTransactionsJob")
	private Job failedCustomerTransactionsJob;

	@RequestMapping("/run-transStmt-Job/{fileName}")
	public String handle(@PathVariable("fileName") String fileName) throws Exception {

		JobParameters jobParameters = new JobParametersBuilder().addString(CustomerTransactionConstants.JOB_PARAM1, fileName)
				.addDate(CustomerTransactionConstants.JOB_PARAM2, new Date())
				.toJobParameters();
		jobLauncher.run(failedCustomerTransactionsJob, jobParameters);

		return "Batch job has been invoked";
	}

}
