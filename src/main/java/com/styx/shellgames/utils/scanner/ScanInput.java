package com.styx.shellgames.utils.scanner;

import com.styx.shellgames.generic.ExitGameException;
import com.styx.shellgames.utils.ErrorsProperties;

import java.util.Scanner;

public class ScanInput {
    private static void checkIfExitGame(String input) throws ExitGameException {
        if (input.equals("exit")) {
            throw new ExitGameException("Exiting game");
        }
    }

    public static char ScanChar() throws ExitGameException, ScanInputException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        checkIfExitGame(input);

        if (input.length() != 1) {
            throw new ScanInputException(ErrorsProperties.getProperty("input.notSingleChar"));
        }

        if (!Character.isLetter(input.charAt(0))) {
            throw new ScanInputException(ErrorsProperties.getProperty("input.notLetter"));
        }

        return input.charAt(0);
    }

    public static void ScanEnter(){
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static int ScanChooseMenu(int max) throws ScanInputException, ExitGameException {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine().trim();

        checkIfExitGame(input);

        if (input.length() != 1) {
            throw new ScanInputException(ErrorsProperties.getProperty("input.notSingleChar"));
        }

        if (!Character.isDigit(input.charAt(0))) {
            throw new ScanInputException(ErrorsProperties.getProperty("input.notInteger").formatted(max));
        }

        int choice = Integer.parseInt(input);

        if (choice < 1 || choice > max) {
            throw new ScanInputException(ErrorsProperties.getProperty("input.outOfRange").formatted(max));
        }

        return choice;
    }
}
