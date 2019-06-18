package org.encog.examples.guide.classification;

import java.util.ArrayList;

public interface IDoctor extends IEnquirer, IResponderReceptacle, ITableProducerReceptacle {
    public void doutorInteligente(String caminho, ArrayList<String> sintomas);
}
