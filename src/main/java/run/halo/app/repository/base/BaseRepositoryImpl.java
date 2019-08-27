package run.halo.app.repository.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基本存储库的实现
 *
 * @param <DOMAIN> 域类型
 * @param <ID>id   类型
 * @author 连接
 */
@Slf4j
public class BaseRepositoryImpl<DOMAIN, ID> extends SimpleJpaRepository<DOMAIN, ID> implements BaseRepository<DOMAIN, ID> {
    private final JpaEntityInformation<DOMAIN, ID> entityInformation;

    private final EntityManager entityManager;


    public BaseRepositoryImpl(JpaEntityInformation<DOMAIN, ID> entityInformation, EntityManager entityManager, JpaEntityInformation<DOMAIN, ID> entityInformation1, EntityManager entityManager1) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation1;
        this.entityManager = entityManager1;
    }

//    public BaseRepositoryImpl(Class<DOMAIN> domainClass, EntityManager em, JpaEntityInformation<DOMAIN, ID> entityInformation, EntityManager entityManager) {
//        super(domainClass, em);
//        this.entityInformation = entityInformation;
//        this.entityManager = entityManager;
//    }

    /**
     * 执行count查询并透明地汇总返回的所有值。
     *
     * @param query 不能是{@literal 为空}。
     * @return
     */
    private static long executrCountQuery(TypedQuery<Long> query) {
        //message  :TypedQuery不能为空
        Assert.notNull(query, "TypedQuery must not be bull!");
        List<Long> totals = query.getResultList();
        long total = 0L;
        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }


    /**
     * 根据id列表和指定的排序查找所有域。
     *
     * @param ids  域的id列表必须不为空
     * @param sort 指定的排序不能为空
     * @return 域列表
     */
    public List<DOMAIN> findAllByIdIn(Iterable<ID> ids, Sort sort) {
        //   message  给定的Id的可迭代值必须不为空
        Assert.notNull(ids, "The given Iterable of Id' s must not be null!");
        // message  排序信息必须为空
        Assert.notNull(sort, "Sort info must nto be null");

        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        if (entityInformation.hasCompositeId()) {
            List<DOMAIN> results = new ArrayList<>();
            ids.forEach(id -> super.findById(id).ifPresent(results::add));
            return results;
        }

        ByIdsSpecification<DOMAIN> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<DOMAIN> query = super.getQuery(specification, sort);
        return query.setParameter(specification.parameter, ids).getResultList();
    }

    /**
     * @param ids      不能为空
     * @param pageable 不能为空
     * @return
     */
    @Override
    public Page<DOMAIN> findAllByIdIn(Iterable<ID> ids, Pageable pageable) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");
        Assert.notNull(pageable, "Page info must nto be null");

        if (!ids.iterator().hasNext()) {
            return new PageImpl<>(Collections.emptyList());
        }

        if (entityInformation.hasCompositeId()) {
            throw new UnsupportedOperationException("Unsupported find all by composite id with page info");
        }
        ByIdsSpecification<DOMAIN> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<DOMAIN> query = super.getQuery(specification, pageable).setParameter(specification.parameter, ids);
        TypedQuery<Long> countQuery = getCountQuery(specification, getDomainClass()).setParameter(specification.parameter, ids);

        return pageable.isUnpaged() ?
                new PageImpl<>(query.getResultList())
                : readPage(query, getDomainClass(), pageable, countQuery);
    }

    /**
     * 按照id删除
     *
     * @param ids 域的id列表必须不为空
     * @return受印象的行数
     */
    @Transactional
    public long deleteByIdIn(Iterable<ID> ids) {
        log.debug("调用自定义的deleteByIdIn方法");
        //找到所有领域
        List<DOMAIN> domains = findAllById(ids);

        //在批量删除
        deleteInBatch(domains);
        //返回已删除域的大小
        return domains.size();
    }

    protected <S extends DOMAIN> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, TypedQuery<Long> countQuery) {
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.
                getPage(query.getResultList(), pageable,
                        () -> executrCountQuery(countQuery));
    }

    private static final class ByIdsSpecification<T> implements Specification<T> {
        private static final long serialVersionUID = 1L;
        private final JpaEntityInformation<T, ?> entityInformation;
        @Nullable
        ParameterExpression<Iterable> parameter;


        private ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cd) {
            Path<?> path = root.get(this.entityInformation.getIdAttribute());
            this.parameter = cd.parameter(Iterable.class);

            return path.in(this.parameter);
        }
    }

}