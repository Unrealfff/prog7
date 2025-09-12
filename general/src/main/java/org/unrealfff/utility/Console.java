package org.unrealfff.utility;

import java.util.Scanner;

/**
 * Консоль для ввода команд и вывода результата
 */
public interface Console {
    void print(Object obj);
    void println(Object obj);
    String readln();
    boolean canReadln();
    Integer isEampty();
    void printError(Object obj);
    void printTable(Object obj1, Object obj2);
    void prompt();
    String getPrompt();
    void selectFile(Scanner obj);
    void selectConsole();
    String readPassword();
}