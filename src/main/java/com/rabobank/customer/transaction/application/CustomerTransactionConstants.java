package com.rabobank.customer.transaction.application;

/**
 * This class holds constants for the application.
 * 
 * @author Bharathi
 *
 */
public class CustomerTransactionConstants {

	public static final String DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss";
	public static final String SEMICOLON_DELIMITER = ";";
	public static final String OUTPUT_FILE_EXT = ".csv";
	public static final String TEMP_DIR = "java.io.tmpdir";
	public static final String JOB_PARAM1 = "fileName";
	public static final String JOB_PARAM2 = "currentTime";
	public static final String XSD_FILE = "records.xsd";
	public static final String XML_EXT = "xml";
	public static final String CSV_EXT = "csv";
	public static final String COMMA_DELIMITER = ",";

	public static final String FILE_HEADERS = "Reference;Description";

	public static final String OUTPUT_FILE_NAME_SUFFIX = "CUST_TRANS_REPORT_";

	public static final String CUST_TRANS_JOB_URI = "http://localhost:8080/run-transStmt-Job/";

	public static final String REFERENCEIDS = "referenceIds";
	public static final double ROUND_TWO_DIGIT_DECIMAL = 100.0;

	public static final int COL_REFERENCE = 0;
	public static final int COL_ACCTNUMBER = 1;
	public static final int COL_DESCRIPTION = 2;
	public static final int COL_STARTBAL = 3;
	public static final int COL_MUTATION = 4;
	public static final int COL_ENDBAL = 5;

}
