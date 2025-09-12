package org.unrealfff.managers;

import org.unrealfff.models.Coordinates;
import org.unrealfff.models.Location;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * Класс для запроса данных из консоли
 */
public class Handler {
    public static Route getRoute(Console console, int id, LocalDate creationDate) {
        console.println("building route");
        try {
            String name;
            while(true) {
                console.println("enter route name");
                console.prompt();
                name = console.readln().trim();
                if(!name.isEmpty()) break;
                incorrectInput(console);
            }
            Coordinates coordinates = getCoordinates(console);
            Location from = getLocation(console, "from");
            Location to;
            while(true) {
                to = getLocation(console, "to");
                if(to != null) break;
                incorrectInput(console);
            }
            Float distance;
            while(true) {
                console.println("enter route distance");
                console.prompt();
                String strDistance = console.readln().trim();
                if (!strDistance.isEmpty()) {
                    try {
                        distance = Float.parseFloat(strDistance);
                    }
                    catch (NumberFormatException e) {
                        incorrectInput(console);
                        continue;
                    }
                }
                else{
                    distance = null;
                }
                if(distance == null || distance > 1.0F) break;
                incorrectInput(console);
            }
            return new Route(id, name, coordinates, creationDate, from, to, distance, -1);
        }
        catch (NoSuchElementException | IllegalStateException e) {
            console.printError("reading error");
            return null;
        }
    }


    public static Coordinates getCoordinates(Console console) {
        try {
            console.println("building coordinates");
            Long x;
            while (true) {
                console.println("enter coordinates x");
                console.prompt();
                String strX = console.readln().trim();
                try {
                    x = Long.parseLong(strX);
                } catch (NumberFormatException e) {
                    incorrectInput(console);
                    continue;
                }
                if (x.compareTo(500L) < 0) break;
                incorrectInput(console);
            }
            Long y;
            while (true) {
                console.println("enter coordinates y");
                console.prompt();
                String strY = console.readln().trim();
                try {
                    y = Long.parseLong(strY);
                } catch (NumberFormatException e) {
                    incorrectInput(console);
                    continue;
                }
                break;
            }
            return new Coordinates(x, y);
        }
        catch (NoSuchElementException | IllegalStateException e) {
            console.printError("reading error");
            return null;
        }
    }


    public static Location getLocation(Console console, String direction) {
        try {
            console.println("building location " + direction);
            Integer x;
            while (true) {
                console.println("enter location x");
                console.prompt();
                String strX = console.readln().trim();
                if (!strX.isEmpty()) {
                    try {
                        x = Integer.parseInt(strX);
                    }
                    catch (NumberFormatException e) {
                        incorrectInput(console);
                        continue;
                    }
                    break;
                }
                else {
                    return null;
                }
            }
            double y;
            while (true) {
                console.println("enter location y");
                console.prompt();
                String strY = console.readln().trim();
                try {
                    y = Double.parseDouble(strY);
                }
                catch (NumberFormatException e) {
                    incorrectInput(console);
                    continue;
                }
                break;
            }
            Double z;
            while (true) {
                console.println("enter location z");
                console.prompt();
                String strZ = console.readln().trim();
                try {
                    z = Double.parseDouble(strZ);
                } catch (NumberFormatException e) {
                    incorrectInput(console);
                    continue;
                }
                if(z!=null) break;
                incorrectInput(console);
            }
            String name;
            while(true) {
                console.println("enter location name");
                console.prompt();
                name = console.readln().trim();
                if(!name.isEmpty()) break;
                incorrectInput(console);
            }
            return new Location(x, y, z, name);
        }
        catch (NoSuchElementException | IllegalStateException e) {
            console.printError("reading error");
            return null;
        }
    }


    public static void incorrectInput(Console console) {
        console.printError("your input is incorrect - please try again");
    }
}
