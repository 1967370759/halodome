package run.halo.app.model.params;

import lombok.Data;
import run.halo.app.model.dto.base.InputConverter;
import run.halo.app.model.entity.Option;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 选项参数
 */
@Data
public class OptionParam implements InputConverter<Option> {

    @NotBlank(message = "选项key不能为null")
    @Size(max = 100, message = "选项key不能超过{max}")
    private String key;

    @Size(max = 1023, message = "选项value不能超过{max}")
    private String value;
}
