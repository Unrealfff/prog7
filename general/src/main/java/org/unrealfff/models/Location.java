package org.unrealfff.models;

import org.unrealfff.utility.Validatable;

public class Location implements Validatable {
    /**
     * Location - a secondary data unit
     */
    private Integer x; //Поле не может быть null
    private double y;
    private Double z; //Поле не может быть null
    private String name; //Строка не может быть пустой, Поле может быть null

    public Location(Integer x, double y, Double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    public Location(String s) {
        try {
            try {
                this.x = Integer.parseInt(s.split(";")[0]);
            }
            catch (NumberFormatException e) {  }
            try { this.y = Double.parseDouble(s.split(";")[1]); }
            catch (NumberFormatException e) { }
            try { this.z = Double.parseDouble(s.split(";")[2]); }
            catch (NumberFormatException e) { }
            this.name = s.split(";")[3];
        }
        catch (ArrayIndexOutOfBoundsException e) {  }
    }


    public Integer getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Double getZ() {
        return this.z;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return x + ";" + y + ";" + z + ";" + name;
    }
    @Override
    public boolean validate() {
        if (x == null) {
            return false;
        }
        if (z == null) {
            return false;
        }
        if (name.isEmpty()) {
            return false;
        }
        return true;
    }

    public static Location fromString(String s) {
        Integer x;
        double y;
        Double z;
        String name;
        try {
            try {
                x = Integer.parseInt(s.split(";")[0]);
            }
            catch (NumberFormatException e) { return null; }
            try { y = Double.parseDouble(s.split(";")[1]); }
            catch (NumberFormatException e) { return null; }
            try { z = Double.parseDouble(s.split(";")[2]); }
            catch (NumberFormatException e) { return null; }
            name = s.split(";")[3];
        }
        catch (ArrayIndexOutOfBoundsException e) { return null; }
        return new Location(x, y, z, name);
    }
}