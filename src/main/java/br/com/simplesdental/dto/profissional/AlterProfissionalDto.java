package br.com.simplesdental.dto.profissional;

import br.com.simplesdental.entity.profissional.Cargo;

public record AlterProfissionalDto(String nome,
                                   Cargo cargo) {
}
