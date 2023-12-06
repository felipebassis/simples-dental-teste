package br.com.simplesdental.dto.contato;

import java.util.UUID;

public record NewContatoDto(String nome,
                            String contato,
                            UUID profissionalId) {
}
