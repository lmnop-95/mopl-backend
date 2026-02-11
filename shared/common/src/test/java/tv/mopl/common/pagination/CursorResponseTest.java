package tv.mopl.common.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CursorResponseTest {

    @Test
    void createsWithAllFields() {
        CursorResponse<String> response = new CursorResponse<>(
            List.of("a", "b"), "cursor2", "id2", true, 100, "NAME", SortDirection.ASCENDING
        );

        assertThat(response.data()).containsExactly("a", "b");
        assertThat(response.nextCursor()).isEqualTo("cursor2");
        assertThat(response.nextIdAfter()).isEqualTo("id2");
        assertThat(response.hasNext()).isTrue();
        assertThat(response.totalCount()).isEqualTo(100);
        assertThat(response.sortBy()).isEqualTo("NAME");
        assertThat(response.sortDirection()).isEqualTo(SortDirection.ASCENDING);
    }

    @Test
    void dataIsDefensivelyCopied() {
        ArrayList<String> mutableList = new ArrayList<>(List.of("a", "b"));
        CursorResponse<String> response = new CursorResponse<>(
            mutableList, null, null, false, 0, "NAME", SortDirection.ASCENDING
        );

        mutableList.add("c");

        assertThat(response.data()).containsExactly("a", "b");
    }

    @Test
    void ofWithMoreItemsThanSizeHasNextTrue() {
        List<String> itemsWithExtra = List.of("a", "b", "c");

        CursorResponse<String> response = CursorResponse.of(
            itemsWithExtra, 2, 10, "NAME", SortDirection.ASCENDING,
            item -> "cursor_" + item, item -> "id_" + item
        );

        assertThat(response.data()).containsExactly("a", "b");
        assertThat(response.nextCursor()).isEqualTo("cursor_b");
        assertThat(response.nextIdAfter()).isEqualTo("id_b");
        assertThat(response.hasNext()).isTrue();
        assertThat(response.totalCount()).isEqualTo(10);
        assertThat(response.sortBy()).isEqualTo("NAME");
        assertThat(response.sortDirection()).isEqualTo(SortDirection.ASCENDING);
    }

    @Test
    void ofWithExactSizeHasNextFalse() {
        List<String> items = List.of("a", "b");

        CursorResponse<String> response = CursorResponse.of(
            items, 2, 2, "NAME", SortDirection.DESCENDING,
            item -> "cursor_" + item, item -> "id_" + item
        );

        assertThat(response.data()).containsExactly("a", "b");
        assertThat(response.nextCursor()).isNull();
        assertThat(response.nextIdAfter()).isNull();
        assertThat(response.hasNext()).isFalse();
    }

    @Test
    void ofWithFewerItemsThanSizeHasNextFalse() {
        List<String> items = List.of("a");

        CursorResponse<String> response = CursorResponse.of(
            items, 2, 1, "NAME", SortDirection.ASCENDING,
            item -> "cursor_" + item, item -> "id_" + item
        );

        assertThat(response.data()).containsExactly("a");
        assertThat(response.nextCursor()).isNull();
        assertThat(response.nextIdAfter()).isNull();
        assertThat(response.hasNext()).isFalse();
    }

    @Test
    void ofWithEmptyListReturnsEmptyResponse() {
        CursorResponse<String> response = CursorResponse.of(
            List.of(), 10, 0, "NAME", SortDirection.ASCENDING,
            item -> item, item -> item
        );

        assertThat(response.data()).isEmpty();
        assertThat(response.nextCursor()).isNull();
        assertThat(response.nextIdAfter()).isNull();
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(0);
    }

    @Test
    void emptyReturnsEmptyResponse() {
        CursorResponse<String> response = CursorResponse.empty("CREATED_AT", SortDirection.DESCENDING);

        assertThat(response.data()).isEmpty();
        assertThat(response.nextCursor()).isNull();
        assertThat(response.nextIdAfter()).isNull();
        assertThat(response.hasNext()).isFalse();
        assertThat(response.totalCount()).isEqualTo(0);
        assertThat(response.sortBy()).isEqualTo("CREATED_AT");
        assertThat(response.sortDirection()).isEqualTo(SortDirection.DESCENDING);
    }

    @Test
    void ofWithNegativeSizeThrowsException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> CursorResponse.of(
                List.of("a"), -1, 0, "NAME", SortDirection.ASCENDING,
                item -> item, item -> item
            )
            )
            .withMessageContaining("size");
    }

    @Test
    void mapTransformsDataAndPreservesMetadata() {
        CursorResponse<String> original = new CursorResponse<>(
            List.of("1", "2", "3"), "cursor3", "id3", true, 100, "NAME", SortDirection.ASCENDING
        );

        CursorResponse<Integer> mapped = original.map(Integer::parseInt);

        assertThat(mapped.data()).containsExactly(1, 2, 3);
        assertThat(mapped.nextCursor()).isEqualTo("cursor3");
        assertThat(mapped.nextIdAfter()).isEqualTo("id3");
        assertThat(mapped.hasNext()).isTrue();
        assertThat(mapped.totalCount()).isEqualTo(100);
        assertThat(mapped.sortBy()).isEqualTo("NAME");
        assertThat(mapped.sortDirection()).isEqualTo(SortDirection.ASCENDING);
    }
}
