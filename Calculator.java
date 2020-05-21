package com.company;
import java.util.Stack;

class Operation {
    public char ch;
    public int priority;
    public Operation(char ch, int priority) {
        this.ch = ch;
        this.priority = priority;
    }
}

public class Calculator {
    

    //вспомогательные методы

    //выполняет одну операцию
    public static void countOne(Operation op,  Stack<Float> digitStack) throws Exception {
        float n1, n2;
        if (!digitStack.empty()) {
            n1 = digitStack.pop();
        }
        else
            throw new Exception("Неверное выражение");
        if (!digitStack.empty()) {
            n2 = digitStack.pop();
        }
        else
            throw new Exception("Неверное выражение");
        switch (op.ch) {
            case '*':
                digitStack.push(n2 * n1);
                break;
            case '/':
                if (n1 == 0)
                    throw new Exception("Деление на ноль");
                digitStack.push(n2 / n1);
                break;
            case '+':
                digitStack.push(n2 + n1);
                break;
            case '-':
                digitStack.push(n2 - n1);
                break;
        }
    }

    //выполнение операций в стеках
    //если приоритет позволяет, то считаем, если нет - добавляем
    public static void makeOperation(Operation operation, Stack<Operation> operationStack, Stack<Float> digitStack) throws Exception {
        if (operationStack.empty())
            operationStack.push(operation);
        else {
            Operation op = operationStack.peek(); // смотрим, что на верхушке
            //если приоритет операции такой же или больше, то вполняем операцию
            while ( (op.priority >= operation.priority) && (!operationStack.empty())) {
                op = operationStack.pop();
                countOne(op, digitStack);
                //если стек не пустой, смотрим что там
                if (!operationStack.empty())
                    op = operationStack.peek();
            }
            operationStack.push(operation); //добавляем этот знак операции в стек
        }
    }

    //добавление знака, если приоритет позволяет, то сразу вычисляется выражение
    public static void addOp(char ch, Stack<Operation> operationStack, Stack<Float> digitStack ) throws Exception {
        Operation op;
        switch (ch) {
            case '(': //если открывающаяся, то просто добавляем
                operationStack.push(new Operation('(',0));
                break;
            case ')':
                //выполняем все операции пока не встретим открывающуюся скобку
                if (!operationStack.empty()) {
                    op = operationStack.pop();
                    while (op.ch != '(') {
                        countOne(op, digitStack);
                        if (!operationStack.empty())
                            op = operationStack.pop();
                    }
                }
                else //если стек операций пуст
                    throw new Exception("Некорректное выражение");
                break;
            case '+':
                makeOperation(new Operation('+',1), operationStack, digitStack);
                break;
            case '-':
                makeOperation(new Operation('-',1), operationStack, digitStack);
                break;
            case '*':
                makeOperation(new Operation('*',2), operationStack, digitStack);
                break;
            case '/':
                makeOperation(new Operation('/',2), operationStack, digitStack);
                break;
            case ' ':
                break;
            default:
                throw new Exception("Некорректный символ!");
        };
    }
}