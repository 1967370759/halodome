package run.halo.app.cache;

import lombok.*;

import java.util.Date;

import java.io.Serializable;
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CacheWrapper<V> implements Serializable {

    /**
     * 缓存数据
     */
    private V data;
    /**
     *失效时间
     */
    private Date expireAt;
    /**
     * 创造时间
     */
    private Date createAt;

}
