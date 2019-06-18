package org.encog.examples.guide.classification;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.bot.BotUtil;
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
import org.encog.util.simple.EncogUtility;
import java.util.Scanner;

public class ZombieClassification {
    public File importData() {
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();

        File doctorFile = new File(path);

        return doctorFile;
    }

    public void doutorInteligente(String[] args) {
        try {
            File doctorFile = importData();

            VersatileDataSource source = new CSVDataSource(doctorFile, false, CSVFormat.DECIMAL_POINT);

            VersatileMLDataSet data = new VersatileMLDataSet(source);

            data.defineSourceColumn("paralysis", 0, ColumnType.continuous);
            data.defineSourceColumn("yellow_tong", 1, ColumnType.continuous);
            data.defineSourceColumn("member_loss", 2, ColumnType.continuous);
            data.defineSourceColumn("chest_pain", 3, ColumnType.continuous);
            data.defineSourceColumn("rembling_finger", 4, ColumnType.continuous);
            data.defineSourceColumn("severe_anger", 5, ColumnType.continuous);
            data.defineSourceColumn("history_bacteria", 6, ColumnType.continuous);

            ColumnDefinition outputColumn = data.defineSourceColumn("diagnostic", 7, ColumnType.nominal);

            data.analyze();

            data.defineSingleOutputOthersInput(outputColumn);

            EncogModel model = new EncogModel(data);
            model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);

            model.setReport(new ConsoleStatusReportable());

            data.normalize();

            model.holdBackValidation(0.3, true, 1001);

            model.selectTrainingType(data);

            MLRegression bestMethod = (MLRegression) model.crossvalidate(5, true);

            System.out.println("Training error: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
            System.out.println("Validation error: " + EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));

            NormalizationHelper helper = data.getNormHelper();
            System.out.println(helper.toString());

            System.out.println("Final model: " + bestMethod);

            ReadCSV csv = new ReadCSV(doctorFile, false, CSVFormat.DECIMAL_POINT);
            String[] line = new String[7];
            MLData input = helper.allocateInputVector();

            while (csv.next()) {
                StringBuilder result = new StringBuilder();
                line[0] = csv.get(0);
                line[1] = csv.get(1);
                line[2] = csv.get(2);
                line[3] = csv.get(3);
                line[4] = csv.get(4);
                line[5] = csv.get(5);
                line[6] = csv.get(6);
                String correct = csv.get(7);
                helper.normalizeInputVector(line, input.getData(), false);
                MLData output = bestMethod.compute(input);
                String docChosen = helper.denormalizeOutputVectorToString(output)[0];

                result.append(Arrays.toString(line));
                result.append(" -> predicted: ");
                result.append(docChosen);
                result.append("(correct: ");
                result.append(correct);
                result.append(")");

                System.out.println(result.toString());
            }

            // Delete data file ande shut down.
            doctorFile.delete();
            Encog.getInstance().shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZombieClassification doutor = new ZombieClassification();
        doutor.doutorInteligente(args);
    }

}