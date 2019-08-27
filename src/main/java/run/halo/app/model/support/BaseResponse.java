package run.halo.app.model.support;


import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    /**
     * 响应状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应开发消息
     */
    private String devMessage;
    /**
     * 响应数据
     */
    private T data;


    public BaseResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 使用消息和数据创建 ok结果（默认状态为200
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message,@Nullable T data){
        return new BaseResponse<>(HttpStatus.OK.value(),message,data);
    }
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message){
        return ok(message,null);
    }
    public static <T> BaseResponse<T> ok(@NonNull T data){
        return new BaseResponse<>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),data);
    }
}
