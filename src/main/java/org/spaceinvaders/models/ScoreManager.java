package org.spaceinvaders.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {

    private static final String FILE_PATH = "scores.json";

    // Metodo para cargar las puntuaciones desde el archivo
    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scoreList = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                // Crear un nuevo archivo vacío si no existe
                saveScores(scoreList);
            } else {
                FileReader reader = new FileReader(file);

                // Crear un Gson con el adaptador registrado
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ScoreEntry.class, new ScoreEntryAdapter()) // Registrar el adaptador
                        .create();

                Type scoreListType = new TypeToken<List<ScoreEntry>>(){}.getType();
                scoreList = gson.fromJson(reader, scoreListType);
                reader.close();
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return scoreList;
    }

    // Metodo para guardar las puntuaciones en el archivo
    public static void saveScores(List<ScoreEntry> scoreList) {
        try {
            // Guardar las nuevas puntuaciones en el archivo, sobrescribiendo las existentes
            FileWriter writer = new FileWriter(FILE_PATH);

            // Crear un Gson con el adaptador registrado
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ScoreEntry.class, new ScoreEntryAdapter()) // Registrar el adaptador
                    .create();

            gson.toJson(scoreList, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
