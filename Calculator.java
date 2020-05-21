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
    
    //основной метод вычисления
    public static float mainCount(String str) throws Exception {
        int k = str.length();
        Stack<Float> digitStack = new Stack<Float>();
        Stack<Operation> signStack = new Stack();
        //разбираем строку на цифры и знаки и заполняем соответствующие стеки
        parseString(str, digitStack, signStack);
        //считаем что получилось в стеках и выполняем все операции
        while (!signStack.empty()) {
            countOne(signStack.pop(), digitStack);
        }
        //результат окажется на верхушке
        if (digitStack.size() != 1)
            throw new Exception("Неверное выражение");
        return digitStack.pop();
    }

    public static boolean isDigit(char _ch){
        return (_ch >= '0') && (_ch <= '9');
    }
    //считывание формулы с занесением в стек и параллельным вычислением
    public static void parseString(String str, Stack<Float> digitStack, Stack<Operation> operationStack) throws Exception {
        char ch;
        float tmp; //временная переменная для получения числа из строки
        int i = 0;
        //формирует стеки чисел и знаков
        do {
            ch = str.charAt(i);
            //если символ является цифрой
            if (isDigit(ch)) {
                tmp = Character.getNumericValue(ch); //получаем численное значение
                digitStack.push(tmp); //и добавляем его в стек
                //пока не встретили знак +-*/
                while ( isDigit(ch) && (++i < str.length())) {
                    ch = str.charAt(i);
                    //если за цифрой тоже цифра -> двузначное число, достаем из стека первую цифру и кладем уже двузначное число
                    if (isDigit(ch)) {
                        digitStack.pop();
                        tmp = tmp * 10 + Character.getNumericValue(ch);
                        digitStack.push(tmp);
                    }
                    //если знак операции, то добавляем в стек знаков
                    else {
                        addOp(ch, operationStack, digitStack);
                        i++;
                    }
                }
            }
            else {
                addOp(ch, operationStack, digitStack);
                i++;
            }
        } while (i < str.length());
    }
    
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
