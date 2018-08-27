package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import vk.Conversation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

class RequestHandler extends AbstractHandler {

    private final static String CONFIRMATION_TYPE = "confirmation";
    private final static String MESSAGE_TYPE = "message_new";
    private final static String OK_BODY = "ok";

    private final LogicModule logicHandler;
    private final String confirmationCode;
    private final Gson gson;

    RequestHandler(LogicModule handler, String confirmationCode) {
        this.logicHandler = handler;
        this.confirmationCode = confirmationCode;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            throw new ServletException("This method is unsupported");
        }

        Reader reader = request.getReader();

        try {
            JsonObject requestJson = gson.fromJson(reader, JsonObject.class);
            System.out.println(requestJson);

            String type = requestJson.get("type").getAsString();

            if (type == null || type.isEmpty()) throw new ServletException("No type in json");

            final String responseBody;
            switch (type) {
                case CONFIRMATION_TYPE:
                    responseBody = confirmationCode;
                    break;
                case MESSAGE_TYPE:
                    JsonObject object = requestJson.getAsJsonObject("object");
                    long userId = object.getAsJsonPrimitive("peer_id").getAsInt();
                    String message = object.get("text").getAsString();
                    logicHandler.handle(new Conversation(userId), message);
                    responseBody = OK_BODY;
                    break;
                default:
                    responseBody = OK_BODY; // in case we get another event
                    break;
            }

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println(responseBody);
            System.out.println("Send answer to vk");
        } catch (JsonParseException e) {
            throw new ServletException("Incorrect json", e);
        }
    }
}
