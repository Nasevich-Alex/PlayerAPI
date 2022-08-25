package model;

import aquality.selenium.browser.AqualityServices;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;
import utils.Gender;
import utils.Roles;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {

    private int id;
    private int age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;

    public Player(int age, Gender gender, String login, String password, Roles role, String screenName) {
        this.age = age;
        this.gender = gender.getGender();
        this.login = login;
        this.password = password;
        this.role = role.getRole();
        this.screenName = screenName;

    }

    public Player(int age, Gender gender, String login, Roles role, String screenName) {
        this.age = age;
        this.gender = gender.getGender();
        this.login = login;
        this.role = role.getRole();
        this.screenName = screenName;
    }

    public Player(HttpResponse<JsonNode> response) {
        JSONObject object = response.getBody().getObject();
        this.id = object.getInt("id");
        this.login = object.getString("login");
        this.password = object.getString("password");
        this.screenName = object.getString("screenName");
        this.gender = object.getString("gender");
        this.age = object.getInt("age");
        this.role = object.getString("role");
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setAge(int age) {
        if(age > 16 && age < 60) {
            this.age = age;
        } else {
            AqualityServices.getLogger().info("The user must be over 16 and under 60");
        }
    }

    public void setPassword(String password) {
        if(password.length() > 7 && password.length() < 15 && isPasswordContains(password)) {
            this.password = password;
        } else {
            AqualityServices.getLogger().info("Password must contain latin letters and numbers (minimum 7 maximum 15 characters)");
        }
    }

    public Map<String, Object> getParameters() {
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("age", getAge());
        param.put("gender", getGender());
        param.put("login", getLogin());
        param.put("role", getRole());
        param.put("screenName", getScreenName());
        return param;
    }

    public boolean isPasswordContains(String password) {
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(password);
        return m.find();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && age == player.age && gender.equals(player.gender) && login.equals(player.login)
               && password.equals(player.password) && role.equals(player.role) && screenName.equals(player.screenName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, gender, login, password, role, screenName);
    }
}