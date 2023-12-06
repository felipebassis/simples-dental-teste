package br.com.simplesdental.exception;

import java.io.Serial;

public abstract class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1811027028056878853L;

    public NotFoundException(String message) {
        super(message);
    }

}
