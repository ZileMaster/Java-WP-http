package app;

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

    public NewsletterController(Request request) {
        super(request);
        this.request = request;
    }

    @Override
    public Response doGet() {
        StringBuilder htmlBodyBuilder = new StringBuilder();
        htmlBodyBuilder.append("<form method=\"POST\" action=\"/add\">");
        htmlBodyBuilder.append("<label>Quote: </label><input name=\"quote\" type=\"text\"><br><br>");
        htmlBodyBuilder.append("<label>Author: </label><input name=\"author\" type=\"text\"><br><br>");
        htmlBodyBuilder.append("<button>Submit</button>");
        htmlBodyBuilder.append("</form>");
        htmlBodyBuilder.append("<br>");

        htmlBodyBuilder.append("<h2>Quotes</h2>");
        for (Map<String, String> quoteMap : quotes) {
            String quote = quoteMap.get("quote");
            String author = quoteMap.get("author");
            htmlBodyBuilder.append("<blockquote>");
            htmlBodyBuilder.append("<p>").append(quote).append("</p>");
            htmlBodyBuilder.append("<footer>").append(author).append("</footer>");
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
            String quote = quoteBuilder.toString();
            System.out.println("ovojequotesapomocnog" + quote);
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

    @Override
    public Response doPost() {
        String params = request.toString();

        Map<String, String> paramMap = new HashMap<>();
        StringTokenizer st = new StringTokenizer(params, "&");
        while( st.hasMoreTokens() ){
            String pair = st.nextToken();
            int idx = pair.indexOf("=");
            if (idx >= 0) {
                try {
                    paramMap.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(1 + idx), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Map<String, String> quoteMap = new HashMap<>();
        quoteMap.put("quote", paramMap.get("quote"));
        quoteMap.put("author", paramMap.get("author"));
        quotes.add(quoteMap);

        return new RedirectResponse("/newsletter");
    }
}
