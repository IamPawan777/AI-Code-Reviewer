package ai.review.review_dot_ai.model;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "code_version")
public class CodeVersion {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "submission_id")
	private CodeSubmission submission;
	
	@Column(name = "version_no")
	private Integer versionNo;
	
	@Column(columnDefinition = "TEXT")
	private String code;
	
	// AI response.....
	@Column(columnDefinition = "TEXT")
	private String analysis;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt=LocalDateTime.now();
	}
}
