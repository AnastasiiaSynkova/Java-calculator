package com.company;
import java.util.Random;

public class Generator {
    public static String GenerateExpression(int length) {
        Random random = new Random();
        StringBuilder expression = new StringBuilder();
        expression.append(random.nextInt(100));

        for (int i = 1; i < length; i++) {
            expression.append(random.nextBoolean() ? '+' : '-');
            expression.append(random.nextInt(100));
        }
        return expression.toString();
    }
}