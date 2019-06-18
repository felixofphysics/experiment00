package org.encog.examples.guide.classification;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

import javax.xml.crypto.Data;


public class SmartDoctor implements IDoctor {
    private int patientN = 0;

    private ITableProducer producer;
    private IResponder responder;

    public void connect(ITableProducer producer) {
        this.producer = producer;
    }

    public void connect(IResponder responder) {
        this.responder = responder;
    }

    public File importData(String path) {
        File doctorFile = new File(path);

        return doctorFile;
    }

    public DataSetComponent dadosPlanilha(String path) {
        DataSetComponent dataset = new DataSetComponent();
        dataset.setDataSource(path);

        return dataset;
    }

    public void doutorInteligente(String caminho, ArrayList<String> sintomas) {
        try {
            CsvRewrite lerDados = new CsvRewrite();
            lerDados.rewriteData(caminho);
            File doctorFile = importData("src/newDataSet.csv");
            DataSetComponent dataset = dadosPlanilha(caminho);

            VersatileDataSource source = new CSVDataSource(doctorFile, false, CSVFormat.DECIMAL_POINT);

            VersatileMLDataSet data = new VersatileMLDataSet(source);

            String attributes[] = dataset.requestAttributes();

            for (int a = 0; a < attributes.length-1; a++) {
                data.defineSourceColumn(attributes[a], a, ColumnType.continuous);
            }

            ColumnDefinition outputColumn = data.defineSourceColumn("diagnostic", attributes.length-1, ColumnType.nominal);

            data.analyze();

            data.defineSingleOutputOthersInput(outputColumn);

            EncogModel model = new EncogModel(data);
            model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);

            model.setReport(new ConsoleStatusReportable());

            data.normalize();

            model.holdBackValidation(0.3, true, 1001);

            model.selectTrainingType(data);

            System.out.println("--------------------------- consulting table of diseases ---------------------------");

            MLRegression bestMethod = (MLRegression) model.crossvalidate(5, true);


            NormalizationHelper helper = data.getNormHelper();

            ReadCSV csv = new ReadCSV(doctorFile, false, CSVFormat.DECIMAL_POINT);
            String[] line = new String[attributes.length-1];
            MLData input = helper.allocateInputVector();

            while (csv.next()) {
                StringBuilder result = new StringBuilder();
                for (int a = 0; a < attributes.length-1; a++) {
                    line[a] = csv.get(a);
                }
                String correct = csv.get(attributes.length-1);
                helper.normalizeInputVector(line, input.getData(), false);
                MLData output = bestMethod.compute(input);
                String docChosen = helper.denormalizeOutputVectorToString(output)[0];
            }
            /*

            for(int clear = 0; clear < 10; clear++) {
                System.out.println("\b") ;
            }
            */

            String linee[] = new String[sintomas.size()];

            for (int a = 0; a < sintomas.size(); a++) {
                linee[a] = sintomas.get(a);
            }
            String correct = csv.get(attributes.length-1);
            helper.normalizeInputVector(linee, input.getData(), false);
            MLData output = bestMethod.compute(input);
            String docChosen = helper.denormalizeOutputVectorToString(output)[0];

            System.out.println("--------------------------- end of consultation ---------------------------");

            for (int a = 0; a < attributes.length-1; a++) {
                System.out.println("Question: " + responder.ask(attributes[a]));
            }

            System.out.println("Disease diagnosis: " + docChosen);
            boolean result = responder.finalAnswer(docChosen);
            System.out.println("Result: " + ((result) ? "I am right =)" : "I am wrong =("));

            // Delete data file ande shut down.
            doctorFile.delete();
            Encog.getInstance().shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startInterview() {
        String attributes[] = producer.requestAttributes();
        String instances[][] = producer.requestInstances();
        ArrayList<String> sintomas = new ArrayList<String>();

        //System.out.println("Doctor guess: " + patientN);

        for (int a = 0; a < attributes.length-1; a++) {
            //System.out.println("Question: " + responder.ask(attributes[a]));
            if ((responder.ask(attributes[a])).equals("yes"))
                sintomas.add("1.0");
            else
                sintomas.add("0.0");
        }
        doutorInteligente(((IDataSet) producer).getDataSource(), sintomas);
    }

    /*public static void main(String[] args) {
        ZombieClassificationV1 doutor = new ZombieClassificationV1();
        String arquivo = doutor.ler_caminho();
        System.out.println(arquivo);
        doutor.doutorInteligente(arquivo);
    }*/
}
