package common.util;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lists {

	public static <T> List<T> toList(T[] arr, ArrayList<T> defaultList) {
		return Optional.ofNullable(arr).map(a -> new ArrayList<>(Arrays.asList(a))).orElse(defaultList);
	}

	public static <T, M> List<M> toList(T[] arr, Function<T, M> mapper) {
		if (arr == null) {
			return new ArrayList<>();
		}
		return Stream.of(arr).map(mapper).collect(Collectors.toList());
	}

	@NotNull
	public static <T> List<T> toList(T[] arr) {
		return toList(arr, new ArrayList<>());
	}

	public static <T> Set<T> findDuplicates(Collection<T> collection) {
		Set<T> duplicates = new LinkedHashSet<>();
		Set<T> uniques = new HashSet<>();

		for (T t : collection) {
			if (!uniques.add(t)) {
				duplicates.add(t);
			}
		}

		return duplicates;
	}

	public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapFunc) {
		return collection.stream().map(mapFunc).collect(Collectors.toList());
	}

	public static <T, R> Set<R> mapToSet(Collection<T> collection, Function<T, R> mapFunc) {
		return collection.stream().map(mapFunc).collect(Collectors.toSet());
	}

	public static <T> Set<T> mapToSet(Collection<T> collection) {
		return collection.stream().collect(Collectors.toSet());
	}

	public static <T, R> List<R> map(T[] arr, Function<T, R> mapFunc) {
		return toList(arr).stream().map(mapFunc).collect(Collectors.toList());
	}

	public static <T, U, R> List<R> map(Collection<Pair<T, U>> collection, BiFunction<T, U, R> mapFunc) {
		return collection.stream().map(pair -> mapFunc.apply(pair.getLeft(), pair.getRight())).collect(Collectors.toList());
	}

	public static <T, U, R> List<R> map(Map<T, U> map, BiFunction<T, U, R> mapFunc) {
		return map.entrySet().stream().map(r -> mapFunc.apply(r.getKey(), r.getValue())).collect(Collectors.toList());
	}

	public static <K, V, R> Map<K, R> map(Map<K, V> map, Function<V, R> mapFunc) {
		return map.entrySet().stream()
			.collect(
				Collectors.toMap(
					Map.Entry::getKey,
					entry -> mapFunc.apply(entry.getValue())
				)
			);
	}

	public static boolean isNotEmpty(Collection list) {
		return list != null && !list.isEmpty();
	}

	public static <T> Optional<List<T>> of(List<T> list) {
		if (isNotEmpty(list)) {
			return Optional.of(list);
		}
		return Optional.empty();
	}

	public static <T> List<T> filter(Collection<T> collection, Predicate<T> filterFn) {
		return collection.stream().filter(filterFn).collect(Collectors.toList());
	}

	public static <K, T> List<K> filter(Collection<T> collection, Predicate<T> filterFn, Function<T, K> mapper) {
		return collection.stream().filter(filterFn).map(mapper).collect(Collectors.toList());
	}

	public static <K, T> Map<K, T> toMap(Collection<T> collection, Function<T, K> supplier) {
		return collection.stream().collect(Collectors.toMap(supplier, Function.identity()));
	}

	public static <K, T, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyFn, Function<T, V> valueFn) {
		return collection.stream().collect(Collectors.toMap(keyFn, valueFn));
	}

	public static <T> Optional<List<T>> nonEmpty(List<T> list) {
		if (list == null || list.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(list);
	}

	public static boolean isEmpty(Collection list) {
		return list == null || list.isEmpty();
	}

	public static <T> Optional<T> first(List<T> list) {
		if (list == null || list.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(list.get(0));
	}

	public static <T> @Nullable T firstOrNull(List<T> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return Optional.of(list.get(0)).orElse(null);
	}

	public static <T> List<T> ofNullable(List<T> list) {
		return list == null ? listOf() : list;
	}

	public static <T> List<T> concat(List<T> l1, List<T> l2) {
		var list = new ArrayList<>(l1);
		list.addAll(l2);
		return list;
	}

	public static <T> List<T> concat(Collection<List<T>> lists) {
		if (lists == null) {
			return listOf();
		}
		return lists.stream().flatMap(List::stream).collect(Collectors.toList());
	}

	public static <T> List<T> listOf() {
		return new ArrayList<>();
	}

	@SafeVarargs
	public static <T> List<T> coalesce(List<T>... lists) {
		return Stream.of(lists)
			.filter(list -> isNotEmpty(list))
			.findFirst()
			.orElse(List.of());
	}

	@SafeVarargs
	public static <T> List<T> listOf(T... elements) {
		if (elements == null) {
			return listOf();
		}
		return toList(elements);
	}

	public static <K, V> Map<K, V> mapOf() {
		return Map.of();
	}

	@SafeVarargs
	public static <K, V> Map<K, V> mapOf(Pair<K, V>... pairs) {
		if (pairs == null) {
			return mapOf();
		}

		var map = new HashMap<K, V>(pairs.length);
		for (Pair<K, V> pair : pairs) {
			map.put(pair.getLeft(), pair.getRight());
		}
		return map;
	}
}
