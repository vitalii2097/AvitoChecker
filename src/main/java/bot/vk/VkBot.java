package bot.vk;

import bot.Bot;
import checker.AvitoChecker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VkBot extends Bot {
    private final int groupId = 170549975;
    private final String accessToken = "75aadc0f278b4608ad5fa5baca2ab0aab9d6f45f0573c389455dcebc0bd620968d61119aef09333e4b546";
    private final static Random random = new Random();
    private final VkApiClient apiClient;
    private final GroupActor actor;
    private final HttpTransportClient client;
    private final Gson gson = new Gson();

    public VkBot(AvitoChecker avitoChecker) throws Exception {
        super(avitoChecker);
        client = new HttpTransportClient();
        apiClient = new VkApiClient(client);
        actor = new GroupActor(groupId, accessToken);

        Server server = new Server(80);
        server.setHandler(new RequestHandler("ee825fd8", this));

        server.start();
        server.join();
    }

    private String uploadFile(File file, String url) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        FileBody uploadFilePart = new FileBody(file);
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("photo", uploadFilePart);
        httpPost.setEntity(reqEntity);

        InputStream is = httpclient.execute(httpPost).getEntity().getContent();

        StringBuilder answer = new StringBuilder();

        while (is.available() > 0) {
            answer.append((char) is.read());
        }

        return answer.toString();
    }

    private String uploadImageToVk(File image) throws Exception {
        PhotoUpload photoUpload = apiClient.photos()
                .getMessagesUploadServer(actor)
                .execute();

        String answer = uploadFile(image, photoUpload.getUploadUrl());

        JsonObject requestJson = gson.fromJson(answer, JsonObject.class);

        JsonObject response = gson.fromJson(apiClient.photos()
                .saveMessagesPhoto(actor, requestJson.get("photo").getAsString())
                .server(requestJson.get("server").getAsInt())
                .hash(requestJson.get("hash").getAsString())
                .executeAsString(), JsonObject.class);

        JsonArray photos = response.getAsJsonArray("response");
        if (photos.size() == 0) {
            throw new IOException();
        }

        JsonObject photo = photos.get(0).getAsJsonObject();
        return "photo"
                + photo.get("owner_id").getAsString()
                + "_"
                + photo.get("id").getAsString();

    }

    void sendMessage(VkConversation conversation, String text, List<File> images) {
        List<String> vkUrls = new ArrayList<>();

        for (File image : images) {
            try {
                vkUrls.add(uploadImageToVk(image));
            } catch (IOException e) {
                System.out.println("Не удалось загрузить фотографию");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                image.delete();
            }
        }

        try {
            apiClient.messages()
                    .send(actor)
                    .message(text)
                    .attachment(vkUrls)
                    .peerId((int) conversation.getId())
                    .randomId(random.nextInt())
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

}
