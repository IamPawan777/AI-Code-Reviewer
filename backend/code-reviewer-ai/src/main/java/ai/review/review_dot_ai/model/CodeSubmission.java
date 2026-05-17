package ai.review.review_dot_ai.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "code_submission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeSubmission {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "user_id", nullable = false)
	private UUID userId;
	
//	@Column(name = "file_name")
	private String filename;
	private String language;
	
	@Column(columnDefinition = "TEXT")
	private String code;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt=LocalDateTime.now();
	}
}
