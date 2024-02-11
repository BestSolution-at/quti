package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import at.bestsolution.qutime.BaseHandlerTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CreateHandlerTest extends BaseHandlerTest<CreateHandler> {

    @Inject
    public CreateHandlerTest(CreateHandler handler) {
        super(handler);
    }

    @Test
    public void testCreate() {
        var result = handler.create("Test Handler");
        assertNotNull(result, "Expected an UUID");
        assertDoesNotThrow(() -> UUID.fromString(result), "Result should have been an UUID");
    }

    @Test
    public void testFail() {
        assertThrows(NullPointerException.class, () -> handler.create(null));
    }
}
