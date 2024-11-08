package com.baogutang.frame.common.response;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @param <T>
 * @author N1KO
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO<T> {

    @ApiModelProperty("列表数据")
    private List<T> list;

    @ApiModelProperty("总记录数")
    private Integer totalCount;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;

    @ApiModelProperty("总页数")
    private Integer totalPage;

    @ApiModelProperty("当前页数")
    private Integer currPage;

    public PageDTO(List<T> list, PageInfo pageInfo) {
        this.list = list;
        this.totalCount = pageInfo.getTotalCount();
        this.pageSize = pageInfo.getPageSize();
        this.totalPage = pageInfo.getTotalPage();
        this.currPage = pageInfo.getCurrPage();
    }
}
