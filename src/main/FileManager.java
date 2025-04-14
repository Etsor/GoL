package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import main.model.Cell;

public class FileManager {
    public static void saveToText(Cell[][] board, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(board.length);
            // x, y, age
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[x].length; y++) {
                    Cell cell = board[x][y];
                    if (cell.isAlive()) {
                        writer.println(x + "," + y + "," + cell.getAge());
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Cell[][] loadFromText(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int size = Integer.parseInt(reader.readLine());
            Cell[][] board = new Cell[size][size];

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    board[x][y] = new Cell();
                }
            }
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int age = Integer.parseInt(parts[2]);
                
                board[x][y].setAlive(true);
                board[x][y].setAge(age);
            }
            
            return board;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}