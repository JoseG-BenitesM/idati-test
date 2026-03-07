package com.example.demo.solicitud;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CorreoService {
    
    private final String API_KEY = "re_ZDDqSSUH_q7Djt3FEUMoUKWpbRyj8E3rP";
    
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        System.out.println("=== INTENTANDO ENVIAR CORREO A: " + destinatario);
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            String json = String.format(
                    "{" +
                            "\"from\": \"onboarding@resend.dev\"," +
                            "\"to\": [\"%s\"]," +
                            "\"subject\": \"%s\"," +
                            "\"text\": \"%s\"" +
                            "}",
                    destinatario, asunto, cuerpo
            );
            
            System.out.println("=== JSON: " + json);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("=== RESEND STATUS: " + response.statusCode());
            System.out.println("=== RESEND BODY: " + response.body());
            
        } catch (Exception e) {
            System.err.println("=== ERROR ENVIANDO CORREO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
