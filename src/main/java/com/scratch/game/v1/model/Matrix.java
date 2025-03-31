package com.scratch.game.v1.model;

/**
 * Represents a 2D matrix used in the scratch card game.
 * The matrix contains rows and columns, and each cell can hold a symbol.
 */
public class Matrix {
    private final int rows;
    private final int columns;
    private final String[][] cells;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.cells = new String[rows][columns];
    }

    public void setCell(int row, int column, String value) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            cells[row][column] = value;
        }
    }

    public String[][] getCells() {
        return cells;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < rows; i++) {
            sb.append("    [");
            for (int j = 0; j < columns; j++) {
                sb.append("\"").append(cells[i][j]).append("\"");
                if (j < columns - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (i < rows - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}