package ai.review.review_dot_ai.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.review.review_dot_ai.model.CodeSubmission;

@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, UUID> {
	List<CodeSubmission> findByUserId(UUID userId);
}
