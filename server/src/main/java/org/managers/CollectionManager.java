package org.managers;

import org.unrealfff.models.Route;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Класс для управления коллекцией
 */
public class CollectionManager {
    private Integer currentId = 1;
    private Set<Route> collection = ConcurrentHashMap.newKeySet();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.fileManager = fileManager;
    }

    public boolean update(Route route) {
        if (!isContain(route)) return false;
        if (fileManager.update(route)) {
            this.collection.remove(getById(route.getId()));
            this.collection.add(route);
            return true;
        }
        return false;
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекция.
     */
    public Set<Route> getCollection() {
        return collection;
    }

    /**
     * Получить route по id
     */
    public Route getById(Integer id) {
        var iter = collection.iterator();
        while (iter.hasNext()) {
            Route route = iter.next();
            if(route.getId() == id) return route;
        }
        return  null;
    }

    /**
     * Содержит ли колекции route
     */
    public boolean isContain(Route e) { return e == null || getById(e.getId()) != null; }

    /**
     * Получить свободный id
     */
    public int getFreeId() {
        currentId = getNextId();
        return currentId;
    }

    /**
     * Добавляет route
     */
    public boolean add(Route a) {
        if(a == null) return false;
        //System.out.println(a);
        Route route = new Route(a.getId(), a.getName(), a.getCoordinates(), a.getCreationDate(), a.getFrom(),
                a.getTo(), a.getDistance(), a.getUser());
        if (isContain(route) || !route.validate()) return false;
        collection.add(route);
        currentId = a.getId() + 1;
        return true;
    }

    /**
     * Удаляет route по id
     */
    public boolean remove(Integer id, Integer user) {
        var a = getById(id);
        if (a == null || !a.getUser().equals(user)) return false;
        if (fileManager.remove(a.getId())) {
            collection = collection.stream()
                    .filter(route -> !route.getId().equals(id))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        if (collection.isEmpty()) {
            resetIdCounter();
        }
        return true;
    }

    private void resetIdCounter() {
        this.currentId = 1;
    }

    public boolean clear(Integer user) {
        boolean sucsess = true;
        var criminals = collection.stream().filter(route -> route.getUser().equals(user))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for(var i : criminals){
            if(fileManager.remove(i.getId())) collection.remove(i);
            else sucsess = false;
        }
        currentId = 0;
        return sucsess;
    }

    public boolean init() {
        collection.clear();
        fileManager.readCollection(collection);
        lastInitTime = LocalDateTime.now();
        boolean hasConflict = collection.stream()
                .anyMatch(e -> getById(e.getId()) == null);
        if(hasConflict) {
            collection.clear();
            return false;
        }
        //collection.forEach(r -> System.out.println(r.toFormatString()));
        //System.out.println("\n\n\n");
        this.currentId = getNextId();
        sort();
        //collection.forEach(r -> System.out.println(r.toFormatString()));
        return true;
    }
    private Integer getNextId() {
        return collection.stream()
                .mapToInt(Route::getId).max()
                .orElse(0) + 1;
    }

    public LinkedHashSet<Route> sortedByName() {
        return collection.stream().sorted(Comparator.comparingInt(r -> r.getName().length()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    /**
     * Сохраняет коллекцию в файл
     */
    public void save() {
        //fileManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "collection is empty";
        return sortedByName().stream()
                .map(Route::toFormatString)
                .collect(Collectors.joining("\n\n"))
                .trim();
    }

        public void sort() {
            LinkedHashSet<Route> nulls = collection.stream()
                    .filter(r -> r.getDistance() == null)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            LinkedHashSet<Route> notNulls = collection.stream().filter(route -> route.getDistance() != null)
                    .sorted((r1, r2) -> Float.compare(r1.getDistance(), r2.getDistance()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            collection = Stream.concat(notNulls.stream(), nulls.stream()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}