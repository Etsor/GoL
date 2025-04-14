package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import main.model.Cell;

public class GridPanel extends JPanel {
    private boolean isRunning = false;
    private boolean isDrawing = false;
    private boolean drawState = false;

    private int defaultCellSize = 25;
    
    private static final int MIN_CELL_SIZE = 5;
    private static final int MAX_CELL_SIZE = 50;
    
    private Cell[][] board;
    
    public void setCellSize(int size) {
        if (size >= MIN_CELL_SIZE && size <= MAX_CELL_SIZE) {
            this.defaultCellSize = size;
            repaint();
        }
    }

    public int getCellSize() {
        return defaultCellSize;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }
    
    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] newBoard) {
        this.board = newBoard;
        repaint();
    }

    public GridPanel(Cell[][] board) {
        this.board = board;
        
        setBorder(javax.swing.BorderFactory
        .createLineBorder(Color.BLACK, 5));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (isRunning) {
                    return;
                }
                
                int col = event.getX() / defaultCellSize;
                int row = event.getY() / defaultCellSize;

                if (row >= 0 && row < board.length
                && col >= 0 && col < board[0].length) {
                    isDrawing = true;
                    drawState = !board[row][col].isAlive();
                    toggleCell(row, col);
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                isDrawing = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isRunning || !isDrawing) return;
                
                int col = e.getX() / defaultCellSize;
                int row = e.getY() / defaultCellSize;
                
                if (row >= 0 && row < board.length 
                && col >= 0 && col < board[0].length) {
                    if (board[row][col].isAlive() != drawState) {
                        toggleCell(row, col);
                    }
                }
            }
        });
    }

    private void toggleCell(int row, int col) {
        Cell cell = board[row][col];
        
        cell.setAlive(drawState);
        cell.resetAge();
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        Graphics2D graphics2d = (Graphics2D) graphics;
        
        // background color
        GradientPaint gradient = new GradientPaint(
            0, 0, 
            new Color(180, 200, 255),
            getWidth(), getHeight(),
            new Color(255, 180, 200)
        );

        graphics2d.setPaint(gradient);
        graphics2d.fillRect(0, 0, getWidth(), getHeight());

        if (board == null) {
            return;
        }

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {     
                Cell cell = board[row][col];
                
                int cellAge = cell.getAge();

                if (cell.isAlive()) {
                    // alive cell color
                    float hue = Math.max(0.0f, 0.6f - (cellAge * 0.02f));
                    Color cellColor = Color.getHSBColor(hue, 0.9f, 0.9f);
                    
                    graphics.setColor(cellColor);
                    graphics.fillRect(col * defaultCellSize, row * defaultCellSize,
                    defaultCellSize, defaultCellSize);
                
                } else {
                    // dead cell color
                    graphics.setColor(new Color(255, 255, 255, 86));
                    graphics.fillRect(col * defaultCellSize, row * defaultCellSize,
                    defaultCellSize, defaultCellSize);
                
                }
                // grid color
                graphics.setColor(new Color(100, 100, 100, 32));
                graphics.drawRect(col * defaultCellSize, row * defaultCellSize,
                defaultCellSize, defaultCellSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (board == null) {
            return new Dimension(400, 400);
        }
    
        Dimension size = getSize();
        
        if (size.width > 0 && size.height > 0) {
            int optimalCellSize = Math.min(
                size.width / board.length,
                size.height / board[0].length
            );

            defaultCellSize = Math.max(MIN_CELL_SIZE,
            Math.min(optimalCellSize, MAX_CELL_SIZE));
        }
        
        return size;
    }
}
