package com.baogutang.frame.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author N1KO
 */
@ApiModel("分页信息")
@Data
@ToString
@Accessors(chain = true)
public class PageInfo {

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Integer currPage = 1;

    /**
     * 一页几个
     */
    @ApiModelProperty("一页多少个")
    private Integer pageSize = Integer.MAX_VALUE;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private Integer totalCount;

    public PageInfo() {
    }

    public PageInfo(Number pageSize, Number currentPage, Number totalCount, Number totalPage) {
        this(pageSize, currentPage);
        initTotleRecord(totalCount, totalPage);
    }

    public PageInfo(Number pageSize, Number currentPage) {
        this.pageSize = pageSize == null ? null : pageSize.intValue();
        this.currPage = currentPage == null ? 1 : currentPage.intValue();
    }

    @JsonIgnore
    public PageInfo initTotleRecord(Number totalRecord, Number totalPage) {
        this.totalCount = totalRecord == null ? null : totalRecord.intValue();
        this.totalPage = totalPage == null ? null : totalPage.intValue();
        return this;
    }

    @JsonIgnore
    public Integer getFrom() {
        return pageSize != null ? (currPage - 1) * pageSize : null;
    }

    @JsonIgnore
    public Integer getTo() {
        Integer from = getFrom();
        return from != null ? from + pageSize : null;
    }

    /**
     * 获取总页数
     **/
    @JsonIgnore
    public Integer getAllPage() {
        totalPage = totalCount != null && pageSize != null ? (this.totalCount - 1) / this.pageSize + 1 : null;
        return totalPage;
    }

    @JsonIgnore
    public Type getType() {
        return pageSize == null ? Type.ALL : Type.PAGE;
    }

    /**
     * 分页类型
     */
    public static enum Type {
        /**
         * 全部
         */
        ALL,
        /**
         * 分页
         */
        PAGE;
    }
}