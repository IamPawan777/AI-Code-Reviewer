package ai.review.review_dot_ai.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.review.review_dot_ai.model.CodeSubmission;
import ai.review.review_dot_ai.model.CodeVersion;
import ai.review.review_dot_ai.repository.CodeSubmissionRepository;
import ai.review.review_dot_ai.repository.CodeVersionRepository;

@Service
public class CodeVersionService {
	
	@Autowired
	private CodeVersionRepository codeVersionRepository;
	
	@Autowired
	private AIService aiService;
	
	@Autowired
	private CodeSubmissionRepository codeSubmissionRepository;
	
	public List<CodeVersion> getVersion(UUID submissionId) {
		return codeVersionRepository.findBySubmissionId(submissionId);
	}
	
	public CodeVersion analyzeVersion(UUID versionId) {
		CodeVersion version = codeVersionRepository.findById(versionId)
				.orElseThrow(() -> new RuntimeException("Version not found..."));
		
		// Code goes into the gemini api... 
		String analysis=aiService.analyzeCode(version.getCode());
		version.setAnalysis(analysis);
		return codeVersionRepository.save(version);		
	}

	 public List<CodeVersion> getVersions(UUID submissionId) {
		 return codeVersionRepository.findBySubmissionId(submissionId);
	 }

	 
	 
	 public CodeVersion createNextVersion(UUID submissionId, String newCode) {		 
         CodeSubmission submission = codeSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submussion not found"));

         Optional<CodeVersion> latest = codeVersionRepository.findTopBySubmissionIdOrderByVersionNoDesc(submissionId);

         int nextVersion = latest.map(v -> v.getVersionNo() + 1).orElse(1);

         CodeVersion version = new CodeVersion();
         version.setSubmission(submission);
         version.setVersionNo(nextVersion);
         version.setCode(newCode);
         version.setAnalysis("Pending analysis...");

         return codeVersionRepository.save(version);
     }
	 
	 public CodeVersion getVersionByVersionId(UUID versionId) {
        return codeVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));
     }
}
