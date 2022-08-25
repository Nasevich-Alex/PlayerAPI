package utils;

import aquality.selenium.browser.AqualityServices;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.Player;
import org.json.JSONObject;
import java.util.Properties;

public class PlayerControllerApi {
    protected static Properties properties = ReaderDataProperties.loadProperties();
    public static final String API_URL = properties.getProperty("urlApi");
    public static final String ALL_PLAYERS = API_URL + "get/all";
    public static final String PLAYER_BY_ID = API_URL + "get";
    public static final String DELETE_BY_ID = API_URL + "/delete/";
    public static final String CREATE = API_URL + "/create/";
    public static final String UPDATE = API_URL + "/update/";

    public static HttpResponse<JsonNode> getAllPlayers() {
        try {
            return ApiUtils.makeGetRequest(ALL_PLAYERS).asJson();
        } catch (UnirestException e) {
            AqualityServices.getLogger().warn("Something gone wrong with getting all players");
            throw new RuntimeException();
        }
    }

    public static HttpResponse<JsonNode> getPlayerById(int id) {
        try {
            JSONObject playerId = new JSONObject();
            playerId.put("playerId", id);

            return ApiUtils.makePostRequestWithBody(PLAYER_BY_ID)
                    .header("content-type", "application/json").body(playerId).asJson();
        } catch (UnirestException e) {
            AqualityServices.getLogger().warn("Something gone wrong with make post request");
            throw new RuntimeException();
        }
    }

    public static HttpResponse<JsonNode> deletePlayerById(Roles role, int id) {
        try {
            JSONObject playerId = new JSONObject();
            playerId.put("playerId", id);

            return ApiUtils.makeDeleteRequest(DELETE_BY_ID + role.getRole())
                    .header("content-type", "application/json").body(playerId).asJson();
        } catch (UnirestException e) {
            AqualityServices.getLogger().warn("Something gone wrong with make post request");
            throw new RuntimeException();
        }
    }

    public static HttpResponse<JsonNode> createPlayer(Roles editor, Player player) {
        try {
            return ApiUtils.makeGetRequest(CREATE + editor.getRole() + "?").queryString(player.getParameters()).asJson();
        } catch (UnirestException e) {
            AqualityServices.getLogger().warn("Something gone wrong with creating player");
            throw new RuntimeException();
        }
    }

    public static HttpResponse<JsonNode> updatePlayer(Roles editor, int id, Player player) {
        JSONObject updatedPlayer = new JSONObject();
        updatedPlayer.put("age", player.getAge());
        updatedPlayer.put("gender", player.getGender());
        updatedPlayer.put("login", player.getLogin());
        updatedPlayer.put("password", player.getPassword());
        updatedPlayer.put("screenName", player.getScreenName());

        try {
            return ApiUtils.makePatchRequest(UPDATE + editor.getRole() + "/" + id)
                    .header("content-type", "application/json").body(updatedPlayer).asJson();
        } catch (UnirestException e) {
            AqualityServices.getLogger().warn("Something gone wrong with updating player");
            throw new RuntimeException();
        }
    }
}