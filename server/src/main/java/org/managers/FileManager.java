package org.managers;



import com.opencsv.*;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {
    private final Console console;
    private Connection connection;
    /**
     * Класс для чтения и записи в файл
     */
    public FileManager(Console console) {
        this.console = console;
        init();
    }

    public void init() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("db.cfg"));
            connection = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", properties);
            Statement statement = connection.createStatement();
            /*statement.executeUpdate("CREATE TABLE IF NOT EXISTS COORDINATES(\n" +
                    "    ID serial PRIMARY KEY,\n" +
                    "    COORDINATES_X bigint NOT NULL CHECK(x <= 500),\n" +
                    "    COORDINATES_Y bigint NOT NULL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS FROMS(\n" +
                    "    ID serial PRIMARY KEY,\n" +
                    "    X integer NOT NULL,\n" +
                    "    Y DOUBLE PRECISION NOT NULL,\n" +
                    "    Z DOUBLE PRECISION NOT NULL,\n" +
                    "    NAME text NOT NULL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS TOS(\n" +
                    "    ID serial PRIMARY KEY,\n" +
                    "    X integer NOT NULL,\n" +
                    "    Y DOUBLE PRECISION NOT NULL,\n" +
                    "    Z DOUBLE PRECISION NOT NULL,\n" +
                    "    NAME text NOT NULL);");*/
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS USERS(\n" +
                    "    ID serial PRIMARY KEY,\n" +
                    "    LOGIN text NOT NULL UNIQUE,\n" +
                    "    PASSWORD TEXT NOT NULL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ROUTE(\n" +
                    "    ID serial PRIMARY KEY,\n" +
                    "    NAME text NOT NULL,\n" +
                    "    COORDINATES_X bigint NOT NULL CHECK(COORDINATES_X <= 500),\n" +
                    "    COORDINATES_Y bigint NOT NULL,\n" +
                    "    CREATION_DATE timestamp NOT NULL,\n" +
                    "    FROM_X integer,\n" +
                    "    FROM_Y DOUBLE PRECISION,\n" +
                    "    FROM_Z DOUBLE PRECISION,\n" +
                    "    FROM_NAME text,\n" +
                    "    TO_X integer NOT NULL,\n" +
                    "    TO_Y DOUBLE PRECISION NOT NULL,\n" +
                    "    TO_Z DOUBLE PRECISION NOT NULL,\n" +
                    "    TO_NAME text NOT NULL,\n" +
                    "    DISTANCE float CHECK(DISTANCE > 1),\n" +
                    "    USER_ID integer REFERENCES USERS(ID) NOT NULL);");
            statement.close();
        } catch (IOException e) {
            System.out.println("cfg file can not be reached: " + e);
            System.exit(1);
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Преобразует коллекцию в CSV-строку.
     * @param collection
     * @return CSV-строка
     */
    /*private String collection2CSV (Set<Route> collection) {
        try {
            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);
            for (var e : collection) {
                csvWriter.writeNext(Route.toArray(e));
            }
            String csv = stringWriter.toString();
            return csv;
        }
        catch (Exception e) {
            console.printError("Serialization error");
            return null;
        }
    }*/

    /**
     * Преобразует CSV-строку в коллекцию.
     * @param s-строка
     * @return коллекция
     */
    /*private Set<Route> CSV2collection(String s) {
        try {
            StringReader sr = new StringReader(s);
            CSVReader csvReader = new CSVReader(sr);
            Set<Route> ds = ConcurrentHashMap.newKeySet();
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                Route d = Route.fromArray(record);
                if (d != null && d.validate())
                    ds.add(d);
                else
                    console.printError("file contains corrupted data");
            }
            csvReader.close();
            return ds;
        } catch (Exception e) {
            console.printError("deserialization error");
            return null;
        }
    }*/

    /**
     * Считывает коллекцию из файл.
     */
    public void readCollection(Set<Route> collection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ROUTE.ID, ROUTE.NAME, ROUTE.CREATION_DATE," +
                    " COORDINATES_X, COORDINATES_Y, TO_X, TO_Y, TO_Z, TO_NAME, DISTANCE, FROM_X, FROM_Y, FROM_Z," +
                    " FROM_NAME, USER_ID\n" +
                    "FROM ROUTE");
            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String name = resultSet.getString("NAME");
                String creationDate = resultSet.getString("CREATION_date");
                String coordinatesX = resultSet.getString("coordinates_x");
                String coordinatesY = resultSet.getString("coordinates_y");
                String fromX = resultSet.getString("from_x");
                String fromY = resultSet.getString("from_y");
                String fromZ = resultSet.getString("from_z");
                String fromName = resultSet.getString("from_name");
                String toX = resultSet.getString("to_x");
                String toY = resultSet.getString("to_y");
                String toZ = resultSet.getString("to_z");
                String toName = resultSet.getString("to_name");
                String distance = resultSet.getString("distance");
                Integer user = resultSet.getInt("user_id");
                String[] data = {id, name, coordinatesX + ";" + coordinatesY, creationDate.split(" ")[0], fromX==null ? "":fromX +
                        ";" + fromY + ";" + fromZ + ";" + fromName, toX +  ";" + toY + ";" + toZ + ";" + toName,
                        distance==null ? "":distance, user.toString()};
                Route route = Route.fromArray(data);
                if(route != null && route.validate()){
                    collection.add(route);
                }
                else {
                    System.out.println("Corrupted data");
                }
            }
            System.out.println("collection loaded");
            //while (resultSet.next()) {
                //collection.add(Route.fromArray(data));
            //}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer login(String[] login) {
        String query = "SELECT ID FROM USERS WHERE LOGIN = ? AND PASSWORD = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login[0]);
            statement.setString(2, login[1]);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("id");
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(String[] login) {
        String query = "INSERT INTO USERS(LOGIN, PASSWORD)\n" +
                "VALUES(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login[0]);
            statement.setString(2, login[1]);
            int rows = statement.executeUpdate();
            if(rows == 1) return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Integer add(Route route) {
        String additional = "";
        String additionalq = "";
        //System.out.println(route.toFormatString());
        if(route.getDistance() != null) {additional += ", DISTANCE"; additionalq += ", ?";}
        if(route.getFrom() != null) {additional += ", FROM_X, FROM_Y, FROM_Z, FROM_NAME"; additionalq += ", ?, ?, ?, ?";}
        String query = "INSERT INTO ROUTE(NAME, CREATION_DATE," +
        " COORDINATES_X, COORDINATES_Y, TO_X, TO_Y, TO_Z, TO_NAME, USER_ID" + additional + ")\n " +
                "VALUES(?, NOW(), ?, ?, ?, ?, ?, ?, ?" + additionalq + ")";
        //System.out.println(query);
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, route.getName());
            statement.setLong(2, route.getCoordinates().getX());
            statement.setLong(3, route.getCoordinates().getY());
            statement.setInt(4, route.getTo().getX());
            statement.setDouble(5, route.getTo().getY());
            statement.setDouble(6, route.getTo().getZ());
            statement.setString(7, route.getTo().getName());
            statement.setInt(8, route.getUser());
            if(route.getDistance() != null) {
                statement.setFloat(9, route.getDistance());
                if(route.getFrom() != null) {
                    statement.setInt(10, route.getFrom() == null ? null : route.getFrom().getX());
                    statement.setDouble(11, route.getFrom() == null ? null : route.getTo().getY());
                    statement.setDouble(12, route.getFrom() == null ? null : route.getTo().getZ());
                    statement.setString(13, route.getFrom() == null ? null : route.getTo().getName());
                }
            }
            else if(route.getFrom() != null){
                statement.setInt(9, route.getFrom() == null ? null : route.getFrom().getX());
                statement.setDouble(10, route.getFrom() == null ? null : route.getTo().getY());
                statement.setDouble(11, route.getFrom() == null ? null : route.getTo().getZ());
                statement.setString(12, route.getFrom() == null ? null : route.getTo().getName());
            }
            int rows = statement.executeUpdate();
            if(rows == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    return resultSet.getInt("ID");
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean remove(Integer routeId) {
        String query = "DELETE FROM ROUTE WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, routeId);
            int rows = statement.executeUpdate();
            if(rows == 1) return true;
            return false;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Route route) {
        String query = "UPDATE ROUTE\n" +
                "SET NAME = ?, CREATION_DATE = ?, COORDINATES_X = ?, COORDINATES_Y = ?, TO_X = ?, TO_Y = ?, TO_Z = ?," +
                " TO_NAME = ?, DISTANCE = ?, FROM_X = ?, FROM_Y = ?, FROM_Z = ?, FROM_NAME = ?\n" +
                "WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            SimpleDateFormat datetimeFormatter = new SimpleDateFormat(
                    "yyyy-MM-dd");
            statement.setString(1, route.getName());
            statement.setTimestamp(2, new Timestamp(datetimeFormatter.parse(route.getCreationDate().toString()).getTime()));
            statement.setLong(3, route.getCoordinates().getX());
            statement.setLong(4, route.getCoordinates().getY());
            statement.setInt(5, route.getTo().getX());
            statement.setDouble(6, route.getTo().getY());
            statement.setDouble(7, route.getTo().getZ());
            statement.setString(8, route.getTo().getName());
            if(route.getDistance() == null) {
                statement.setNull(9, Types.FLOAT);
            }
            else {
                statement.setFloat(9, route.getDistance());
            }
            if(route.getFrom() == null) {
                statement.setNull(10, Types.INTEGER);
                statement.setNull(11, Types.DOUBLE);
                statement.setNull(12, Types.DOUBLE);
                statement.setNull(13, Types.VARCHAR);
            }
            else {
                statement.setInt(10, route.getFrom().getX());
                statement.setDouble(11, route.getFrom().getY());
                statement.setDouble(12, route.getFrom().getZ());
                statement.setString(13, route.getFrom().getName());
            }
            statement.setInt(14, route.getId());
            int rows = statement.executeUpdate();
            if(rows == 1) return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
