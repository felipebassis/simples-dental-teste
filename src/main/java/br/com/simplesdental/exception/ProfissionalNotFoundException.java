package br.com.simplesdental.exception;

import java.io.Serial;

public class ProfissionalNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 3071478552217913993L;

    public ProfissionalNotFoundException(String message) {
        super(message);
    }
}
