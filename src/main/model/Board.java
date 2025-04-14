package main.model;

public class Board {
    public static Cell[][] createBoard(int size) {        
        Cell[][] board = new Cell[size][size];
        
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                board[x][y] = new Cell();
                Cell cell = board[x][y];
                cell.setAlive(false);
            }
        }
        
        return board;
    }

    public static Cell[][] nextGeneration(Cell[][] board) {
        final int minNeighbors = 2;
        final int maxNeighbors = 3;
    
        int size = board.length;
    
        Cell[][] nextBoard = new Cell[size][size];
    
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                nextBoard[x][y] = new Cell();
            }
        }
    
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int neighbors = countNeighbors(board, x, y);
    
                Cell currentCell = board[x][y];
                Cell newCell = nextBoard[x][y];
                
                newCell.setAge(currentCell.getAge());

                if (currentCell.isAlive()) {
                    if (neighbors < minNeighbors || neighbors > maxNeighbors) {
                        newCell.setAlive(false);
                        newCell.resetAge();
                    } else {
                        newCell.setAlive(true);
                        newCell.setAge(currentCell.getAge());
                        newCell.incrementAge();
                    }
                } else {
                    if (neighbors == maxNeighbors) {
                        newCell.setAlive(true);
                        newCell.resetAge();
                    } else {
                        newCell.setAlive(false);
                        newCell.resetAge();
                    }
                }
                nextBoard[x][y] = newCell;
            }
        }
    
        return nextBoard;
    }            

    private static int countNeighbors(Cell[][] board, int x, int y) {
        int neighbors = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int new_x = x + i;
                int new_y = y + j;

                if (new_x >= 0 && new_x < board.length
                && new_y >= 0 && new_y < board.length) {
                    Cell cell = board[new_x][new_y];
                    if (cell.isAlive()) {
                        neighbors++;
                    }
                }
            }
        }

        return neighbors;
    }
}