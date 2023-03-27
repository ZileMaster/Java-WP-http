package app;

import http.HttpMethod;
import http.Request;
import http.response.Response;

public class RequestHandler {
    public Response handle(Request request) throws Exception {
        if (request.getPath().equals("/newsletter") && request.getHttpMethod().equals(HttpMethod.GET)) {
            return (new NewsletterController(request)).doGet();
        } else if (request.getPath().equals("/add") && request.getHttpMethod().equals(HttpMethod.POST)) {
            return (new NewsletterController(request)).doPost();
        }

        throw new Exception("Page: " + request.getPath() + ". Method: " + request.getHttpMethod() + " not found!");
    }
}
