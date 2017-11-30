package com.rainwen.mybatis.page;

import java.io.Serializable;

public abstract class AbstractPageRequest implements Pageable, Serializable {
    private static final long serialVersionUID = 1L;
    private final int page;
    private final int size;

    public AbstractPageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        } else if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        } else {
            this.page = page;
            this.size = size;
        }
    }

    public int getPageSize() {
        return this.size;
    }

    public int getPageNumber() {
        return this.page;
    }

    public int getOffset() {
        return this.page * this.size;
    }

    public boolean hasPrevious() {
        return this.page > 0;
    }

    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    public abstract Pageable next();

    public abstract Pageable previous();

    public abstract Pageable first();

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + this.page;
        result1 = 31 * result1 + this.size;
        return result1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            AbstractPageRequest other = (AbstractPageRequest) obj;
            return this.page == other.page && this.size == other.size;
        } else {
            return false;
        }
    }
}