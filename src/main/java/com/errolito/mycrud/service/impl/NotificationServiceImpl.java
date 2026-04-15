package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.NotificationQuery;
import com.errolito.mycrud.entity.Notification;
import com.errolito.mycrud.repository.NotificationRepository;
import com.errolito.mycrud.service.NotificationService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
public class NotificationServiceImpl
        extends BaseCrudServiceImpl<Integer, NotificationQuery, Notification, NotificationRepository>
        implements NotificationService {

    protected NotificationServiceImpl(NotificationRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<Notification> buildLikeSpec(NotificationQuery userQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .build();
    }

    @Override
    protected Specification<Notification> buildEqualSpec(NotificationQuery userQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Notification not found");
    }

    @Override
    @Transactional
    public void saveAll(List<Notification> notifications) {
        repository.saveAll(notifications);
    }
}
