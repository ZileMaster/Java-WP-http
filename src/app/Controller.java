package app;

import http.Request;
import http.response.Response;

public abstract class Controller {

    protected Request request;

    public Controller(Request request) {
        this.request = request;
    }

    public abstract Response doGet();
    public abstract Response doPost();
}
