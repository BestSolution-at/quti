package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.Json;

@QuarkusTest
public class UpdateHandlerTest extends CalendarHandlerTest<UpdateHandler> {

    @Inject
    public UpdateHandlerTest(UpdateHandler handler) {
        super(handler);
    }

    @Test
    public void testUpdateName() {
        var patch = Json.createPatchBuilder()
            .replace("name", "My Updated Calendar")
            .build();
        assertEquals("My Calendar", calendar(basicCalendarKey).name);
        assertTrue(handler.update(basicCalendarKey, patch).isOk());
        assertEquals("My Updated Calendar", calendar(basicCalendarKey).name);
    }

    @Test
    public void testUpdateMulti() {
        var patch = Json.createPatchBuilder()
            .replace("name", "My Updated Calendar")
            .add("owner", "testowner@bestsolution.at")
            .build();
        assertEquals("My Calendar", calendar(ownerlessCalendarKey).name);
        assertNull(calendar(ownerlessCalendarKey).owner);

        assertTrue(handler.update(ownerlessCalendarKey, patch).isOk());

        assertEquals("My Updated Calendar", calendar(ownerlessCalendarKey).name);
        assertEquals("testowner@bestsolution.at", calendar(ownerlessCalendarKey).owner);
    }

    @Test
    public void testFail() {
        assertThrows(NullPointerException.class, 
            () -> handler.update(null, null));
        assertThrows(NullPointerException.class, 
            () -> handler.update(basicCalendarKey, null));
    }
}
