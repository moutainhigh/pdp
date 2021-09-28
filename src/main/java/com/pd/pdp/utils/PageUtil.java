package com.pd.pdp.utils;

/**
 * @Author pdp
 * @Description  分页工具
 * @Date 2:31 下午 2020/10/16
 * @Param 
 * @return 
 **/

public class PageUtil {
    /**
     * 总条数
     */
    private int     totalNumber;

    /**
     * 总页数
     */
    private int     totalPage;

    /**
     * 当前页
     */
    private int     currentPage;

    /**
     * 每页显示的数目
     */
    private int     pageNumber = 10;

    /**
     * 数据库中limit的参数，从第几条开始取
     */
    private int     dbIndex;

    /**
     * 数据库中limit的参数，一共取多少条,适用于mysql, 如果是oracle则表示最大取到的条数
     */
    private int     dbNumber;

    private boolean isNext     = true;

    /**
     * 根据当前对象中的属性值计算并设置相关属性的值
     */

    public void count() {
        // 计算总页数
        int totalPageTemp = this.totalNumber / this.pageNumber;
        int plus = (this.totalNumber % this.pageNumber) == 0 ? 0 : 1;
        totalPageTemp += plus;
        // 如果总页数小于0显示第一页
        if (totalPageTemp <= 0) {
            totalPageTemp += 1;
        }
        this.totalPage = totalPageTemp;
        if (this.currentPage < 1) {
            this.currentPage = 1;
        }

        if (this.totalPage <= this.currentPage) {
            this.currentPage = this.totalPage;
            this.isNext = false;
        }

        // 设置limit参数
        this.dbIndex = (this.currentPage - 1) * this.pageNumber;
        this.dbNumber = this.pageNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
        count();
    }

    public int gettotalPage() {
        return totalPage;
    }

    public void settotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        count();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public int getDbNumber() {
        return dbNumber;
    }

    public void setDbNumber(int dbNumber) {
        this.dbNumber = dbNumber;
    }

    /**
     * Getter method for property <tt>isNext</tt>.
     * @return property value of isNext
     */
    public boolean isNext() {
        return isNext;
    }

    /**
     * Setter method for property <tt>isNext</tt>.
     * @param isNext
     *            value to be assigned to property isNext
     */
    public void setNext(boolean isNext) {
        this.isNext = isNext;
    }

}
