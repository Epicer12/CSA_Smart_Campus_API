package io.github.epicer12.smart_campus_api.exceptions;

/**
 *
 * @author Hasun
 */
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}
