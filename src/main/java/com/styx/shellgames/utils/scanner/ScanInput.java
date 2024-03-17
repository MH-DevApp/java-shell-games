package com.styx.shellgames.utils.scanner;

import com.styx.shellgames.utils.ErrorsProperties;

import java.util.Scanner;

public class ScanInput {
    public static int ScanChooseMenu(int max) throws ScanInputException {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine().trim();

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
