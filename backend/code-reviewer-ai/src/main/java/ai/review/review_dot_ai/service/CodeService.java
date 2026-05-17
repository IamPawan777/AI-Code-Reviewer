package ai.review.review_dot_ai.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.review.review_dot_ai.model.CodeSubmission;
import ai.review.review_dot_ai.model.CodeVersion;
import ai.review.review_dot_ai.repository.CodeSubmissionRepository;
import ai.review.review_dot_ai.repository.CodeVersionRepository;

@Service
public class CodeService {
	
	@Autowired
	private CodeSubmissionRepository codeSubmissionRepository;
	
	@Autowired
	private CodeVersionRepository codeVersionRepository;

	public CodeSubmission createSubmission(CodeSubmission codeSubmission) {
		CodeSubmission saved = codeSubmissionRepository.save(codeSubmission);

        // create inital version
        CodeVersion version = new CodeVersion();
        version.setSubmission(saved);
        version.setVersionNo(1);
        version.setCode(codeSubmission.getCode());
        version.setAnalysis("Pending analysis...");
        codeVersionRepository.save(version);

        return saved;
	}

	public List<CodeSubmission> getUserSubmissions(UUID userId) {
		return codeSubmissionRepository.findByUserId(userId);
    }

	
}
