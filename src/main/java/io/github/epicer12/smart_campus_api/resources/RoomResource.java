/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.epicer12.smart_campus_api.resources;

import io.github.epicer12.smart_campus_api.exceptions.RoomNotEmptyException;
import io.github.epicer12.smart_campus_api.models.ErrorResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import io.github.epicer12.smart_campus_api.store.DataStore;
import io.github.epicer12.smart_campus_api.models.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Response;

/**
 *
 * @author Hasun
 */

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    @GET
    public Response getAllRooms() {
        List<Map<String, Object>> summaryList = new ArrayList<>();
        
        for (Room room : DataStore.rooms.values()) {
            Map<String, Object> roomSummary = new HashMap<>();
            roomSummary.put("id", room.getId());
            roomSummary.put("name", room.getName());
            roomSummary.put("href", "/api/v1/rooms/" + room.getId());
            summaryList.add(roomSummary);
        }
        return Response.ok(summaryList).build();
    }
    
    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", "Room ID is required");
            return Response.status(400).entity(error).build();
        }
        
        if (DataStore.rooms.containsKey(room.getId())) {
            ErrorResponse error = new ErrorResponse(409, "Conflict", "Room with ID " + room.getId() + " already exists");
            return Response.status(409).entity(error).build();
        }
        
        DataStore.rooms.put(room.getId(), room);
        
        return Response.status(201)
                .entity(room)
                .header("Location", "/api/v1/rooms/" + room.getId())
                .build();
    }
    
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        
        if (room == null) {
            ErrorResponse error = new ErrorResponse(404, "Not Found", "Room with ID " + roomId + " was not found");
            return Response.status(404).entity(error).build();
        }
        return Response.ok(room).build();
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        
        if (room == null) {
            ErrorResponse error = new ErrorResponse(404, "Not Found", "Room with ID " + roomId + " was not found");
            return Response.status(404).entity(error).build();
        }
        
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room with the ID " + roomId + " already has sensors");
        }
        
        DataStore.rooms.remove(roomId);
        return Response.status(204).build();
    }
}
