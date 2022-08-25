
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import model.Player;
import org.testng.annotations.Test;
import utils.*;
import java.nio.file.Path;
import java.util.Properties;

public class TestClass {
    protected static Properties properties = ReaderDataProperties.loadProperties();

    @Test
    public void verifyAllPlayersPositiveTest(){
        HttpResponse<JsonNode> allPlayers = PlayerControllerApi.getAllPlayers();
        AssertSteps.checkStatusCode(allPlayers, Statuses.OK);
    }

    @Test
    public void playerGetByIdPositiveTest() {
        HttpResponse<JsonNode> playerById = PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("validPlayerId")));
        AssertSteps.checkStatusCode(playerById, Statuses.OK);
        AssertSteps.checkThatResponseHaveRightKeys(playerById, JsonUtils.getJsonObject(Path.of(
                properties.getProperty("pathToTemplate"))));
    }

    @Test
    public void playerGetByIdNegativeTest() {
        HttpResponse<JsonNode> playerById = PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("invalidPlayerId")));
        System.out.println(playerById.getBody());
        AssertSteps.checkThatResponseHaveRightKeys(playerById, JsonUtils.getJsonObject(Path.of(
                properties.getProperty("pathToTemplate"))));
    }

    @Test
    public void deletePlayerPositiveTest() {
        int id = Integer.parseInt(properties.getProperty("idForDeleting"));
        HttpResponse<JsonNode> deletePlayerResponse = PlayerControllerApi.deletePlayerById(Roles.ADMIN, id);
        AssertSteps.checkStatusCode(deletePlayerResponse, Statuses.NO_CONTENT);
        AssertSteps.checkThatPlayerWasDeleted(id);
    }

    @Test
    public void deletePlayerNegativeTest() {
        int id = Integer.parseInt(properties.getProperty("invalidPlayerId"));
        HttpResponse<JsonNode> deletePlayerResponse = PlayerControllerApi.deletePlayerById(Roles.ADMIN, id);
        AssertSteps.checkStatusCode(deletePlayerResponse, Statuses.NO_CONTENT);
        AssertSteps.checkThatPlayerWasDeleted(id);
    }

    @Test
    public void createPlayerPositiveTest() {
        Player newPlayer = new Player(25, Gender.MALE, "Player10", Roles.USER, "Player10");
        HttpResponse<JsonNode> createPlayer = PlayerControllerApi.createPlayer(Roles.SUPERVISOR, newPlayer);
        AssertSteps.checkStatusCode(createPlayer, Statuses.OK);
        AssertSteps.checkThatPlayerWasCreated(newPlayer);
    }

    @Test
    public void createPlayerWithInvalidAgeTest() {
        Player newPlayer = new Player(5, Gender.FEMALE, "Child5", Roles.USER, "Child5");
        HttpResponse<JsonNode> createPlayer = PlayerControllerApi.createPlayer(Roles.SUPERVISOR, newPlayer);
        AssertSteps.checkStatusCode(createPlayer, Statuses.OK);
        AssertSteps.checkThatPlayerWasCreated(newPlayer);
    }

    @Test
    public void updatePlayerPositiveTest() {
        Player playerById = new Player(PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("validPlayerId"))));
        playerById.setAge(36);

        HttpResponse<JsonNode> updatePlayer = PlayerControllerApi.updatePlayer(Roles.SUPERVISOR,
                playerById.getId(), playerById);
        AssertSteps.checkStatusCode(updatePlayer, Statuses.OK);
        Player updatedPlayer = new Player(PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("validPlayerId"))));
        AssertSteps.checkThatPlayerWasUpdated(playerById, updatedPlayer);
    }

    //найденные баги
    //1. Неправильный код ответа при невалидном id юзера
    @Test
    public void findPlayerByInvalidId() {
        HttpResponse<JsonNode> playerById = PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("invalidPlayerId")));
        AssertSteps.checkStatusCode(playerById, Statuses.NOT_FOUND);
    }

    //2. Обновляются поля с невалидными значениями
    @Test
    public void updatePlayerWithInvalidDataTest() {
        Player playerById = new Player(PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("validPlayerId"))));
        playerById.setAge(-2);
        playerById.setGender("woman");

        HttpResponse<JsonNode> updatePlayer = PlayerControllerApi.updatePlayer(Roles.SUPERVISOR,
                playerById.getId(), playerById);
        AssertSteps.checkStatusCode(updatePlayer, Statuses.FORBIDDEN);
        Player updatedPlayer = new Player(PlayerControllerApi.getPlayerById(Integer.parseInt(
                properties.getProperty("validPlayerId"))));
        AssertSteps.checkThatPlayerWasNotUpdated(playerById, updatedPlayer);
    }

    //3. создается юзер с неуникальным screenName
    @Test
    public void createPlayerWithNonUniqueScreenName() {
        Player newPlayer = new Player(25, Gender.MALE, "Player10", Roles.USER, "Player10");
        HttpResponse<JsonNode> firstPlayer = PlayerControllerApi.createPlayer(Roles.SUPERVISOR, newPlayer);
        AssertSteps.checkStatusCode(firstPlayer, Statuses.OK);
        AssertSteps.checkThatPlayerWasCreated(newPlayer);
        AssertSteps.checkThatAllPlayersHaveUniqueScreenName();

        Player playerWithNonUniqueScreenName = new Player(25, Gender.MALE, "Player11",
                Roles.USER, "Player10");
        HttpResponse<JsonNode> secondPlayer = PlayerControllerApi.createPlayer(Roles.SUPERVISOR, playerWithNonUniqueScreenName);
        AssertSteps.checkStatusCode(secondPlayer, Statuses.OK);
        AssertSteps.checkThatPlayerWasCreated(newPlayer);
        AssertSteps.checkThatAllPlayersHaveUniqueScreenName();
    }
}