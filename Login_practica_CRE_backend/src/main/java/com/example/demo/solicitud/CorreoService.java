package com.example.demo.solicitud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class CorreoService {
    private final String API_KEY = "jZYnC6BHkcF2za0A";
    // En Brevo → Transaccional → SMTP & API → Parámetros de la API
    
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            String json = """
                    {
                        "sender": {"email": "idaticre@gmail.com"},
                        "to": [{"email": "%s"}],
                        "subject": "%s",
                        "textContent": "%s"
                    }
                    """.formatted(destinatario, asunto, cuerpo);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            client.send(request, HttpResponse.BodyHandlers.ofString());
            
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }
    }
}
