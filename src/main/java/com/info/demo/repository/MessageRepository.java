package com.info.demo.repository;

import com.info.demo.model.StageWiseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<StageWiseMessage, Long> {
    List<StageWiseMessage> findByStage(int stage);
}
