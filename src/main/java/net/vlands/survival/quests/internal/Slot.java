package net.vlands.survival.quests.internal;

public record Slot(int row, int column) {
    public Slot(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
