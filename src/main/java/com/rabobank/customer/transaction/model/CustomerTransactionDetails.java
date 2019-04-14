package com.rabobank.customer.transaction.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "records")
@XmlAccessorType (XmlAccessType.FIELD)
public class CustomerTransactionDetails {
	
	 @XmlElement(name = "record")
    private List<CustomerTransactionDetail> transactions = null;
 
   
    public List<CustomerTransactionDetail> getTransactions() {
        return transactions;
    }
 
    public void setTransactions(List<CustomerTransactionDetail> transactions) {
        this.transactions = transactions;
    }

}
