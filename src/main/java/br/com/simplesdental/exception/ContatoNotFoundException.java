package br.com.simplesdental.exception;

import java.io.Serial;

public class ContatoNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -7436549035675691272L;

    public ContatoNotFoundException(String message) {
        super(message);
    }
}
