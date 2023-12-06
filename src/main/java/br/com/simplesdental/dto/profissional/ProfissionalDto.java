package br.com.simplesdental.dto.profissional;

import br.com.simplesdental.entity.profissional.Cargo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProfissionalDto(UUID id,
                              String nome,
                              Cargo cargo,
                              LocalDate dataDeNascimento,
                              LocalDateTime createdAt) {
}
