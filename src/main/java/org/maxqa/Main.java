package org.maxqa;

import org.maxqa.service.NoteServiceImpl;

public class Main {
    public static void main(String[] args) {
        NoteServiceImpl noteService = new NoteServiceImpl();
        noteService.start();
    }
}
