package com.healthcare.booking.patient.spec;

import com.healthcare.booking.multifilter.config.FilterConfig;
import com.healthcare.booking.multifilter.spec.EntitySpecification;
import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.provider.PatientDataProvider;
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
                    switch (filterType) {
                        case "like":
                            specification = specification.and((root1, query1, criteriaBuilder1) ->
                                criteriaBuilder1.like(criteriaBuilder1.lower(root1.get(fieldName)), "%" + fieldValue.toLowerCase() + "%")
                            );
                            break;
                        case "equal":
                            specification = specification.and((root1, query1, criteriaBuilder1) ->
                                criteriaBuilder1.equal(root1.get(fieldName), fieldValue)
                            );
                            break;
                    }
                }
            }

            return specification.toPredicate(root, query, criteriaBuilder);
        });
    }
}
