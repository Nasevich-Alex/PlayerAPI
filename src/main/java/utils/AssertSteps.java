package utils;

import aquality.selenium.browser.AqualityServices;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import model.Player;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AssertSteps {
    public static void checkStatusCode(HttpResponse<JsonNode> response, Statuses status) {
        AqualityServices.getLogger().info("Check status code of response");
        Assert.assertEquals(response.getStatus(), status.getCode(), "Status is wrong");
    }

    public static void checkThatResponseHaveRightKeys(HttpResponse<JsonNode> response, JSONObject template) {
        AqualityServices.getLogger().info("Check that response have right keys");
        Set<String> responseKeys = response.getBody().getObject().keySet();
        Set<String> templateKeys = template.keySet();
        boolean isEqual = responseKeys.containsAll(templateKeys);
        Assert.assertTrue(isEqual, "Response haven't the same structure of keys");
    }

    public static void checkThatPlayerWasDeleted(int id) {
        AqualityServices.getLogger().info("Check that player was deleted (by id)");
        HttpResponse<JsonNode> allPlayers = PlayerControllerApi.getAllPlayers();
        JSONArray array = allPlayers.getBody().getObject().getJSONArray("players");
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            ids.add((Integer) array.getJSONObject(i).get("id"));
        }
        Assert.assertFalse(ids.contains(id), "Player wasn't deleted");
    }

    public static void checkThatPlayerWasCreated(Player player) {
        AqualityServices.getLogger().info("Check that player was created");
        HttpResponse<JsonNode> allPlayers = PlayerControllerApi.getAllPlayers();
        JSONArray array = allPlayers.getBody().getObject().getJSONArray("players");
        ArrayList<String> screenNames = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            screenNames.add((String) array.getJSONObject(i).get("screenName"));
        }
        Assert.assertTrue(screenNames.contains(player.getScreenName()), "Player wasn't created");
    }

    public static void checkThatAllPlayersHaveUniqueScreenName() {
        HttpResponse<JsonNode> allPlayers = PlayerControllerApi.getAllPlayers();
        JSONArray array = allPlayers.getBody().getObject().getJSONArray("players");
        ArrayList<String> screenNames = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            screenNames.add((String) array.getJSONObject(i).get("screenName"));
        }
        HashSet<String> uniqueScreenNames = new HashSet<>(screenNames);
        Assert.assertTrue(screenNames.size() == uniqueScreenNames.size(),
                "List of players contains non-unique screenNames");
    }

    public static void checkThatPlayerWasUpdated(Player oldPlayer, Player updatedPlayer) {
        AqualityServices.getLogger().info("Check that player was updated");
        Assert.assertEquals(oldPlayer, updatedPlayer, "Player wasn't updated");
    }

    public static void checkThatPlayerWasNotUpdated(Player oldPlayer, Player updatedPlayer) {
        AqualityServices.getLogger().info("Check that player was not updated");
        Assert.assertNotEquals(oldPlayer, updatedPlayer, "Player was updated");
    }
}