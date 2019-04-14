package com.rabobank.customer.transaction.batch.reader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;
import com.rabobank.customer.transaction.exception.CustomerTransactionException;
import com.rabobank.customer.transaction.model.CustomerTransactionDetail;
import com.rabobank.customer.transaction.model.CustomerTransactionDetails;

/**
 * Helper class for validating the xnl file and parsing xml file to convert a list of CustomerTransactionDetail objects.
 *  
 */
@Component("xmlReader")
public class XMLReader implements TransactionLineReader<CustomerTransactionDetail> {

	@Override
	public List<CustomerTransactionDetail> readLines(File file) {
		// Check if the file exists
		List<CustomerTransactionDetail> customerTransactions = null;
		CustomerTransactionDetails customerTransactionDetails = null;
		// Create List for holding Statement objects
		JAXBContext jaxbContext = null;
		validate(file, file.getName());
		try {
			jaxbContext = JAXBContext.newInstance(CustomerTransactionDetails.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			customerTransactionDetails = (CustomerTransactionDetails) jaxbUnmarshaller.unmarshal(file);
			if (customerTransactionDetails != null && customerTransactionDetails.getTransactions() != null) {
				customerTransactions = customerTransactionDetails.getTransactions();
			}

		} catch (JAXBException excep) {
			throw new CustomerTransactionException(
					String.format("Error while exporting objects from %s", file.getName()), excep);
		}

		return customerTransactions;
	}

	private void validate(File file, String fileName) {

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			Schema xsdScheme = factory.newSchema(new File(classLoader.getResource(CustomerTransactionConstants.XSD_FILE).getFile()));
			Validator validator = xsdScheme.newValidator();
			Source source = new StreamSource(file);
			validator.validate(source);
		} catch (SAXException | IOException excep) {
			new CustomerTransactionException(String.format("Error while parsing the file %s", fileName), excep);
		}
	}

}
