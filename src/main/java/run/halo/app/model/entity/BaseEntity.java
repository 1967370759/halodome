package run.halo.app.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 基础的实体
 */
@MappedSuperclass
@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {
    
    private Date createTime;

}
