package com.baogutang.frame.common.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author N1KO
 */
@Data
public abstract class AbstractAdminVo {

    /**
     * 唯一主键
     */
    @ApiModelProperty(value = "ID")
    protected Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateTime;

    /**
     * 已删除
     */
    @ApiModelProperty(value = "删除状态")
    protected Boolean deleted;

    /**
     * 管理员id
     */
    @ApiModelProperty(value = "管理员ID")
    protected Long adminId;

    /**
     * 管理员姓名
     */
    @ApiModelProperty(value = "管理员姓名")
    protected String adminName;
}
