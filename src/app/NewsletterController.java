package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import http.Request;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;

public class NewsletterController extends Controller {

    private static List<Map<String, String>> quotes = new ArrayList<>();

    public NewsletterController(){
        super(null);

    }
    public NewsletterController(Request request) {
        super(request);
        this.request = request;
    }

    @Override
    public Response doGet() {
        StringBuilder htmlBodyBuilder = new StringBuilder();
        htmlBodyBuilder.append("<form method=\"POST\" action=\"/add-quote\">");
        htmlBodyBuilder.append("<label>Quote: </label><input name=\"quote\" type=\"text\"><br><br>");
        htmlBodyBuilder.append("<label>Author: </label><input name=\"author\" type=\"text\"><br><br>");
        htmlBodyBuilder.append("<button>Submit</button>");
        htmlBodyBuilder.append("</form>");
        htmlBodyBuilder.append("<br>");

        htmlBodyBuilder.append("<h2>Quotes</h2>");
        for (Map<String, String> quoteMap : quotes) {
            System.out.println("quoteandauthor" + quoteMap);
            String quote = quoteMap.get("quote");
            quote = quote.substring(6);
            String author = quoteMap.get("author");
            author = author.substring(7);
            htmlBodyBuilder.append("<blockquote>");
            htmlBodyBuilder.append("<p>Quote: <i>'").append(quote.toString()).append("'</i> </p>");
            htmlBodyBuilder.append("<footer>~ <b>").append(author).append("</b></footer>");
            htmlBodyBuilder.append("</blockquote>");
        }

        try{
            Socket socket = new Socket("localhost", 9000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder quoteBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                quoteBuilder.append(line);
            }
            String jsonStr = quoteBuilder.toString();
            System.out.println(jsonStr);
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(jsonStr, JsonObject.class);
            String quote = json.get("quote").getAsString();
            System.out.println("Quote of the day: " + quote);
            socket.close();
            htmlBodyBuilder.append("<h2>Quote of the day: </h2>");
            htmlBodyBuilder.append("<blockquote>");
            htmlBodyBuilder.append("<p>").append(quote).append("</p");
            htmlBodyBuilder.append("</blockquote>");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String content = "<html><head><title>Odgovor servera</title></head>\n";
        content += "<body>" + htmlBodyBuilder.toString() + "</body></html>";

        return new HtmlResponse(content);
    }

    public void addQuote(String quote, String author) {
        Map<String, String> quoteMap = new HashMap<>();
        quoteMap.put("quote", quote);
        quoteMap.put("author", author);
        quotes.add(quoteMap);
    }

    @Override
    public Response doPost() {

        return new RedirectResponse("/quotes");
    }
}
