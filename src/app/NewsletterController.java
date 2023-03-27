package app;

import http.Request;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class NewsletterController extends Controller {

    public NewsletterController(Request request) {
        super(request);
        this.request = request;
    }

    @Override
    public Response doGet() {
        String htmlBody = "" +
                "<form method=\"POST\" action=\"/add\">" +
                "<label>Quote: </label><input name=\"quote\" type=\"text\"><br><br>" +
                "<label>Author: </label><input name=\"author\" type=\"text\"><br><br>" +
                "<button>Submit</button>" +
                "</form>";

        String content = "<html><head><title>Odgovor servera</title></head>\n";
        content += "<body>" + htmlBody + "</body></html>";

        return new HtmlResponse(content);
    }

    @Override
    public Response doPost() {
        String params = request.toString(); // Get the POST parameters from the request

//        // TODO: process the POST request parameters
//        String quote = params.get("quote");
//        String author = params.get("author");
//
//        // Print the parameters to the console
//        System.out.println("Received POST request with quote=" + quote + " and author=" + author);
        System.out.println("Received POST request: " + params);

        return new RedirectResponse("/newsletter");
    }
}
