package com.rabobank.customer.transaction.batch.reader;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;
import com.rabobank.customer.transaction.exception.CustomerTransactionException;
import com.rabobank.customer.transaction.model.CustomerTransactionDetail;

/**
 * This class loads a input file and create a list of CustomerTransactionDetail objects using helper classes. 
 * The duplicate transaction ids and its count are stored at step context scope for validation in the processor.
 */

@Service
@StepScope
public class TransactionReader implements ItemReader<CustomerTransactionDetail> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionReader.class);

	private int nexttIndex = 0;
	private List<CustomerTransactionDetail> customerTransactions;

	@Value("#{stepExecution}")
	private StepExecution stepExecution;
	
	@Value("#{jobParameters['fileName']}")
	private String fileName;

	@Autowired
	private CSVReader csvReaderHelper;

	@Autowired
	private XMLReader xmlReaderHelper;	

	@Override
	public CustomerTransactionDetail read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(nexttIndex == 0) {
			loadFile();
		}
		if (customerTransactions != null && nexttIndex < customerTransactions.size()) {
			return customerTransactions.get(nexttIndex++);
		} else {
			return null;
		}
	}
	
	private void loadFile() {
		ExecutionContext stepContext = stepExecution.getExecutionContext();
		// Reading the incoming file (csv/xml)
		File file = new File(System.getProperty(CustomerTransactionConstants.TEMP_DIR)+ File.separator + fileName);
		if(!file.exists()) {
			throw new CustomerTransactionException(String.format("File %s not found", file.getName()));
		}
		if(!CustomerTransactionConstants.CSV_EXT.equalsIgnoreCase(FilenameUtils.getExtension(file.getName())) 
				&& !CustomerTransactionConstants.XML_EXT.equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))) {
			throw new CustomerTransactionException(String.format("Invalid file for processing %s", FilenameUtils.getExtension(file.getName())));
		}
		if (CustomerTransactionConstants.CSV_EXT.equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))) {
			customerTransactions = csvReaderHelper.readLines(file);
		} else {
			customerTransactions = xmlReaderHelper.readLines(file);
		}
		nexttIndex = 0;
		// Create List for holding Statement objects
		if (!CollectionUtils.isEmpty(customerTransactions)) {
			LOGGER.info("Processing {} records", customerTransactions.size());
			Map<String, Long> referenceIds = customerTransactions.stream()
					.collect(Collectors.groupingBy(CustomerTransactionDetail::getReference, Collectors.counting()));
			stepContext.put("referenceIds", referenceIds);
		}
	}

	public int getNextIndex() {
		return nexttIndex;
	}	

}
