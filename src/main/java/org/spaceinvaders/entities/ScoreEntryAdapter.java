package org.spaceinvaders.entities;

import com.google.gson.*;
import javafx.beans.property.*;

import java.lang.reflect.Type;

public class ScoreEntryAdapter implements JsonSerializer<ScoreEntry>, JsonDeserializer<ScoreEntry> {

    @Override
    public JsonElement serialize(ScoreEntry src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Serializar las propiedades de JavaFX a sus valores primitivos
        jsonObject.addProperty("playerName", src.getPlayerName());
        jsonObject.addProperty("score", src.getScore());
        jsonObject.addProperty("lives", src.getLives());
        return jsonObject;
    }

    @Override
    public ScoreEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String playerName = jsonObject.get("playerName").getAsString();
        int score = jsonObject.get("score").getAsInt();
        int lives = jsonObject.get("lives").getAsInt();

        // Crear el objeto ScoreEntry con los valores primitivos, y asignar las propiedades de JavaFX
        return new ScoreEntry(playerName, score, lives);
    }
}
