package com.dblog.dblog.model.dtos;

import java.time.LocalDateTime;

public record BlogDto (Long id, String titulo, String contenido, Integer view, LocalDateTime timeData, Long autorId, String categoria, String imagen, String autor, String autorImg){
}
