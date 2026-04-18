package io.github.epicer12.smart_campus_api.store;

/**
 *
 * @author Hasun
 */
import io.github.epicer12.smart_campus_api.models.Room;
import io.github.epicer12.smart_campus_api.models.Sensor;
import io.github.epicer12.smart_campus_api.models.SensorReading;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    // Rooms Storage
    public static Map<String, Room> rooms = new ConcurrentHashMap<>();
    
    // Sensors Storage
    public static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    
    // Readings Storage
    public static Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();
}
