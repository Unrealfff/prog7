package org.unrealfff.commands;

import org.unrealfff.utility.Response;

/**
 * Интерфейс для всех выполняемых команд
 */
public interface Executable {
    /**
     * Выполнить что-либо.
     *
     * @param arguments Аргумент для выполнения
     * @return результат выполнения
     */
    default Response execute(String[] arguments, String[] login) {return new Response("");};
    default Response execute(String[] arguments) {return new Response("");};
}