package com.yueshuya.knighttour;

public class Location {
    private int row;
    private int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "[" +row + ", " + col + "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Check if the objects are the same instance
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Ensure that the object is not null and of the same class
        }
        Location location = (Location) obj; // Cast the object to Location
        return row == location.row && col == location.col; // Compare row and col values
    }

    @Override
    public int hashCode() {
        return 31 * row + col; // Compute hash based on row and col
    }
}
