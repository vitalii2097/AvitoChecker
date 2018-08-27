package vk;

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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot {

    private final int groupId = 170549975;
    private final String accessToken = "75aadc0f278b4608ad5fa5baca2ab0aab9d6f45f0573c389455dcebc0bd620968d61119aef09333e4b546";
    private final static Random random = new Random();
    private final VkApiClient apiClient;
    private final GroupActor actor;
    private final HttpTransportClient client;
    private final Gson gson = new Gson();

    public Bot() {
        client = new HttpTransportClient();
        apiClient = new VkApiClient(client);

        actor = new GroupActor(groupId, accessToken);
    }

    private File downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();

        File file = new File(System.currentTimeMillis() + ".jpg");
        file.createNewFile();
        OutputStream os = new FileOutputStream(file);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();

        return file;
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

    private String uploadImageToVk(String url) throws Exception {
        File file = downloadImage(url);

        PhotoUpload photoUpload = apiClient.photos()
                .getMessagesUploadServer(actor)
                .execute();

        String answer = uploadFile(file, photoUpload.getUploadUrl());

        file.delete();

        JsonObject requestJson = gson.fromJson(answer, JsonObject.class);

        JsonObject response = gson.fromJson(apiClient.photos()
                .saveMessagesPhoto(actor, requestJson.get("photo").getAsString())
                .server(requestJson.get("server").getAsInt())
                .hash(requestJson.get("hash").getAsString())
                .executeAsString(), JsonObject.class);

        JsonArray photos = response.getAsJsonArray("response");
        JsonObject photo = photos.get(0).getAsJsonObject();

        return "photo"
                + photo.get("owner_id").getAsString()
                + "_"
                + photo.get("id").getAsString();

    }

    public void sendMessage(Conversation conversation, String text, List<String> imageUrls) {
        List<String> images = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            try {
                images.add(uploadImageToVk(imageUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            apiClient.messages()
                    .send(actor)
                    .message(text)
                    .attachment(images)
                    .peerId((int)conversation.getId())
                    .randomId(random.nextInt())
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

}
