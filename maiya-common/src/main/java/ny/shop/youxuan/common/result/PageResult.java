package ny.shop.youxuan.common.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> list;
    private long total;
    private int pageIndex;
    private int pageSize;
    private int pages;

    public PageResult() {
        this.list = Collections.emptyList();
    }

    public PageResult(List<T> list, long total, int pi, int ps) {
        this.list = list;
        this.total = total;
        this.pageIndex = pi;
        this.pageSize = ps;
        this.pages = (ps > 0) ? (int) Math.ceil((double) total / ps) : 0;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> v) {
        this.list = v;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long v) {
        this.total = v;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int v) {
        this.pageIndex = v;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int v) {
        this.pageSize = v;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int v) {
        this.pages = v;
    }
}