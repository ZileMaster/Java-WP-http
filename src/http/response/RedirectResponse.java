package http.response;

public class RedirectResponse extends Response {


    private String location;

    public RedirectResponse(String location) {
        this.location = location;
    }

    @Override
    public String getResponseString() {
        String response = "HTTP/1.1 301 redirect\r\nLocation: " + this.location +"\r\n\r\n";

        return response;
    }
}
