import org.encog.examples.guide.classification.DataSetComponent;
import org.encog.examples.guide.classification.IDataSet;

public class MinhaClasse {
    public static void main(String args[]) {
        IDataSet dataset = new DataSetComponent();
        dataset.setDataSource("src/new.csv");

        System.out.println("=== Attributes ===");
        String attributes[] = dataset.requestAttributes();
        for (int a = 0; a < attributes.length-1; a++)
            System.out.print(attributes[a] + ", ");
        System.out.println(attributes[attributes.length-1]);

        System.out.println();
        System.out.println("=== Instances ===");
        String instances[][] = dataset.requestInstances();
        for (int i = 0; i < instances.length; i++) {
            for (int a = 0; a < attributes.length-1; a ++)
                System.out.print(instances[i][a] + ", ");
            System.out.println(instances[i][attributes.length-1]);
        }
    }
}
