package org.unrealfff.models;

//import org.unrealfff.Coordinates;
//import org.unrealfff.models.Location;

import org.unrealfff.utility.Validatable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

public class Route implements Validatable, Comparable<Route> {
    /**
     * Route - main data unit
     */
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле не может быть null
    private Float distance; //Поле может быть null, Значение поля должно быть больше 1
    private Integer user;

    public Route(Integer id, String name, Coordinates coordinates, LocalDate creationDate, Location from,
                 Location to, Float distance, Integer user) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.user = user;
    }

    public void setId(Integer id) {this.id = id;}

    public String toFormatString() {
        return "route{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"creationDate\": \"" + creationDate + "\", " +
                "\"coordinates\": \"" + coordinates + "\", " +
                "\"from\": " + (from == null ? " null " : "\""+from+"\"") +
                "\"to\": \"" + to + "\", " +
                "\"distance\": " + (distance == null ? " null " : "\""+distance+"\", ") +
                "\"user_id\": \"" + user + "\"" +"}";
                //"\"distance\": \"" + distance + "\", " + "}";
    }

    @Override
    public String toString() {
        return id.toString() + "," + name + "," + coordinates + "," + creationDate + "," +
        from  + "," + to + "," + distance + "," + user;
    }
    @Override
    public boolean validate() {
        if (id == null) {
            return false;
        }
        if (name == null) {
            return false;
        }
        if (creationDate == null) {
            return false;
        }
        if (coordinates == null) {
            return false;
        }
        if (!coordinates.validate()) return false;

        if (from != null && !from.validate()) return false;
        if (to == null) {
            return false;
        }
        if(!to.validate()) return false;
        if (id.compareTo(0) <= 0) {
            return false;
        }
        if (distance != null && distance.compareTo(1F) <= 0) {
            return false;
        }
        if (user == null || user < 1) return false;
        return true;
    }

    public Float getDistance() {
        return distance;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Location getFrom() {
        return from;
    }
    public Location getTo() {
        return to;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return this.id;
    }
    public Integer getUser() {return user;}

    public static String[] toArray(Route route) {
        if(route == null) return null;
        ArrayList<String> fields = new ArrayList<>();
        fields.add(route.getId().toString());
        fields.add(route.getName());
        fields.add(route.getCoordinates().toString());
        fields.add(route.getCreationDate().format(DateTimeFormatter.ISO_DATE));
        fields.add(route.getFrom() == null ? "null" : route.getFrom().toString());
        fields.add(route.getTo().toString());
        fields.add(route.getDistance() == null ? "null" : route.getDistance().toString());
        fields.add(route.getUser().toString());
        return fields.toArray(new String[0]);
    }

    public static Route fromArray(String[] data) {
        Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
        String name; //Поле не может бытьcreationDate =  null, Строка не может быть пустой
        Coordinates coordinates; //Поле не может быть null
        LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
        Location from; //Поле может быть null
        Location to; //Поле не может быть null
        Float distance; //Поле может быть null, Значение поля должно быть больше 1
        Integer user;
        try {
            try{
                id = Integer.parseInt(data[0]);
            }
            catch (NumberFormatException e) { id = null; }
            name = data[1];
            coordinates = new Coordinates(data[2]);
            try {
                creationDate = LocalDate.parse(data[3], DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException e) { creationDate = null; }
            from = Location.fromString(data[4]);
            to = Location.fromString(data[5]);
            try{
                distance = Float.parseFloat(data[6]);
            }
            catch(NumberFormatException e) { distance = null;}
            try{
                user = Integer.parseInt(data[7]);
            }
            catch (NumberFormatException e) {user = null;}
            return new Route(id, name, coordinates, creationDate, from, to, distance, user);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public int compareTo(Route route) {
        if (route == null) return 1;
        if (this.distance != null) {
            if(route.getDistance() != null) {
                return this.distance.compareTo(route.getDistance());
            }
            else {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route that = (Route) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance, user);
    }
}
