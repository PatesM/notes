import lombok.extern.java.Log;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.maxqa.model.Note;
import org.maxqa.service.NoteServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@Log
@DisplayName("Тестирование реализации класса NoteService")
public class NoteServiceTest {

    private final NoteServiceImpl noteService = new NoteServiceImpl();
    private final InputStream inputStream = System.in;

    @BeforeEach
    void setup(final TestInfo info) {
        final Set<String> testTags = info.getTags();
        if (testTags.stream().anyMatch(tag -> tag.equals("skipBeforeEach"))) {
            return;
        }
        ByteArrayInputStream in = new ByteArrayInputStream("Test note number one\ntest one".getBytes());
        System.setIn(in);
        noteService.noteNew();
        System.setIn(inputStream);
    }

    @AfterEach
    public void cleanUpEach() {
        Note.getNotes().clear();
        Note.resetId();
    }

    static Stream<Arguments> createNewNoteWithFilledFields_argsProviderFactory() {
        return Stream.of(
                Arguments.of(
                        "One\nt",
                        List.of(
                                """
                                        1 One
                                        T

                                        ===================
                                        """
                        ),
                        "Создание заметки со всеми заполненными полями"
                )
        );
    }

    @Tag("skipBeforeEach")
    @DisplayName("note-new")
    @ParameterizedTest(name = "{2}")
    @MethodSource("createNewNoteWithFilledFields_argsProviderFactory")
    public void createNewNoteWithFilledFields(String userInput, List<Note> expectedResult, String name) {
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        noteService.noteNew();
        assertEquals(expectedResult.toString(), Note.getNotes().toString());
        System.setIn(inputStream);
        log.info("Тест " + "\"" + name + "\"" + " - успешно пройден.\n");
    }

    static Stream<Arguments> createNewNoteWithId2_argsProviderFactory() {
        return Stream.of(
                Arguments.of(
                        "Test note number two\nTest two",
                        List.of(
                                """
                                        1 Test note number one
                                        TEST; ONE
                                                                                
                                        ===================
                                        """,
                                """
                                        2 Test note number two
                                        TEST; TWO
                                                                                
                                        ===================
                                        """
                        ),
                        "Создание заметки с id 2"
                )
        );
    }

    @DisplayName("note-new")
    @ParameterizedTest(name = "{2}")
    @MethodSource("createNewNoteWithId2_argsProviderFactory")
    public void createNewNoteWithId2(String userInput, List<Note> expectedResult, String name) {
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        noteService.noteNew();
        assertEquals(expectedResult.toString(), Note.getNotes().toString());
        System.setIn(inputStream);
        log.info("Тест " + "\"" + name + "\"" + " - успешно пройден.\n");
    }

    static Stream<Arguments> removeNoteWithId1_argsProviderFactory() {
        return Stream.of(
                Arguments.of(
                        "1",
                        List.of(
                                """
                                        2 Test note number two
                                        TEST; TWO
                                                                                
                                        ===================
                                        """
                        ),
                        "Удаление заметки с id 2"
                )
        );
    }

    @DisplayName("note-remove")
    @ParameterizedTest(name = "{2}")
    @MethodSource("removeNoteWithId1_argsProviderFactory")
    public void removeNoteWithId1(String userInput, List<Note> expectedResult, String name) {
        ByteArrayInputStream in1 = new ByteArrayInputStream("Test note number two\ntest two".getBytes());
        System.setIn(in1);
        noteService.noteNew();
        System.setIn(inputStream);
        ByteArrayInputStream in2 = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in2);
        noteService.noteRemove();
        assertEquals(expectedResult.toString(), Note.getNotes().toString());
        System.setIn(inputStream);
        log.info("Тест " + "\"" + name + "\"" + " - успешно пройден.\n");
    }

    static Stream<Arguments> createNewNoteTextField_argsProviderFactory() {
        return Stream.of(
                Arguments.of(
                        "\n",
                        "Создание заметки с текстом в 0 символов"
                ),
                Arguments.of(
                        "No",
                        "Создание заметки с текстом в 2 символа"
                ),
                Arguments.of(
                        "Test note number one\n\n",
                        "Создание заметки с лейблом в 0 символов"
                ),
                Arguments.of(
                        "Test note number two\n!",
                        "Создание заметки с лейблом в 1 символ"
                )
        );
    }

    @Tag("skipBeforeEach")
    @DisplayName("note-new")
    @ParameterizedTest(name = "{1}")
    @MethodSource("createNewNoteTextField_argsProviderFactory")
    public void createNewNote_shouldTrowIllegalArgumentException(String userInput, String name) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
            System.setIn(in);
            noteService.noteNew();
            System.setIn(inputStream);
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertTrue(Note.getNotes().isEmpty());
        log.info("Тест " + "\"" + name + "\"" + " - успешно пройден.\n");
    }

    static Stream<Arguments> removeNote_argsProviderFactory() {
        return Stream.of(
                Arguments.of(
                        "\n",
                        "Удаление заметки с пустым id"
                ),
                Arguments.of(
                        "one",
                        "Удаление заметки с id one"
                )
        );
    }

    @Tag("skipBeforeEach")
    @DisplayName("note-new")
    @ParameterizedTest(name = "{1}")
    @MethodSource("removeNote_argsProviderFactory")
    public void removeNote_shouldTrowRuntimeException(String userInput, String name) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
            System.setIn(in);
            noteService.noteRemove();
            System.setIn(inputStream);
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertTrue(Note.getNotes().isEmpty());
        log.info("Тест " + "\"" + name + "\"" + " - успешно пройден.\n");
    }
}
