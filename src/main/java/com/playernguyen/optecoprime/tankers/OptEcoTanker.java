package com.playernguyen.optecoprime.tankers;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Tanker represents a storage to cache and contain data in them.
 * Tanker works like a Collection. However, it has modified to work
 * fine with OptEco data.
 */
public class OptEcoTanker<T> {
    /**
     * A persisted collection.
     * This collection is immutable.
     */
    private final Collection<T> collection;

    public OptEcoTanker() {
        this.collection = new LinkedList<>();
    }

    /**
     * Constructor for another type of collection
     *
     * @param collection new instance of collection
     */
    public OptEcoTanker(Collection<T> collection) {
        this.collection = collection;
    }

    /**
     * A persisted collection contains data inside.
     *
     * @return a persisted collection
     */
    public Collection<T> getCollection() {
        return collection;
    }



}
