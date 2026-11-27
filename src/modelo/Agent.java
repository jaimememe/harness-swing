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

    // Crea la carpeta agentes/<nombre> con los mismos archivos que los otros agentes.
    // Devuelve false si ya existe un agente con ese nombre (para no sobrescribirlo).
    public static boolean crearAgente(String nombre, String system) throws Exception {
        Path carpeta = Path.of("agentes/" + nombre);
        if (Files.exists(carpeta)) {
            return false;
        }
        Files.createDirectories(carpeta);

        // Usamos el config.json del Asistente como plantilla y solo cambiamos el nombre
        String plantilla = Files.readString(Path.of("agentes/Asistente/config.json"));
        JSONObject config = new JSONObject(plantilla);
        config.put("name", nombre);

        Files.writeString(carpeta.resolve("config.json"), config.toString(4));
        Files.writeString(carpeta.resolve("SYSTEM.md"), system);
        Files.writeString(carpeta.resolve("AGENT.md"), "");
        // El transcript empieza con el mensaje "system" en la posición 0, igual que los otros agentes.
        // Así OpenAi.generarRespuesta reemplaza ese system y no el primer mensaje del usuario.
        JSONArray transcript = new JSONArray();
        transcript.put(new JSONObject().put("role", "system").put("content", system));
        Files.writeString(carpeta.resolve("transcript.json"), transcript.toString(4));
        return true;
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
