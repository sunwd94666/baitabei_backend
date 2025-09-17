package com.baitabei.common.result;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果包装类
 * 
 * @author MiniMax Agent
 */
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "数据列表")
    private List<T> records;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "每页大小")
    private Long size;

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "是否有上一页")
    private Boolean hasPrevious;

    @Schema(description = "是否有下一页")
    private Boolean hasNext;

    @Schema(description = "是否为第一页")
    private Boolean isFirst;

    @Schema(description = "是否为最后一页")
    private Boolean isLast;

    // 无参构造函数
    public PageResult() {
        this.current = 1L;
        this.size = 10L;
        this.total = 0L;
        this.pages = 0L;
        this.hasPrevious = false;
        this.hasNext = false;
        this.isFirst = true;
        this.isLast = true;
    }

    // 构造函数
    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size; // 计算总页数
        this.hasPrevious = current > 1;
        this.hasNext = current < pages;
        this.isFirst = current == 1;
        this.isLast = current.equals(pages) || pages == 0;
    }

    // 静态创建方法
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        return new PageResult<>(records, total, current, size);
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>();
    }

    public static <T> PageResult<T> empty(Long current, Long size) {
        PageResult<T> result = new PageResult<>();
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }

    // 从MyBatis Plus的IPage转换
    public static <T> PageResult<T> fromIPage(com.baomidou.mybatisplus.core.metadata.IPage<T> iPage) {
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    // 计算总页数
    private void calculatePages() {
        if (size != null && size > 0 && total != null) {
            this.pages = (total + size - 1) / size;
        } else {
            this.pages = 0L;
        }
    }

    // 计算分页状态
    private void calculatePageStatus() {
        if (current != null && pages != null) {
            this.hasPrevious = current > 1;
            this.hasNext = current < pages;
            this.isFirst = current == 1;
            this.isLast = current.equals(pages) || pages == 0;
        }
    }

    // Getters and Setters
    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
        calculatePages();
        calculatePageStatus();
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
        calculatePages();
        calculatePageStatus();
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
        calculatePageStatus();
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
        calculatePageStatus();
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    /**
     * 获取开始索引
     */
    @Schema(description = "开始索引", hidden = true)
    public Long getStartIndex() {
        if (current != null && size != null && current > 0) {
            return (current - 1) * size + 1;
        }
        return 1L;
    }

    /**
     * 获取结束索引
     */
    @Schema(description = "结束索引", hidden = true)
    public Long getEndIndex() {
        if (current != null && size != null && current > 0) {
            Long end = current * size;
            return total != null && end > total ? total : end;
        }
        return 0L;
    }

    /**
     * 判断是否为空结果
     */
    @Schema(description = "是否为空", hidden = true)
    public Boolean isEmpty() {
        return records == null || records.isEmpty();
    }

    /**
     * 获取记录数量
     */
    @Schema(description = "当前页记录数", hidden = true)
    public Integer getRecordCount() {
        return records != null ? records.size() : 0;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", pages=" + pages +
                ", recordCount=" + getRecordCount() +
                ", hasPrevious=" + hasPrevious +
                ", hasNext=" + hasNext +
                '}';
    }
}
