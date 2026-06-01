package com.chat.bot.repository;

import com.chat.bot.model.Intent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntentRepository extends JpaRepository<Intent, Long> {

    List<Intent> findByParentIdIsNullAndIsActiveTrueOrderByDisplayOrderAsc();

    List<Intent> findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(Long parentId);

    List<Intent> findByIsActiveTrueOrderByDisplayOrderAsc();
}