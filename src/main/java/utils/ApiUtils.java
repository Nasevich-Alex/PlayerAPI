package utils;

import aquality.selenium.browser.AqualityServices;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class ApiUtils {
    public static GetRequest makeGetRequest(String url) {
        AqualityServices.getLogger().info(String.format("Sending GET request by url: %s", url));
        return Unirest.get(url);
    }

    public static HttpRequestWithBody makePostRequestWithBody(String url) {
        AqualityServices.getLogger().info(String.format("Sending POST request by url: %s", url));
        return Unirest.post(url);
    }

    public static HttpRequestWithBody makeDeleteRequest(String url) {
        AqualityServices.getLogger().info(String.format("Sending DELETE request by url: %s", url));
        return Unirest.delete(url);
    }

    public static HttpRequestWithBody makePatchRequest(String url) {
        AqualityServices.getLogger().info(String.format("Sending PATCH request by url: %s", url));
        return Unirest.patch(url);
    }
}