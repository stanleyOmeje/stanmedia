package com.stan.notification.repository;

import com.stan.notification.domaim.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
