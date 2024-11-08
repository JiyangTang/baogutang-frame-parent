package com.baogutang.frame.common.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @description:
 *
 * @author: nikooh
 * @date: 2024/11/08 : 16:18
 */
@ApiModel("通用Page请求参数")
@Data
public abstract class AbstractPageParam implements Serializable {

    private static final long serialVersionUID = 2897098106246970752L;

    @ApiModelProperty("页数")
    @Setter
    private Integer pageNum;

    @ApiModelProperty("页面大小")
    @Setter
    private Integer pageSize;

    @ApiModelProperty("排序条件")
    private List<OrderBy> orders = new ArrayList<>();

    @ApiModel("排序")
    @Data
    public static class OrderBy implements Serializable {

        private static final long serialVersionUID = -2936335557980068706L;

        @ApiModelProperty("排序列名")
        private String columnName;

        @ApiModelProperty("是否降序")
        private boolean desc;

    }

    public Integer getPageNum() {
        pageNum = pageNum == null ? 1 : pageNum;
        return pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize > 100) {
            pageSize = 10;
        }
        return pageSize;
    }

}
