package at.bestsolution.qutime.handler.calendar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class CreateHandlerTest {
    @Inject
    CreateHandler handler;

    @Test
    public void testCreate() {
        var result = handler.create("Test Handler");
        assertNotNull(result, "Expected an UUID");
        assertDoesNotThrow(() -> UUID.fromString(result), "Result should have been an UUID");
    }
}
