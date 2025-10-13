package common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by deedarb on 10/4/17.
 */
public class PagedList<E> extends ArrayList<E> implements List<E> {

	private static final long serialVersionUID = 9160105579432115661L;
	private Long count = 0L;    //общее кол-во записей

	public PagedList() {
	}

	public PagedList(List<E> list, Long count) {
		this.addAll(list);
		this.count = count;
	}

	public PagedList(List<E> list, Integer count) {
		this.addAll(list);
		this.count = count.longValue();
	}

	public static <E> PagedList<E> of(List<E> list, Number count) {
		return new PagedList<>(list, count.intValue());
	}

	public <N> PagedList<N> transform(Function<E, N> transformFn) {
		List<N> list = this.stream().map(transformFn).toList();
		return PagedList.of(list, getCount());
	}

	public Long getCount() {
		return count;
	}

	public <N> Paged<N> toPaged(Function<E, N> transformFn) {
		List<N> list = this.stream().map(transformFn).toList();
		return Paged.of(list, getCount());
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Paged<E> toPaged() {
		return Paged.of(this);
	}
}
