package com.shy.app.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shy.app.websocket.model.Device;

@ApplicationScoped
public class ItemSessionHandler {
	private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Device> devices = new HashSet<>();
    
    public void addSession(Session session) {
    	sessions.add(session);
    	/*for (Items item : items) {
    		JSONObject addMessage = createAddMessage(item);
    		sendToSession(session, addMessage);
    	}*/
    }
    
    /*public void addSession(Session session) {
    	sessions.add(session);
    	for (Device device : devices) {
    		JsonObject addMessage = createAddMessage(device);
    		sendToSession(session, addMessage);
    	}
    }*/
    
    public void removeSession(Session session) {
    	sessions.remove(session);
    }
    
    public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }

    public void addDevice(Device device) {
    	device.setId(deviceId);
        devices.add(device);
        deviceId++;
        JsonObject addMessage = createAddMessage(device);
        //sendToAllConnectedSessions(addMessage);
    }

    public void removeDevice(int id) {
    	Device device = getDeviceById(id);
        if (device != null) {
            devices.remove(device);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            //sendToAllConnectedSessions(removeMessage);
        }
    }

    public void toggleDevice(int id) {
    	JsonProvider provider = JsonProvider.provider();
        Device device = getDeviceById(id);
        if (device != null) {
            if ("On".equals(device.getStatus())) {
                device.setStatus("Off");
            } else {
                device.setStatus("On");
            }
            JsonObject updateDevMessage = provider.createObjectBuilder()
                    .add("action", "toggle")
                    .add("id", device.getId())
                    .add("status", device.getStatus())
                    .build();
            //sendToAllConnectedSessions(updateDevMessage);
        }
    }

    private Device getDeviceById(int id) {
        for (Device device : devices) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }

    private JsonObject createAddMessage(Device device) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", device.getId())
                .add("name", device.getName())
                .add("type", device.getType())
                .add("status", device.getStatus())
                .add("description", device.getDescription())
                .build();
        return addMessage;
    }

    public void sendToAllConnectedSessions(JSONObject message) {
    	System.out.println("sendToAllConnectedSessions");
    	System.out.println(sessions.size());
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JSONObject message) {
        try {
            //session.getBasicRemote().sendText(message.toString());
            session.getRemote().sendString(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(ItemSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}