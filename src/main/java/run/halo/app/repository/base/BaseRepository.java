package run.halo.app.repository.base;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 基本存储库接口包含一些常用方法。
 * @param <DOMAIN>
 * @param <ID>
 */
public interface BaseRepository<DOMAIN, ID> extends JpaRepository<DOMAIN, ID> {

    /**
     *按id列表查找所有域
     * @param ids 不能为空
     * @param sort 不能为空
     * @return  域列表
     */
    @NonNull
    List<DOMAIN> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Sort sort);


    /**
     *按域id列表查找所有域。
     * @param ids 不能为空
     * @param pageable 不能为空
     * @return 域列表
     */
    @NonNull
    Page<DOMAIN> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Pageable pageable);


    /**
     * 按照id删除
     * @param ids 域的id列表必须不为空
     * @return 受影响的行数
     */
    long deleteByIdIn(@NonNull Iterable<ID> ids);
}
