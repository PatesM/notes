package org.maxqa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class Note {
    private static int initialId = 0;

    @Getter
    @Setter
    public static List<Note> notes = new ArrayList<>();

    private int id;

    private String note;

    private List<String> labels;

    public Note(String note, String labels) {
        this.note = note;
        this.labels = List.of(labels.toUpperCase().split(" "));
    }

    public void generateId() {
        this.id = initialId += 1;
    }

    public static void resetId() {
        initialId = 0;
    }

    public static void addNoteToList(Note note) {
        notes.add(note);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " " + note + "\n" + String.join("; ", labels) + "\n" + "\n===================\n";
    }
}
