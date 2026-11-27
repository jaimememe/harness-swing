package herramientas;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAi {

    private String url;
    private String apiKey;
    private String model;

    // Lee los datos del agente desde su config.json
    public OpenAi(String rutaConfig) throws IOException {
        String config = Files.readString(Path.of(rutaConfig));
        url = leerCampo(config, "url");
        apiKey = leerCampo(config, "apiKey");
        model = leerCampo(config, "model");
    }

    public String getApiKey() {
        return apiKey;
    }

    private Object[] jsonMessages(String[] messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < messages.length; i++) {
            sb.append("{\"role\": \"user\", \"content\": \"").append(escapar(messages[i])).append("\"}");
            if (i < messages.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return new Object[]{sb.toString()};
    }

    // Envía un mensaje y devuelve la respuesta
    public String generarRespuesta(String systemMessage, JSONArray messages){
        try {
         
            messages.put(0, new JSONObject().put("role", "system").put("content", systemMessage));
            String json = "{\"model\": \"" + model + "\"," + "\"messages\": " + messages + "}";

            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient cliente = HttpClient.newHttpClient();
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() != 200) {
                return "Error " + respuesta.statusCode() + ": " + leerCampo(respuesta.body(), "message");
            }
            return leerCampo(respuesta.body(), "content");
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al generar respuesta: " + e.getMessage();
        }
    }

    // Prepara el texto para poder ponerlo dentro de un JSON
    private String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Busca "campo": "valor" en un JSON y devuelve el valor
    private String leerCampo(String json, String campo) {
        int posicion = json.indexOf("\"" + campo + "\"");
        if (posicion == -1) {
            return "";
        }
        int dosPuntos = json.indexOf(":", posicion);
        int i = json.indexOf("\"", dosPuntos) + 1;

        String valor = "";
        while (i < json.length() && json.charAt(i) != '"') {
            char letra = json.charAt(i);
            if (letra == '\\') {
                i++;
                letra = json.charAt(i);
                if (letra == 'n') {
                    letra = '\n';
                } else if (letra == 't') {
                    letra = '\t';
                } else if (letra == 'r') {
                    letra = '\r';
                } else if (letra == 'u') {
                    letra = (char) Integer.parseInt(json.substring(i + 1, i + 5), 16);
                    i = i + 4;
                }
            }
            valor = valor + letra;
            i++;
        }
        return valor;
    }
}
