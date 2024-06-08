package net.vlands.survival.quests.internal;

import java.util.ArrayList;
import java.util.List;

public class SlotMap {
    private final List<Slot> slots = new ArrayList<>();

    public SlotMap() {
        slots.add(0, null);
        slots.add(1, new Slot(2,2));
        slots.add(2, new Slot(3,2));
        slots.add(3, new Slot(4,2));
        slots.add(4, new Slot(5,2));
        slots.add(5, new Slot(5,3));
        slots.add(6, new Slot(5,4));
        slots.add(7, new Slot(4,4));
        slots.add(8, new Slot(3,4));
        slots.add(9, new Slot(2,4));
        slots.add(10, new Slot(2,5));
        slots.add(11, new Slot(2, 6));
        slots.add(12, new Slot(3, 6));
        slots.add(13, new Slot(4, 6));
        slots.add(14, new Slot(5, 6));
        slots.add(15, new Slot(5, 7));
        slots.add(16, new Slot(5, 8));
        slots.add(17, new Slot(4, 8));
        slots.add(18, new Slot(3, 8));
        slots.add(19, new Slot(2, 8));
        slots.add(20, new Slot(2, 9));
        slots.add(21, new Slot(2,1));
        slots.add(22, new Slot(2,2));
        slots.add(23, new Slot(3,2));
        slots.add(24, new Slot(4,2));
        slots.add(25, new Slot(5,2));
        slots.add(26, new Slot(5,3));
        slots.add(27, new Slot(5,4));
        slots.add(28, new Slot(5,5));
        slots.add(29, new Slot(4,5));
        slots.add(30, new Slot(3,5));
        slots.add(31, new Slot(3,4));
        slots.add(32, new Slot(2,4));
        slots.add(33, new Slot(1,4));
        slots.add(34, new Slot(1,5));
        slots.add(35, new Slot(1,6));
        slots.add(36, new Slot(1,7));
        slots.add(37, new Slot(2,7));
        slots.add(38, new Slot(3,7));
        slots.add(39, new Slot(4,7));
        slots.add(40, new Slot(5,7));
        slots.add(41, new Slot(5,8));
        slots.add(42, new Slot(5,9));
        slots.add(43, new Slot(5,1));
        slots.add(44, new Slot(5, 2));
        slots.add(45, new Slot(4, 2));
        slots.add(46, new Slot(3, 2));
        slots.add(47, new Slot(2, 2));
        slots.add(48, new Slot(1, 2));
        slots.add(49, new Slot(1, 3));
        slots.add(50, new Slot(1, 4));
        slots.add(51, new Slot(2, 4));
        slots.add(52, new Slot(3, 4));
        slots.add(53, new Slot(3, 5));
        slots.add(54, new Slot(3, 6));
        slots.add(55, new Slot(4, 6));
        slots.add(56, new Slot(5, 6));
        slots.add(57, new Slot(5, 7));
        slots.add(58, new Slot(5, 8));
        slots.add(59, new Slot(4, 8));
        slots.add(60, new Slot(3, 8));
        slots.add(61, new Slot(2, 8));
        slots.add(62, new Slot(2, 9));
    }

    public Slot getLocation(int a) {
        return slots.get(a);
    }
}
