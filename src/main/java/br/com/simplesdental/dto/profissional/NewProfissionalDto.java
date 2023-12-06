package br.com.simplesdental.dto.profissional;

import br.com.simplesdental.entity.profissional.Cargo;

import java.time.LocalDate;

public record NewProfissionalDto(String nome,
                                 Cargo cargo,
                                 LocalDate dataDeNascimento) {
}
