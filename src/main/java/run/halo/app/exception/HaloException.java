package run.halo.app.exception;


import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * 项目基本异常
 */
public abstract class HaloException extends RuntimeException {

    /**
     * 错误 和错误数据
     *
     */
        private Object errorData;

    public HaloException(String message) {
        super(message);

    }

    public HaloException(String message, Throwable cause) {
        super(message, cause);

    }
    @NonNull
    public abstract HttpStatus getStatus();

    @NonNull
    public Object getErrorData(){
        return errorData;
    }
    @NonNull
    public HaloException  setErrorData(@NonNull Object errorData){
        this.errorData=errorData;
        return this;
    }
}
