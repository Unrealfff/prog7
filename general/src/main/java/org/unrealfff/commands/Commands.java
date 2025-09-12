package org.unrealfff.commands;

import java.io.Serializable;

/**
 * Перечисление для хранения всех типов команд
 */
public enum Commands implements Serializable {
    Help("help", false),
    Info("info"),
    Show("show"),
    Add("add", true, true),
    Update("update", true, true),
    RemoveById("remove_by_id"),
    Clear("clear"),
    Save("save"),
    ExecuteScript("execute_script"),
    Exit("exit", false),
    AddIfMin("add_if_min", true, true),
    RemoveLower("remove_lower", true, true),
    History("history", false),
    RemoveAnyByDistance("remove_any_by_distance"),
    CountGreaterThanDistance("count_greater_than_distance"),
    Register("register"),
    PrintFieldDescendingDistance("print_field_descending_distance");

    private String type;
    private boolean serverBased;
    private boolean needRoute;

    private Commands(String type) {
        this.type = type;
        this.serverBased = true;
        this.needRoute = false;
    }

    private Commands(String type, boolean serverBased) {
        this.type = type;
        this.serverBased = serverBased;
        this.needRoute = false;
    }

    private Commands(String type, boolean serverBased, boolean needRoute) {
        this.type = type;
        this.serverBased = serverBased;
        this.needRoute = needRoute;
    }

    public boolean needsRoute() {
        return needRoute;
    }

    public String Type(){
        return type;
    }

    public boolean serverBased() {return serverBased;}

    public static final long serialVersionUID = 14L;

    /**
     * Метод для выделения константы из строки
     * @param string строка с командой
     * @return соответствующяя константа
     */
    public static Commands get(String string) {
        try {
            String[] parts = string.split("_");
            StringBuilder enumName = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    enumName.append(part.substring(0, 1).toUpperCase())
                            .append(part.substring(1).toLowerCase());
                }
            }
            return Commands.valueOf(enumName.toString());
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }
}