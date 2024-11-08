package com.baogutang.frame.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author N1KO
 */
@Data
public abstract class AbstractEntity {

    /**
     * 唯一主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    protected Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    protected LocalDateTime updateTime;

    /**
     * 已删除
     */
    @TableField("deleted")
    protected Boolean deleted;
}
