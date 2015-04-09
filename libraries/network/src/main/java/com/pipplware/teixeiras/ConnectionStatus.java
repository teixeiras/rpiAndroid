package com.pipplware.teixeiras;

import java.util.Observable;

/**
 * Created by teixeiras on 09/04/15.
 */
public class ConnectionStatus extends Observable{

    public boolean isWebserviceAvailable() {
        return isWebserviceAvailable;
    }

    public void setWebserviceAvailable(boolean isWebserviceAvailable) {
        this.isWebserviceAvailable = isWebserviceAvailable;
        this.setChanged();
        this.notifyObservers();
    }

    public boolean isWebSocketAvailable() {
        return isWebSocketAvailable;
    }

    public void setWebSocketAvailable(boolean isWebSocketAvailable) {
        this.isWebSocketAvailable = isWebSocketAvailable;
        this.setChanged();
        this.notifyObservers();
    }

    boolean isWebserviceAvailable;
    boolean isWebSocketAvailable;

    static ConnectionStatus instance = new ConnectionStatus();

    public static ConnectionStatus sharedInstance() {
        return instance;
    }


}
