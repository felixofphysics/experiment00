package org.encog.examples.guide.classification;

public interface ITableProducer {
    String[] requestAttributes();
    String[][] requestInstances();
}