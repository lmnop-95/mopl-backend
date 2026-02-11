package tv.mopl.common.pagination;

public interface CursorRequest<S extends Enum<S>> {

    String cursor();

    String idAfter();

    int limit();

    SortDirection sortDirection();

    S sortBy();
}
