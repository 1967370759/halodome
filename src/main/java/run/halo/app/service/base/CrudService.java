package run.halo.app.service.base;


import com.sun.org.apache.xalan.internal.xsltc.DOM;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import run.halo.app.exception.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * CrudService 接口包含一些常用的方法
 *
 * @param <DOMAIN> 域类型
 * @param <ID>     id类型
 */
public interface CrudService<DOMAIN, ID> {

    /**
     * List All
     *
     * @return List
     */
    @NonNull
    List<DOMAIN> listAll();

    /**
     * List 所以通过排序
     *
     * @param sort
     * @return
     */
    @NonNull
    List<DOMAIN> listAll(@NonNull TypeCache.Sort sort);

    /**
     * list all by ids
     *
     * @param ids ids
     * @return List
     */
    @NonNull
    List<DOMAIN> listAllByIdS(@Nullable Collection<ID> ids);

    /**
     * 按id列出内容并排序
     *
     * @param ids  ids
     * @param sort sort
     * @return List
     */
    @NonNull
    List<DOMAIN> listAllByids(@NonNull Collection<ID> ids, @NonNull Sort sort);

    /**
     * 通过id获取
     *
     * @param id id
     * @return Optional
     */
    @NonNull
    Optional<DOMAIN> fetchById(@NonNull ID id);

    /**
     * 获取 id
     *
     * @param id id
     * @return DOMAIN
     * @throws NotFoundException 如果指定的id不存在
     */
    @NonNull
    DOMAIN getById(@NonNull ID id);

    /**
     * 获取可按id为空的域
     *
     * @param id id
     * @return DOMAIN
     */
    DOMAIN getByIdOfNullable(@NonNull ID id);

    /**
     * id必须存在， 否则抛出NotFoundException
     *
     * @param id id
     * @return boolean
     */
    boolean existsById(@NonNull ID id);

    /**
     * id必须存在， 否则抛出NotFoundException
     *
     * @param id id
     * @throws NotFoundException 如果指定的id不存在
     */
    void mustExistById(@NonNull ID id);

    /**
     * 计算所以
     *
     * @return long
     */
    long count();

    /**
     * 保留在域
     *
     * @param domain domain
     * @return DOMAIN
     */
    @NonNull
    @Transactional
    DOMAIN create(@NonNull DOMAIN domain);

    /**
     * 保存在域
     *
     * @param domains domains
     * @return List
     */
    @NonNull
    @Transactional
    List<DOMAIN> createInBatch(@NonNull Collection<DOMAIN> domains);

    /**
     * 更新的域
     *
     * @param domain domain
     * @return DOMAIN
     */
    @NonNull
    DOMAIN update(@NonNull DOMAIN domain);

    /**
     * 刷新数据库中所以挂起的更改
     */
    void flush();

    /**
     * 更新 域
     *
     * @param domains
     * @return List
     */
    @NonNull
    @Transactional
    List<DOMAIN> updateInBatch(@NonNull Collection<DOMAIN> domains);

    /**
     * 删除id
     *
     * @param id
     * @return DOMAIN
     * @throws NotFoundException 如果id不存在
     */
    DOMAIN removeByid(@NonNull ID id);

    /**
     * 如果存在 者按id删除
     *
     * @param id
     * @return DOMAIN
     */
    @NonNull
    @Transactional
    DOMAIN removeByIdOfNullable(@NonNull ID id);

    /**
     * 删除的域
     *
     * @param domain
     */
    @Transactional
    void remove(@NonNull DOMAIN domain);

    /**
     * 通过id删除
     *
     * @param ids
     */
    @Transactional
    void removeInBatch(@NonNull Collection<ID> ids);

    /**
     * 删除所以域
     *
     * @param domains
     */
    @Transactional
    void removeAll(@NonNull Collection<DOMAIN> domains);

    /**
     * 删除所以
     */
    @Transactional
    void removeAll();
}
