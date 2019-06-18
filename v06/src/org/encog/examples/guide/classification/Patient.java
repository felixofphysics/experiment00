package org.encog.examples.guide.classification;

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

        for (int a = 0; a < attributes.length - 1; a++) {
            if (question.equalsIgnoreCase(attributes[a]))
                result = (patientInstance[a].equals("t")) ? "yes" : "no";
        }

        return result;
    }
    public String askSymp(String question) {
        String result = "unknown";

        for (int a = 0; a < attributes.length - 1; a++) {
            if (question.equalsIgnoreCase(attributes[a]))
                result = (patientInstance[a].equals("t")) ? "yes" : "no";
        }
        Random random = new Random();
        int nAnswer = random.nextInt(3);

        if (result.equals("yes")) {
            if (nAnswer == 0)
                result = "Yes, I have";
            else if (nAnswer == 1)
                result = "I think so, not so sure... But if I have to pick one, so... yes!";
            else if (nAnswer == 2)
                result = "Yeah, I've been feeling that!!";
            else if (nAnswer == 3)
                result = "Perhaps in the last few days, so yeah...";
        }
        else {
            if (nAnswer == 0)
                result = "I don't think so...";
            else if (nAnswer == 1)
                result = "Not really";
            else if (nAnswer == 2)
                result = "No no no!";
            else if (nAnswer == 3)
                result = "Are you crazy? Absolutely not!";
        }

        return result;
    }

    public boolean finalAnswer(String answer) {
        boolean result = false;
        if (answer.equalsIgnoreCase(patientInstance[attributes.length - 1]))
            result = true;
        return result;
    }
}