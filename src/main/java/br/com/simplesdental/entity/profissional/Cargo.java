package br.com.simplesdental.entity.profissional;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Cargo {
    DESENVOLVEDOR,
    DESIGNER,
    SUPORTE,
    TESTER,
    ;
}
