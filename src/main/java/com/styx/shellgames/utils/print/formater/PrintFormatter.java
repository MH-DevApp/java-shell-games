package com.styx.shellgames.utils.print.formater;

import com.styx.shellgames.generic.GameBoard;
import com.styx.shellgames.utils.ErrorsProperties;

import java.util.List;

public class PrintFormatter {
    private static int MARGIN;

    public static void print(String title, List<String> lines) {
        print(title, lines, 20);
    }

    public static void print(String title, List<String> lines, int margin) {
        PrintFormatter.MARGIN = margin;

        int maxLength = getMaxLength(lines);

        System.out.println();

        if (title != null) {
            System.out.println(fullLine(maxLength));
            System.out.println(centeredLine(title, maxLength));
        }

        System.out.println(fullLine(maxLength));
        System.out.println(centeredLine("", maxLength));

        for (String line : lines) {
            System.out.println(borderedLine(line, maxLength));
        }

        System.out.println(centeredLine("", maxLength));
        System.out.println(fullLine(maxLength));

        if (GameBoard.errorMessage != null) {
            System.out.print("\u001B[31m");
            System.out.printf(
                    "%s: %s%n",
                    ErrorsProperties.getProperty("error"),
                    GameBoard.errorMessage
            );
            System.out.println("\u001B[0m");
            GameBoard.errorMessage = null;
        }
    }

    private static int getMaxLength(List<String> lines) {
        int maxLength = 0;

        for (String line : lines) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        return maxLength + (MARGIN * 2);
    }

    private static String fullLine(int length) {
        return "=".repeat(length);
    }

    private static String borderedLine(String line, int length) {
        return "*" + " ".repeat((MARGIN) - 1) + line + " ".repeat(length - line.length() - (MARGIN) - 1) + "*";
    }

    private static String centeredLine(String line, int length) {
        int leftMargin = (length - line.length()) / 2;
        int rightMargin = length - line.length() - leftMargin;

        return "*" + " ".repeat(leftMargin - 1) + line + " ".repeat(rightMargin - 1) + "*";
    }



}
