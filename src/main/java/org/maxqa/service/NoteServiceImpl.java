package org.maxqa.service;

import lombok.extern.java.Log;
import org.maxqa.commands.Commands;
import org.maxqa.dao.NoteDaoImpl;

import java.util.Arrays;
import java.util.Scanner;

@Log
public class NoteServiceImpl implements NoteService {

    private final NoteDaoImpl noteDao = new NoteDaoImpl();
    private static int numberOfCallsStartMethod = 0;

    public NoteServiceImpl() {

    }

    public void start() {
        numberOfCallsStartMethod++;
        if (numberOfCallsStartMethod == 1) {
            log.info("""
                    Это Ваша записная книжка. Вот список доступных команд:
                          help,\s
                          note-new,\s
                          note-list,\s
                          note-remove,\s
                          note-export,\s
                          exit.
                                        
                    """);
        }
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        boolean isPresent = Arrays.stream(Commands.values()).anyMatch(command -> command.getNameCommand()
                .equals(userInput));
        try {
            if (isPresent) {
                switch (userInput) {
                    case "help" -> help();
                    case "note-new" -> noteNew();
                    case "note-list" -> noteList();
                    case "note-remove" -> noteRemove();
                    case "note-export" -> noteExport();
                    case "exit" -> exit();
                }
            } else {
                throw new RuntimeException();

            }
        } catch (RuntimeException e) {
            log.warning("Команда не найдена\n");
        }
        this.start();
    }

    @Override
    public void help() {
        noteDao.help();
    }

    @Override
    public void noteNew() {
        noteDao.noteNew();
    }

    @Override
    public void noteList() {
        noteDao.noteList();
    }

    @Override
    public void noteRemove() {
        noteDao.noteRemove();
    }

    @Override
    public void noteExport() {
        noteDao.noteExport();
    }

    @Override
    public void exit() {
        noteDao.exit();
    }
}
