package run.halo.app.exception;


import org.springframework.http.HttpStatus;

/**
 *未找到实体异常
 */
public class NotFoundException extends HaloException {


    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
