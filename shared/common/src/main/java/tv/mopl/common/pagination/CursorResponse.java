package tv.mopl.common.pagination;

import java.util.List;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public record CursorResponse<T>(
    List<T> data,
    @Nullable String nextCursor,
    @Nullable String nextIdAfter,
    boolean hasNext,
    long totalCount,
    String sortBy,
    SortDirection sortDirection
) {

    public CursorResponse {
        data = data == null ? List.of() : List.copyOf(data);
    }

    public static <T> CursorResponse<T> of(
        List<T> itemsWithExtra,
        int size,
        long totalCount,
        String sortBy,
        SortDirection sortDirection,
        Function<T, String> cursorExtractor,
        Function<T, String> idExtractor
    ) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be positive, but was: " + size);
        }
        if (itemsWithExtra.isEmpty()) {
            return new CursorResponse<>(List.of(), null, null, false, totalCount, sortBy, sortDirection);
        }
        boolean hasNext = itemsWithExtra.size() > size;
        List<T> items = hasNext ? itemsWithExtra.subList(0, size) : itemsWithExtra;
        String nextCursor = hasNext ? cursorExtractor.apply(items.getLast()) : null;
        String nextIdAfter = hasNext ? idExtractor.apply(items.getLast()) : null;
        return new CursorResponse<>(items, nextCursor, nextIdAfter, hasNext, totalCount, sortBy, sortDirection);
    }

    public static <T> CursorResponse<T> empty(String sortBy, SortDirection sortDirection) {
        return new CursorResponse<>(List.of(), null, null, false, 0, sortBy, sortDirection);
    }

    public <R> CursorResponse<R> map(Function<T, R> mapper) {
        List<R> mapped = data.stream().map(mapper).toList();
        return new CursorResponse<>(mapped, nextCursor, nextIdAfter, hasNext, totalCount, sortBy, sortDirection);
    }
}
