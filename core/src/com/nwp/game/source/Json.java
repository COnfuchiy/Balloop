package com.nwp.game.source;

import com.nwp.game.levels.Level;
import com.nwp.game.levels.LevelData;

public class Json {
    private static com.badlogic.gdx.utils.Json json = new com.badlogic.gdx.utils.Json();
    public static String SerializeObject(Object obj) {
        return json.toJson(obj);
    }
    public static <T> T DeserializeObject(Class<T> type, String jsonString) {
        return json.fromJson(type,Level.class, jsonString);
    }
}
