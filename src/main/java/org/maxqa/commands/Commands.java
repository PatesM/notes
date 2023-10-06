package org.maxqa.commands;

import lombok.Getter;

public enum Commands {
    help("help", "help - выводит на экран список доступных команд с их описанием;"),
    noteNew("note-new", "note-new - создать новую заметку;"),
    noteList("note-list", "note-list - выводит все заметки на экран;"),
    noteRemove("note-remove", "note-remove - удаляет заметку;"),
    noteExport("note-export", "note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла;"),
    exit("exit", "exit - выход из приложения.");

    private final String descriptionCommand;

    @Getter
    private final String nameCommand;

    Commands(String nameCommand, String descriptionCommand) {
        this.nameCommand = nameCommand;
        this.descriptionCommand = descriptionCommand;
    }

    public static String getDescription() {
        return help.descriptionCommand + "\n" +
                "      " + noteNew.descriptionCommand + "\n" +
                "      " + noteList.descriptionCommand + "\n" +
                "      " + noteRemove.descriptionCommand + "\n" +
                "      " + noteExport.descriptionCommand + "\n" +
                "      " + exit.descriptionCommand + "\n";
    }
}
