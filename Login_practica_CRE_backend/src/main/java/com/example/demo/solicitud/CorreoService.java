package com.example.demo.solicitud;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CorreoService {
    
    private final String API_KEY = "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";
    
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        System.out.println("=== INTENTANDO ENVIAR CORREO A: " + destinatario);
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            String json = String.format(
                    "{" +
                            "\"sender\": {\"email\": \"idaticre@gmail.com\"}," +
                            "\"to\": [{\"email\": \"%s\"}]," +
                            "\"subject\": \"%s\"," +
                            "\"textContent\": \"%s\"" +
                            "}",
                    destinatario, asunto, cuerpo
            );
            
            System.out.println("=== JSON: " + json);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("=== BREVO STATUS: " + response.statusCode());
            System.out.println("=== BREVO BODY: " + response.body());
            
        } catch (Exception e) {
            System.err.println("=== ERROR ENVIANDO CORREO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
