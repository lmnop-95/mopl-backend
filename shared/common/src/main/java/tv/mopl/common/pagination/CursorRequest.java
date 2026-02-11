package tv.mopl.common.pagination;

import org.jspecify.annotations.Nullable;

public interface CursorRequest<S extends Enum<S>> {

    @Nullable
    String cursor();

    @Nullable
    String idAfter();

    int limit();

    SortDirection sortDirection();

    S sortBy();
}
