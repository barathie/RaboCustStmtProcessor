package com.rabobank.customer.transaction.batch.writer;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

/**
 * This class is to construct a header line for the output csv. 
 *
 */
public class FileHeaderWriter implements FlatFileHeaderCallback {
 
    private final String header;
 
    FileHeaderWriter(String header) {
        this.header = header;
    }
 
    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write(header);
    }
}