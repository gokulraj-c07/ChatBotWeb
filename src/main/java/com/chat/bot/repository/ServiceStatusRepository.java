package com.chat.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.bot.model.ServiceStatus;

@Repository
public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {

}
