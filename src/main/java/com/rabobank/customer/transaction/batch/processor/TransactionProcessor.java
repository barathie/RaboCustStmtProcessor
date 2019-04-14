package com.rabobank.customer.transaction.batch.processor;

import java.util.Map;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.rabobank.customer.transaction.application.CustomerTransactionConstants.REFERENCEIDS;
import static com.rabobank.customer.transaction.application.CustomerTransactionConstants.ROUND_TWO_DIGIT_DECIMAL;
import com.rabobank.customer.transaction.model.CustomerTransactionDetail;

/**
 * The processor validates the customer transaction  
 * 1. Duplicate transaction ids
 * 2. Mutation not equal to end balance -start balance.
 *
 */

@Service
@StepScope
public class TransactionProcessor implements ItemProcessor<CustomerTransactionDetail, CustomerTransactionDetail> {


	@Value("#{stepExecution}")
	private StepExecution stepExecution;

	@SuppressWarnings("unchecked")
	@Override
	public CustomerTransactionDetail process(CustomerTransactionDetail customerTransactionDetail) throws Exception {
		boolean isValid = true;
		isValid = checkBalance(customerTransactionDetail);
		if (isValid) {
			ExecutionContext stepContext = stepExecution.getExecutionContext();
			if (stepContext.get(REFERENCEIDS) != null) {
				Map<String, Long> values = (Map<String, Long>) stepContext.get(REFERENCEIDS);
				Long totalSize = (Long) values.get(customerTransactionDetail.getReference());
				isValid = totalSize > 1 ? false : true;
			}
		}
		if (!isValid) {
			return customerTransactionDetail;
		} else {
			return null;
		}
	}

	private boolean checkBalance(CustomerTransactionDetail customerTransactionDetail) {
		if (!(Math
				.round((Double.parseDouble(customerTransactionDetail.getStartBalance())
						+ Double.parseDouble(customerTransactionDetail.getMutation())) * ROUND_TWO_DIGIT_DECIMAL)
				/ ROUND_TWO_DIGIT_DECIMAL == Double.parseDouble(customerTransactionDetail.getEndBalance()))) {
			return false;
		}
		return true;

	}
}
