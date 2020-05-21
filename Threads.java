package com.company;
import java.util.Random;

class ThreadGenerator implements Runnable {

    private double result;
    private int length;

    public ThreadGenerator(int length) {
        this.length = length;
    }

    public void run() {
        try {
            result = Calculator.mainCount(Generator.GenerateExpression(length));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double GetResult() {
        return result;
    }
}

public class Threads {
    static final int THREADS_COUNT = 2;

    public float Task(int count, boolean isThread) throws Exception {
        float result = 0;
        Random random = new Random();
        //если выбран вариант с многопоточностью
        if (isThread) {
            int expressionCount = count / THREADS_COUNT;
            ThreadGenerator[] generateThreads = new ThreadGenerator[THREADS_COUNT];
            Thread[] threads = new Thread[THREADS_COUNT];
            //создаем вспомогательные объекты и запускаем потоки
            for (int i = 0; i < THREADS_COUNT; i++) {
                generateThreads[i] = new ThreadGenerator(expressionCount);
                //через вызов конструктора, принимающего на вход реализацию интерфейса Runnable
                threads[i] = new Thread(generateThreads[i]);
                threads[i].start();
            }
            //ожидаем пока все завершатся
            for (int i = 0; i < THREADS_COUNT; i++)
                threads[i].join();
            //вычисляем общее значение выражения
            for (int i = 0; i < THREADS_COUNT; i++)
                result += generateThreads[i].GetResult();
        }
        //однопоточное вычисление
        else
            result = Calculator.mainCount(Generator.GenerateExpression(count));
        return result;
    }
}