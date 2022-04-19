package com.ddr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ddr.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
	Page<Tutorial> findByPublished(boolean published, Pageable pageable);
	Page<Tutorial> findByTitleContaining(String title, Pageable pageable);
	Page<Tutorial> findAll(Pageable paging);

	}