package br.com.simplesdental.dto.contato;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContatoDto(UUID id,
                         String nome,
                         String contato,
                         LocalDateTime createdAt) {
}
