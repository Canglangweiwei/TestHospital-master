package com.aspsine.irecyclerview.universaladapter.recyclerview.support;

@SuppressWarnings("ALL")
public interface SectionSupport<T> {

    int sectionHeaderLayoutId();

    int sectionTitleTextViewId();

    String getTitle(T t);
}
