package run.halo.app.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import run.halo.app.model.entity.Option;
import run.halo.app.model.params.OptionParam;
import run.halo.app.service.base.CrudService;

import java.util.List;
import java.util.Map;


public interface OptionService extends CrudService<Option,Integer> {
    int DEFAULT_POST_PAGE_SIZE=10;

    int DEFAULT_COMMENT_PAGE_SIZE=10;

    int DEFAULT_RSS_PAGE_SIZE=20;

    String OPTIONS_KEY="options";

    /**
     * 保存多个选项
     * @param options
     */
    @Transactional
    void save(@Nullable Map<String ,String > options);

    /**
     * 保存多个选项
     * @param optionParams
     */
    @Transactional
    void save(@Nullable List<OptionParam> optionParams);


    @Transactional
    void saveProperties(@NonNull Map<? extends PropertyEnum>)
}
