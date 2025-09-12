package org.unrealfff.models;

import org.unrealfff.utility.Validatable;

public class Coordinates implements Validatable {
    /**
     * Coordinates - a secondary data unit
     */
    private Long x; //Максимальное значение поля: 500, Поле не может быть null
    private Long y; //Поле не может быть null

    public Coordinates(Long x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(String s) {
        try {
            try {
                this.x = Long.parseLong(s.split(";")[0]);
            }
            catch (NumberFormatException e) { }
            try { this.y = Long.parseLong(s.split(";")[1]); }
            catch (NumberFormatException e) { }
        } catch (ArrayIndexOutOfBoundsException e) { }
    }

    public Long getX() {
        return this.x;
    }
    public Long getY() {
        return this.y;
    }
    @Override
    public String toString() {
        return x + ";" + y;
    }

    @Override
    public boolean validate() {
        if (x == null) {
            return false;
        }
        if (y == null) {
            return false;
        }
        if (x.compareTo(500L) > 0) {
            return false;
        }
        return true;
    }
}