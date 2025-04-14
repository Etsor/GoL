package main;

import javax.swing.Timer;

import main.model.Board;
import main.model.Cell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JSlider;
import javax.swing.BorderFactory;

import javax.swing.JFileChooser;
import java.io.File;

public class Main extends JFrame {

    public static void drawGlider(Cell[][] board, int size) {
        int center = size / 2;

        board[center][center].setAlive(true);
        board[center + 1][center + 1].setAlive(true);
        board[center + 2][center + 1].setAlive(true);
        board[center + 2][center].setAlive(true);
        board[center + 2][center - 1].setAlive(true);
    
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int size = 400;
            Cell[][] board = Board.createBoard(size);
            
            drawGlider(board, size);

            JFrame frame = new JFrame("Game Of Life");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GridPanel panel = new GridPanel(board);
            frame.add(panel, BorderLayout.CENTER);

            JButton startButton = new JButton("Start");
            JButton clearButton = new JButton("Clear");
            
            JButton saveButton = new JButton("Save");
            JButton loadButton = new JButton("Load");

            JButton zoomInButton = new JButton("+");
            JButton zoomOutButton = new JButton("-");
            
            JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL,
            5, 50, 40);
            
            JPanel zoomPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
            
            zoomSlider.setBorder(BorderFactory
            .createTitledBorder("Scale"));
            
            zoomSlider.setMajorTickSpacing(5);
            zoomSlider.setPaintTicks(true);
            
            zoomSlider.addChangeListener(e -> {
                panel.setCellSize(zoomSlider.getValue());
                frame.pack();
            });

            buttonPanel.add(startButton);
            buttonPanel.add(clearButton);
            buttonPanel.add(saveButton);
            buttonPanel.add(loadButton);

            zoomPanel.add(zoomOutButton);
            zoomPanel.add(zoomSlider);
            zoomPanel.add(zoomInButton);

            JPanel controlPanel = new JPanel(new BorderLayout());
            controlPanel.add(buttonPanel, BorderLayout.NORTH);
            controlPanel.add(zoomPanel, BorderLayout.SOUTH);
            
            frame.add(controlPanel, BorderLayout.NORTH);

            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent event) {
                    Dimension size = frame.getSize();
                
                    Dimension controlSize = controlPanel.getPreferredSize();
                    panel.setPreferredSize(new Dimension(
                        size.width - 20, 
                        size.height - controlSize.height - 50
                    ));
                    
                    frame.revalidate();
                }
                
            });

            frame.setMinimumSize(new Dimension(400, 500));
            frame.setVisible(true);
            
            Timer timer = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Cell[][] current = panel.getBoard();
                    Cell[][] next = Board.nextGeneration(current);

                    for (int x = 0; x < current.length; x++) {
                        for (int y = 0; y < current[x].length; y++) {
                            current[x][y].setAlive(next[x][y].isAlive());
                            current[x][y].setAge(next[x][y].getAge());
                        }
                    }

                    panel.repaint();
                }
            });

            JSlider speedSlider = new JSlider(JSlider.HORIZONTAL,
            50, 1000, 300);

            speedSlider.setBorder(BorderFactory
            .createTitledBorder("Simulation speed"));
            
            Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            labelTable.put(50, new JLabel("Faster"));
            labelTable.put(1000, new JLabel("Slower"));
            
            speedSlider.setLabelTable(labelTable);
            speedSlider.setPaintLabels(true);
            
            speedSlider.addChangeListener(e -> {
                timer.setDelay(speedSlider.getValue());
            });
            
            buttonPanel.add(speedSlider);

            zoomInButton.addActionListener(e -> {
                int newSize = Math.min(panel.getCellSize() + 5, 50);
                zoomSlider.setValue(newSize);
            });

            zoomOutButton.addActionListener(e -> {
                int newSize = Math.max(panel.getCellSize() - 5, 5);
                zoomSlider.setValue(newSize);
            });

            startButton.addActionListener(new ActionListener() {
                boolean running = false;
            
                @Override
                public void actionPerformed(ActionEvent e) {
                    running = !running;
                    panel.setRunning(running);
                    
                    if (running) {
                        timer.start();
                        startButton.setText("Pause");
                    } else {
                        timer.stop();
                        startButton.setText("Start");
                    }
                }
            });

            clearButton.addActionListener(e -> {
                Cell[][] grid = panel.getBoard();
                for (Cell[] cells : grid) {
                    for (Cell cell : cells) {
                        cell.setAlive(false);
                        cell.resetAge();
                    }
                }
                panel.repaint();
            });

            saveButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(frame) == JFileChooser
                .APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    FileManager.saveToText(panel.getBoard(), file);
                }
            });

            loadButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(frame) == JFileChooser
                .APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Cell[][] loadedBoard = FileManager.loadFromText(file);
                    if (loadedBoard != null) {
                        panel.setBoard(loadedBoard);
                    }
                }
            });

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}