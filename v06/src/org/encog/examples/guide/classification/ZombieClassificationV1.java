package org.encog.examples.guide.classification;

import java.io.File;

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
import java.util.Scanner;

public class ZombieClassificationV1 {

    public String ler_caminho(){
        System.out.println("Informe o nome do arquivo: ");
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();

        return path;
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

    public void doutorInteligente(String caminho) {
        try {
            //this.caminhoArq = ler_caminho();
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

            for(int clear = 0; clear < 10; clear++) {
                System.out.println("\b") ;
            }

            String[] linee = {"0.0", "1.0", "1.0", "0.0", "0.0", "0.0", "1.0"};
            String correct = csv.get(attributes.length-1);
            helper.normalizeInputVector(linee, input.getData(), false);
            MLData output = bestMethod.compute(input);
            String docChosen = helper.denormalizeOutputVectorToString(output)[0];
            System.out.println(correct + "  +  " + docChosen);

            // Delete data file ande shut down.
            doctorFile.delete();
            Encog.getInstance().shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZombieClassificationV1 doutor = new ZombieClassificationV1();
        String arquivo = doutor.ler_caminho();
        System.out.println(arquivo);
        doutor.doutorInteligente(arquivo);
    }

}