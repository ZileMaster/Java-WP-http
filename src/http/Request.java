package http;

import http.HttpMethod;

public class Request {

    private final HttpMethod httpMethod;

    private final String path;

    public Request(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }
}
