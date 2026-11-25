package modelo;

import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONObject;

import herramientas.OpenAi;

public class Agent {
    public String name;
    public String url;
    public String apiKey;
    public String model;
    public JSONArray transcript;
    public String systemPrompt;

    public Agent(String name, String url, String apiKey, String model) {
        this.name = name;
        this.url = url;
        this.apiKey = apiKey;
        this.model = model;
    }

    public Agent(String name) {
        this.name = name;
        this.transcript = getTranscript(name);
    }

    // Lee los datos del trasncript desde su trasnscript.json
   
    public String getSystemPrompt(String name) {
        try {
            return Files.readString(Path.of("agentes/" + name + "/SYSTEM.md"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public JSONArray getTranscript(String name) {
        try {
            String config = Files.readString(Path.of("agentes/" + name + "/transcript.json"));
            JSONArray jsonObject = new JSONArray(config);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public void saveTranscript() {
        try {
            Files.writeString(Path.of("agentes/" + name + "/transcript.json"), transcript.toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String chatear(String prompt) throws Exception {
        OpenAi openAi = new OpenAi("agentes/" + name + "/config.json");
        
        String systemPrompt = getSystemPrompt(name);
        
        
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        transcript.put(userMessage);
        saveTranscript();
        // Aquí se implementaría la lógica para enviar el prompt al modelo de IA y obtener la respuesta
        String response = openAi.generarRespuesta(systemPrompt, transcript);
        transcript.put(new JSONObject().put("role", "assistant").put("content", response));
        saveTranscript();
        return response;
    }
}
