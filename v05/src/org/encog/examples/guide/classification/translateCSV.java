package org.encog.examples.guide.classification;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class translateCSV {
    public static void rewriteData(){
        try {
            CSVReader reader = new CSVReader(new FileReader("src/old3.csv"));
            CSVWriter writer = new CSVWriter(new FileWriter("src/crazyMA.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
            String[] nextLine;
            int ind = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (ind == 0) {
                    ind++;
                    continue;
                }
                List<String> lineAsList = new ArrayList<String>(Arrays.asList(nextLine));
                // Add stuff using linesAsList.add(index, newValue) as many times as you need.
                int cont = 0;
                for (String nome : lineAsList) {
                    //System.out.println(nome);
                    if (nome.equals("t") || nome.equals("T")) {
                        lineAsList.set(cont, "1.0");
                    }
                    else if (nome.equals("f") || nome.equals("F")) {
                        lineAsList.set(cont, "0.0");
                    }
                    cont++;
                }
                String[] data = new String[lineAsList.size()];

                // ArrayList to Array Conversion
                for (int i = 0; i < lineAsList.size(); i++) {
                    data[i] = lineAsList.get(i);
                    System.out.println(data[i]);
                }

                writer.writeNext(data);
            }
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        rewriteData();
    }
}
