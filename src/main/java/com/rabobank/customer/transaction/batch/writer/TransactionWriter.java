package com.rabobank.customer.transaction.batch.writer;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;
import com.rabobank.customer.transaction.batch.util.BatchUtil;
import com.rabobank.customer.transaction.model.CustomerTransactionDetail;

/**
 * 
 *
 */

@Service
@StepScope
public class TransactionWriter extends FlatFileItemWriter<CustomerTransactionDetail> {

	@Autowired
	private BatchUtil batchUtil;

	public TransactionWriter() {
		super();
		// All job repetitions should "append" to same output file
		setAppendAllowed(true);
		// Name field values sequence based on object properties
		setLineAggregator(createCustomerLineAggregator());
		FileHeaderWriter headerWriter = new FileHeaderWriter(CustomerTransactionConstants.FILE_HEADERS);
		setHeaderCallback(headerWriter);

	}

	/**
	 * This method constructs the filename for the output to be written and registers with the writer. 
	 */
	@PostConstruct
	public void initialize() {
		StringBuilder fileNameWithExt = new StringBuilder(System.getProperty(CustomerTransactionConstants.TEMP_DIR));
		fileNameWithExt.append(File.separator);
		fileNameWithExt.append(CustomerTransactionConstants.OUTPUT_FILE_NAME_SUFFIX);
		fileNameWithExt.append(batchUtil.parseDate(null));
		fileNameWithExt.append(CustomerTransactionConstants.OUTPUT_FILE_EXT);
		Resource resource = new FileSystemResource(fileNameWithExt.toString());
		setResource(resource);
	}

	private LineAggregator<CustomerTransactionDetail> createCustomerLineAggregator() {
		DelimitedLineAggregator<CustomerTransactionDetail> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(CustomerTransactionConstants.SEMICOLON_DELIMITER);
		FieldExtractor<CustomerTransactionDetail> fieldExtractor = createCustomerFieldExtractor();
		lineAggregator.setFieldExtractor(fieldExtractor);
		return lineAggregator;
	}

	/**
	 * This method extracts the value of given field names.
	 * 
	 * @return
	 */
	private FieldExtractor<CustomerTransactionDetail> createCustomerFieldExtractor() {
		BeanWrapperFieldExtractor<CustomerTransactionDetail> extractor = new BeanWrapperFieldExtractor<>();
		extractor.setNames(new String[] { "Reference", "Description" });
		return extractor;
	}
}
