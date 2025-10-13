package common.util;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class Paged<E> {
	@NotNull public List<E> list;
	@NotNull public Long count;

	public static <E> Paged<E> of(@NotNull PagedList<E> list) {
		var paged = new Paged<E>();
		paged.list = List.copyOf(list);
		paged.count = list.getCount();
		return paged;
	}

	public static <E> Paged<E> of(@NotNull List<E> list, @NotNull Long count) {
		var paged = new Paged<E>();
		paged.list = List.copyOf(list);
		paged.count = count;
		return paged;
	}

	public static <E> Paged<E> of(@NotNull List<E> list, @NotNull Integer count) {
		var paged = new Paged<E>();
		paged.list = List.copyOf(list);
		paged.count = count.longValue();
		return paged;
	}
}
