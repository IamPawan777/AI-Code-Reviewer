package ai.review.review_dot_ai.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.review.review_dot_ai.model.CodeVersion;

@Repository
public interface CodeVersionRepository extends JpaRepository<CodeVersion, UUID> {
	
	List<CodeVersion> findBySubmissionId(UUID submissionId);

	Optional<CodeVersion> findTopBySubmissionIdOrderByVersionNoDesc(UUID submissionId);
}
