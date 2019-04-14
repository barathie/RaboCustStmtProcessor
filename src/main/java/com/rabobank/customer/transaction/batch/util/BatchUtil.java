package com.rabobank.customer.transaction.batch.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;

/**
 * Util class to handle miscellaneous activities.
 *
 */

@Component
public class BatchUtil {
	
    private static final Logger LOGGER =
            LoggerFactory.getLogger(BatchUtil.class);

	public  String parseDate(Date date) {
		String currentTime = null;
		try {
			
			date = date == null?new Date():date;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CustomerTransactionConstants.DATE_FORMAT);
			currentTime = simpleDateFormat.format(date);
			
		} catch (Exception excep) {
			LOGGER.error("Error while formatting the date {}", date);
		}
		return currentTime;
	}

}
