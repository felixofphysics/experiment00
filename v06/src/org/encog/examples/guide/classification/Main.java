package org.encog.examples.guide.classification;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        // instanciando o componente DataSet
        IDataSet dataset = new DataSetComponent();
        dataset.setDataSource("src/zombiebig.csv");

// instanciando o componente paciente
        IPatient aPatient = new Patient();

// conectando-o no componente DataSetaPatient.connect(dataset);
        aPatient.connect(dataset);

// instanciando o componente doutor louco
        IDoctor cDoctor = new SmartDoctor();

// conectando-o ao componente DataSet
        cDoctor.connect(dataset);

// conectando-o ao componente paciente
        cDoctor.connect(aPatient);

// executando a entrevista
        cDoctor.startInterview();
    }
}
