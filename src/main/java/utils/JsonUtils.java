package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class JsonUtils {

    private static final JSONParser jsonParser = new JSONParser();

    public static JSONObject getJsonObject(Path path) {
        try {
            return (JSONObject) jsonParser.parse(new FileReader(path.toString()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}