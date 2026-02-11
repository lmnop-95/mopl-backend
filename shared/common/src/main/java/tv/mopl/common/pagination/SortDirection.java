package tv.mopl.common.pagination;

public enum SortDirection {

    ASCENDING,
    DESCENDING;

    public boolean isAscending() {
        return this == ASCENDING;
    }
}
