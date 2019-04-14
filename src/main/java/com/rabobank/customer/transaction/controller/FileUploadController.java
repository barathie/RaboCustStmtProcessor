package com.rabobank.customer.transaction.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;

/*
 * This controller provides capabilities to upload a file with  MT940 file
*/
@Controller
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @GetMapping("/")
    public String index() {
        LOGGER.info("Index Page invoked...............");
        return "fileUpload";
    }   

    @RequestMapping(value = "/fileUpload", method = {RequestMethod.GET, RequestMethod.POST})
    public String customerTransactionsUpload(HttpSession session, @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        LOGGER.info("Inside the customerTransactionsUpload");
        if (file.isEmpty()) {
            LOGGER.error("File name is empty");
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:fileUploadStatus";
        }

        if( file.getOriginalFilename().endsWith("csv") ||
                file.getOriginalFilename().endsWith("xml")) {
        	String destination = System.getProperty(CustomerTransactionConstants.TEMP_DIR)+File.separator;
        	try {
        		byte[] bytes = file.getBytes();
                Path path = Paths.get(destination + file.getOriginalFilename());
                Files.write(path, bytes);
				redirectAttributes.addFlashAttribute("message", "File Upload Status successfull");
				redirectAttributes.addFlashAttribute("triggerOption", "Yes or No");
				session.setAttribute("fileName", file.getOriginalFilename());
			} catch (IllegalStateException | IOException e) {
	            redirectAttributes.addFlashAttribute("message", "The file has errors. Please upload a valid file");
				LOGGER.error("Error while uploading the file {} ", file.getName());
			}
        }
        else {
            LOGGER.error("Invalid file type uploaded");
            redirectAttributes.addFlashAttribute("message",
                    "Invalid file type uploaded '" + file.getOriginalFilename() + "'");
        }
        return "redirect:/fileUploadStatus";
    }
    
    @RequestMapping(value = "/triggerJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String triggerCustomerTransactonJob(HttpSession session, RedirectAttributes redirectAttributes) {
        LOGGER.info("Inside the triggerCustomerTransactonJob");
        redirectAttributes.addFlashAttribute("message", "The file is processed");
        final String uri = CustomerTransactionConstants.CUST_TRANS_JOB_URI+session.getAttribute("fileName");
        LOGGER.info("Calling the URI : {}", uri);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return "redirect:/fileUploadStatus";
    }    

    @GetMapping("/fileUploadStatus")
    public String fileUploadStatus() {
        LOGGER.info("Inside the statement upload status method");
        return "fileUploadStatus";
    }
}