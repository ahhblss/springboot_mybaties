package com.rainwen.mybatis.page;

public class PageRequest extends AbstractPageRequest {
    private static final long serialVersionUID = -4541509938956089562L;
    private String sort;
    private long totalElements;

    public PageRequest(int page, int size) {
        this(page, size, (String)null);
    }

    public PageRequest(int page, int size, String sort) {
        super(page, size);
        this.sort = sort;
    }

    public String getSort() {
        return this.sort;
    }

    public Pageable next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    public PageRequest previous() {
        return this.getPageNumber() == 0?this:new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    public Pageable first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(!(obj instanceof PageRequest)) {
            return false;
        } else {
            PageRequest that = (PageRequest)obj;
            boolean sortEqual = this.sort == null?that.sort == null:this.sort.equals(that.sort);
            return super.equals(that) && sortEqual;
        }
    }

    public int hashCode() {
        return 31 * super.hashCode() + (null == this.sort?0:this.sort.hashCode());
    }

    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", new Object[]{Integer.valueOf(this.getPageNumber()), Integer.valueOf(this.getPageSize()), this.sort == null?null:this.sort.toString()});
    }
}
