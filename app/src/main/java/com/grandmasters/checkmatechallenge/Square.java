package com.grandmasters.checkmatechallenge;

import java.io.Serializable;
import java.util.Objects;

public class Square implements Serializable {
    private int col;
    private int row;

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Square(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Square square = (Square) obj;
        return col == square.col && row == square.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
