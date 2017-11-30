package com.rainwen.mybatis.page;

public interface Pageable {
    int getPageNumber();

    int getPageSize();

    int getOffset();

    String getSort();

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    boolean hasPrevious();
}
