package com.nwp.game.source;

import java.io.IOException;
import java.lang.reflect.Type;

public class Json {
    public enum Formatting {
        Normal,
        Indented
    }
    private static com.badlogic.gdx.utils.Json json = new com.badlogic.gdx.utils.Json();
    public static String SerializeObject(Object obj) {
        return json.toJson(obj);
    }
    public static String SerializeObject(Object obj, Formatting format) {
        switch (format) {
            case Normal: return json.toJson(obj);
            case Indented: return json.prettyPrint(obj);
            default: throw new IllegalArgumentException();
        }
    }
    public static <T> T DeserializeObject(Class<T> type, String jsonString) {
        return json.fromJson(type, jsonString);
    }
    public static void WriteToFile(String fileName, Object obj) {
        File.WriteLine(fileName, SerializeObject(obj, Formatting.Indented));
    }
    public static <T> T ReadFromFile(Class<T> type, String fileName) {
        return DeserializeObject(type, File.ReadAll(fileName));
    }
}
