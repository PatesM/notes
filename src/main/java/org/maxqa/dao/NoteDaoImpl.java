package org.maxqa.dao;

import lombok.extern.java.Log;
import org.maxqa.model.Note;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
public class NoteDaoImpl implements NoteDao {

    private static Note removeNote;

    public NoteDaoImpl() {

    }

    @Override
    public void help() {
        log.fine("Вызвана команда " + Commands.help.name());
        log.info(Commands.getDescription());
    }

    @Override
    public void noteNew() {
        log.fine("Вызвана команда " + Commands.noteNew.name());
        log.info("Введите заметку\n");
        Scanner userInput = new Scanner(System.in);
        String userNote = userInput.nextLine();
        Note note = new Note();
        try {
            if (userNote.length() >= 3) {
                note.setNote(userNote);
            } else {
                log.warning("Текст заметки должен быть длиннее 3 символов, введено - " + userNote.length() + "\n");
                throw new IllegalArgumentException();
            }
            log.info("Добавить метки? " +
                    "Метки состоят из одного слова и могут содержать только буквы. " +
                    "Для добавления нескольких меток разделяйте слова пробелом\n");
            String[] userLabels = userInput.nextLine().toUpperCase().split(" ");
            List<String> labels = new ArrayList<>(Arrays.asList(userLabels));
            note.setLabels(labels);
            if (checkLabel(labels)) {
                log.warning("Метки должны состоять из одного слова и могут содержать только буквы\n");
                throw new IllegalArgumentException();
            } else {
                note.generateId();
                Note.addNoteToList(note);
                log.info("Заметка добавлена\n");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void noteList() {
        log.fine("Вызвана команда " + Commands.noteList.name());
        log.info("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок\n");
        Scanner userInput = new Scanner(System.in);
        String[] userLabels = userInput.nextLine().toUpperCase().split(" ");
        List<String> labels = new ArrayList<>(Arrays.asList(userLabels));
        List<Note> notes;
        StringBuilder sb = new StringBuilder();
        try {
            if (checkLabel(labels)) {
                throw new IllegalArgumentException();
            } else {
                notes = Note.getNotes();
            }
            for (Note note : notes) {
                if (new HashSet<>(note.getLabels()).containsAll(labels)) {
                    sb.append(note);
                }
            }
        } catch (IllegalArgumentException e) {
            log.warning("Метки должны состоять из одного слова и могут содержать только буквы\n");
        }
        System.out.println(sb);
    }

    @Override
    public void noteRemove() {
        log.fine("Вызвана команда " + Commands.noteRemove.name());
        log.info("Введите id удаляемой заметки\n");
        Scanner userInput = new Scanner(System.in);
        String userNoteId = userInput.nextLine();
        try {
            if (!userNoteId.matches("\\d+")) {
                log.warning("id должен быть числом\n");
                throw new IllegalArgumentException();
            } else {
                int id = Integer.parseInt(userNoteId);
                if (checkId(id)) {
                    removeNote = Note.getNotes().stream().
                            filter(note -> id == note.getId())
                            .findFirst().orElse(null);
                    Note.getNotes().remove(removeNote);
                    log.info("Заметка удалена\n");
                } else {
                    log.warning("id не найден\n");
                    throw new RuntimeException();
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void noteExport() {
        log.fine("Вызвана команда " + Commands.noteExport.name());
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        String exportDateTime = dateFormat.format(new Date());
        String fileName = "notes_" + exportDateTime + ".txt";
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (Note note : Note.getNotes()) {
                writer.write(note.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exit() {
        log.fine("Вызвана команда " + Commands.exit.name());
        System.exit(0);
    }

    public static boolean checkLabel(List<String> labels) {
        boolean bool = false;
        for (String label : labels) {
            if (!label.matches("^[a-zA-Z]+$")) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    public static boolean checkId(int id) {
        boolean bool = false;
        for (Note note : Note.getNotes()) {
            if (note.getId() == id) {
                bool = true;
                removeNote = new Note(note.getNote(), note.getLabels().toString());
            }
        }
        return bool;
    }
}
