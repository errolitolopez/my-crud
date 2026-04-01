package com.errolito.mycrud.shared;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Fluent JPA Criteria Specification builder supporting nested groups and common predicates.
 *
 * @param <T> the entity type
 */
public class SpecBuilder<T> {

    private final Root<T> root;
    private final CriteriaBuilder cb;
    private final List<Predicate> predicates = new ArrayList<>();

    private SpecBuilder(Root<T> root, CriteriaBuilder cb) {
        this.root = root;
        this.cb = cb;
    }

    /** Creates a new SpecBuilder for the given root and CriteriaBuilder. */
    public static <T> SpecBuilder<T> of(Root<T> root, CriteriaBuilder cb) {
        return new SpecBuilder<>(root, cb);
    }

    // ---------------- Basic Predicates ----------------

    /** AND equals predicate. */
    public <V> SpecBuilder<T> andEqual(String field, V value) {
        if (value != null) predicates.add(cb.equal(root.get(field), value));
        return this;
    }

    /** OR equals predicate. */
    public <V> SpecBuilder<T> orEqual(String field, V value) {
        if (value != null) predicates.add(cb.or(cb.equal(root.get(field), value)));
        return this;
    }

    /** AND not equal predicate. */
    public <V> SpecBuilder<T> andNotEqual(String field, V value) {
        if (value != null) predicates.add(cb.notEqual(root.get(field), value));
        return this;
    }

    /** OR not equal predicate. */
    public <V> SpecBuilder<T> orNotEqual(String field, V value) {
        if (value != null) predicates.add(cb.or(cb.notEqual(root.get(field), value)));
        return this;
    }

    /** AND like predicate (case-insensitive). */
    public SpecBuilder<T> andLike(String field, String value) {
        if (value != null && !value.isBlank())
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%"));
        return this;
    }

    public SpecBuilder<T> andLike(Expression<String> expression, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.like(expression, "%" + value + "%"));
        }
        return this;
    }

    public <V> SpecBuilder<T> andEqual(Expression<V> expression, V value) {
        if (value != null) {
            predicates.add(cb.equal(expression, value));
        }
        return this;
    }

    /** OR like predicate (case-insensitive). */
    public SpecBuilder<T> orLike(String field, String value) {
        if (value != null && !value.isBlank())
            predicates.add(cb.or(cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%")));
        return this;
    }

    /** AND flexible range: from and/or to can be null. */
    public <V extends Comparable<? super V>> SpecBuilder<T> andRange(String field, V from, V to) {
        Path<V> path = root.get(field);
        if (from != null) predicates.add(cb.greaterThanOrEqualTo(path, from));
        if (to != null) predicates.add(cb.lessThanOrEqualTo(path, to));
        return this;
    }

    /** OR flexible range: from and/or to can be null. */
    public <V extends Comparable<? super V>> SpecBuilder<T> orRange(String field, V from, V to) {
        Path<V> path = root.get(field);
        if (from != null) predicates.add(cb.or(cb.greaterThanOrEqualTo(path, from)));
        if (to != null) predicates.add(cb.or(cb.lessThanOrEqualTo(path, to)));
        return this;
    }

    /** AND strict between: both bounds must be non-null. */
    public <V extends Comparable<? super V>> SpecBuilder<T> andBetween(String field, V from, V to) {
        if (from != null && to != null) predicates.add(cb.between(root.get(field), from, to));
        return this;
    }

    /** OR strict between: both bounds must be non-null. */
    public <V extends Comparable<? super V>> SpecBuilder<T> orBetween(String field, V from, V to) {
        if (from != null && to != null) predicates.add(cb.or(cb.between(root.get(field), from, to)));
        return this;
    }

    /** AND true predicate. */
    public SpecBuilder<T> isTrue(String field) {
        predicates.add(cb.isTrue(root.get(field)));
        return this;
    }

    /** AND false predicate. */
    public SpecBuilder<T> isFalse(String field) {
        predicates.add(cb.isFalse(root.get(field)));
        return this;
    }

    /** AND in collection predicate. */
    public <V> SpecBuilder<T> andIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(root.get(field).in(values));
        return this;
    }

    /** OR in collection predicate. */
    public <V> SpecBuilder<T> orIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(cb.or(root.get(field).in(values)));
        return this;
    }

    /** AND not in collection predicate. */
    public <V> SpecBuilder<T> andNotIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(cb.not(root.get(field).in(values)));
        return this;
    }

    /** OR not in collection predicate. */
    public <V> SpecBuilder<T> orNotIn(String field, Collection<V> values) {
        if (values != null && !values.isEmpty()) predicates.add(cb.or(cb.not(root.get(field).in(values))));
        return this;
    }

    // ---------------- Nested Groups ----------------

    /** AND a nested group of predicates. */
    public SpecBuilder<T> andGroup(SpecBuilder<T> group) {
        if (group != null && !group.predicates.isEmpty()) {
            predicates.add(cb.and(group.predicates.toArray(new Predicate[0])));
        }
        return this;
    }

    /** OR a nested group of predicates. */
    public SpecBuilder<T> orGroup(SpecBuilder<T> group) {
        if (group != null && !group.predicates.isEmpty()) {
            predicates.add(cb.or(group.predicates.toArray(new Predicate[0])));
        }
        return this;
    }

    // ---------------- Build ----------------

    /** Builds the final AND combination of all predicates. */
    public Predicate build() {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}