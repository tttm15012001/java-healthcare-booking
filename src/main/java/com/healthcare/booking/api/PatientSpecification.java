package com.healthcare.booking.api;

import com.healthcare.booking.multifilter.config.FilterConfig;
import com.healthcare.booking.multifilter.spec.EntitySpecification;
import com.healthcare.booking.model.PatientModel;
import com.healthcare.booking.provider.PatientDataProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PatientSpecification implements EntitySpecification<PatientModel> {
    @Override
    public Specification<PatientModel> filterBy(Map<String, String> filterParams) {
        return ((root, query, criteriaBuilder) -> {
            Specification<PatientModel> specification = Specification.where(null);

            for (FilterConfig filterConfig : PatientDataProvider.FILTER_OPTIONS) {
                String fieldName = filterConfig.getFieldName();
                String filterType = filterConfig.getFilterType();
                String fieldValue = filterParams.get(fieldName);

                if (fieldValue != null && !fieldValue.isEmpty()) {
                    specification = switch (filterType) {
                        case "like" -> specification.and((root1, query1, criteriaBuilder1) ->
                                criteriaBuilder1.like(criteriaBuilder1.lower(root1.get(fieldName)), "%" + fieldValue.toLowerCase() + "%")
                        );
                        case "equal" -> specification.and((root1, query1, criteriaBuilder1) ->
                                criteriaBuilder1.equal(root1.get(fieldName), fieldValue)
                        );
                        default -> specification;
                    };
                }
            }

            return specification.toPredicate(root, query, criteriaBuilder);
        });
    }
}
