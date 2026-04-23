package io.github.epicer12.smart_campus_api.store;

/**
 *
 * @author Hasun
 */
import io.github.epicer12.smart_campus_api.models.Room;
import io.github.epicer12.smart_campus_api.models.Sensor;
import io.github.epicer12.smart_campus_api.models.SensorReading;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DataStore {
    // Rooms Storage
    public static Map<String, Room> rooms = Collections.synchronizedMap(new LinkedHashMap<>());
    
    // Sensors Storage
    public static Map<String, Sensor> sensors = Collections.synchronizedMap(new LinkedHashMap<>());
    
    // Readings Storage
    public static Map<String, List<SensorReading>> sensorReadings = Collections.synchronizedMap(new LinkedHashMap<>());
}
