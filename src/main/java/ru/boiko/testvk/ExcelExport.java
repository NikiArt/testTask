package ru.boiko.testvk;

import au.com.bytecode.opencsv.CSVWriter;
import lombok.SneakyThrows;
import java.io.FileWriter;

public class ExcelExport {
    private static String CSV = "gephi.csv";
    private CSVWriter writer;

    @SneakyThrows
    public ExcelExport() {
        writer = new CSVWriter(new FileWriter(CSV));
        final String [] record = "source,target".split(",");
        writer.writeNext(record);
        writer.close();
    }

    @SneakyThrows
    public void writeString(String dependValue) {
        writer = new CSVWriter(new FileWriter(CSV, true));
        final String [] record = dependValue.split(",");
        writer.writeNext(record);
        writer.close();
        System.out.println("Wrote: " + dependValue);
    }
}
