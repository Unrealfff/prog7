package org.unrealfff.utility;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Для ввода команд и вывода результата
 */
public class StandartConsole implements Console {
    private static final String start = "> ";
    private static Scanner defScanner = new Scanner(System.in);
    private static Scanner scanner = new Scanner(System.in);
    private boolean file = false;

    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    public String readln() throws NoSuchElementException, IllegalStateException {
        return (scanner !=null? scanner : scanner).nextLine();
    }

    public boolean canReadln() throws IllegalStateException {
        return (scanner !=null ? scanner : scanner).hasNextLine();
    }

    @Override
    public Integer isEampty() {
        try {
            return (System.in.available());
        }
        catch (IOException e) {
            return 0;
        }
    }

    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    public void prompt() {
        print(start);
    }

    public String getPrompt() {
        return start;
    }

    public void selectFile(Scanner scanner) {
        this.scanner = scanner;
        this.file = true;
    }

    public void selectConsole() {
        this.scanner = defScanner;
        this.file = false;
    }

    @Override
    public String readPassword() {
        return readln();
    }
}