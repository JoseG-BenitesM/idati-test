package com.example.demo.solicitud;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorreoService {
    private final JavaMailSender mailSender;
    
    public void enviarCorreo(String destinatario, String asunto, String cuerpo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("ruizjeisonf@gmail.com");
        mailSender.send(mensaje);
    }
}
