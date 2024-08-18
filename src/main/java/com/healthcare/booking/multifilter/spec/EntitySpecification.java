package com.healthcare.booking.multifilter.spec;

import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public interface EntitySpecification<T> {
    /**
     * Builds a Specification for filtering based on the provided parameters.
     *
     * @param filterParams a map of filter parameters where the key is the field name and the value is the filter value
     * @return a Specification for filtering the entity
     */
    Specification<T> filterBy(Map<String, String> filterParams);
}
