package com.pipplware.teixeiras.network.models;

/**
 * Created by teixeiras on 07/04/15.
 */
public class Info {
    private String websocket_port;
    private String bonjour_actice;
    private String webservice_actice;
    private String pipCec_actice;
    private String websocketserver_actice;
    private String token;

    public String getWebsocket_port() {
        return websocket_port;
    }

    public void setWebsocket_port(String websocket_port) {
        this.websocket_port = websocket_port;
    }

    public String getBonjour_actice() {
        return bonjour_actice;
    }

    public void setBonjour_actice(String bonjour_actice) {
        this.bonjour_actice = bonjour_actice;
    }

    public String getWebservice_actice() {
        return webservice_actice;
    }

    public void setWebservice_actice(String webservice_actice) {
        this.webservice_actice = webservice_actice;
    }

    public String getPipCec_actice() {
        return pipCec_actice;
    }

    public void setPipCec_actice(String pipCec_actice) {
        this.pipCec_actice = pipCec_actice;
    }

    public String getWebsocketserver_actice() {
        return websocketserver_actice;
    }

    public void setWebsocketserver_actice(String websocketserver_actice) {
        this.websocketserver_actice = websocketserver_actice;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
