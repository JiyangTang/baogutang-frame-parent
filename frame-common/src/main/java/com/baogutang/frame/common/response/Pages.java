package com.baogutang.frame.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 */
@ApiModel(value = "分页结果数据", description = "分页结果数据")
public class Pages<T> implements Serializable {

    private static final long serialVersionUID = -4776072864343943792L;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private int totalCount;

    /**
     * 每页记录数
     */
    @ApiModelProperty("每页记录数")
    private int pageSize;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private int totalPage;

    /**
     * 当前页数
     */
    @ApiModelProperty("当前页数")
    private int currPage;

    /**
     * 列表数据
     */
    @ApiModelProperty(value = "列表数据")
    private List<T> list;

    /**
     * 分页
     */
    public Pages(IPage<T> page) {
        this.list = page.getRecords();
        this.totalCount = (int) page.getTotal();
        this.pageSize = (int) page.getSize();
        this.currPage = (int) page.getCurrent();
        this.totalPage = (int) page.getPages();
    }

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public Pages(List<T> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }


    public Pages() {

    }

    public static <T> Pages<T> getPages(IPage<T> page) {
        Pages<T> iPage = new Pages<>();
        iPage.setList(page.getRecords());
        iPage.setTotalCount((int) page.getTotal());
        iPage.setCurrPage((int) page.getCurrent());
        iPage.setTotalPage((int) page.getPages());
        iPage.setPageSize((int) page.getSize());
        return iPage;
    }

    /**
     * List分页
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param list     数据集
     * @return PageUtils<T>
     */
    public Pages<T> getPages(Integer pageNum, Integer pageSize, List<T> list) {
        int pages = list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
        Pages<T> iPage = new Pages<>();
        iPage.setTotalCount(list.size());
        iPage.setCurrPage(pageNum);
        iPage.setTotalPage(pages);
        if (pages == 0 || pages < pageNum) {
            iPage.setList(Collections.emptyList());
        } else {
            int beginNum = (pageNum - 1) * pageSize;
            int endNum = list.size() - beginNum > pageSize ? beginNum + pageSize : list.size();
            iPage.setList(list.subList(beginNum, endNum));
        }
        return iPage;
    }

    public boolean hasNextPage() {
        return this.currPage < totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
