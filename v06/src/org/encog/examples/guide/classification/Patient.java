package org.encog.examples.guide.classification;

import java.util.Random;

import java.util.Random;

// combinando duas interfaces para IPatient
public class Patient implements IPatient {
    private int patientN = 0;

    private ITableProducer producer;

    private String attributes[];
    private String patientInstance[];

    public void connect(ITableProducer producer) {
        this.producer = producer;

        attributes = producer.requestAttributes();
        String instances[][] = producer.requestInstances();

        patientN = (int)(Math.random() * instances.length);
        patientInstance = instances[patientN];

        System.out.println("Patient selected: " + patientN);
        System.out.println("Patient's disease: " + patientInstance[attributes.length - 1]);
    }

    public String ask(String question) {
        String result = "unknown";

        for (int a = 0; a < attributes.length - 1; a++)
            if (question.equalsIgnoreCase(attributes[a]))
                result = (patientInstance[a].equals("t")) ? "yes" : "no";

        return result;
    }

    public boolean finalAnswer(String answer) {
        boolean result = false;
        if (answer.equalsIgnoreCase(patientInstance[attributes.length - 1]))
            result = true;
        return result;
    }
}