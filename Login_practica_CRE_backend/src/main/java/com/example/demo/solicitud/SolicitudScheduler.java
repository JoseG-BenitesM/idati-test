package com.example.demo.solicitud;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SolicitudScheduler {
    
    private static final Logger log =
            LoggerFactory.getLogger(SolicitudScheduler.class);
    
    private final SolicitudRecuperacionRepository solicitudRepository;
    
    @Scheduled(fixedRate = 60000)
    // Se ejecuta cada 60 segundos automáticamente
    @Transactional
    public void expirarCodigosVencidos() {
        LocalDateTime hace5Minutos = LocalDateTime.now().minusMinutes(5);
        
        // Buscar códigos activos (estado=1 o 4) que ya pasaron los 5 minutos
        List<SolicitudRecuperacionEntity> vencidas = solicitudRepository
                .findByEstadoIn(List.of((byte) 1, (byte) 4))
                .stream()
                .filter(s -> s.getFechaSolicitud().isBefore(hace5Minutos))
                .toList();
        
        if(! vencidas.isEmpty()) {
            vencidas.forEach(s -> s.setEstado((byte) 3));
            // Cambia a estado=3 (expirada) — no elimina, mantiene auditoría
            solicitudRepository.saveAll(vencidas);
            log.info("Códigos expirados actualizados: {}", vencidas.size());
        }
    }
}
