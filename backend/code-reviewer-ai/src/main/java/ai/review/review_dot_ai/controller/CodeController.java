package ai.review.review_dot_ai.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.review.review_dot_ai.model.CodeSubmission;
import ai.review.review_dot_ai.model.CodeVersion;
import ai.review.review_dot_ai.service.CodeService;
import ai.review.review_dot_ai.service.CodeVersionService;

@RestController
@RequestMapping("/api/code")
public class CodeController {
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CodeVersionService versionService;
	
	@GetMapping("/get/hello")
	public String print() {
		return "Hello World...!Bye";
	}
	
	// api that store data into databases...
	@PostMapping("/upload")
	public ResponseEntity<CodeSubmission> uploadCode(@RequestBody CodeSubmission codeSubmission){
		CodeSubmission submission = codeService.createSubmission(codeSubmission);
		return ResponseEntity.status(HttpStatus.OK).body(submission);
	}
	
	// api 
	@PostMapping("/analyze/{submissionId}")
	public ResponseEntity<CodeVersion> analyzeCode(@PathVariable UUID submissionId) {
		// fetch letested version...
		Optional<CodeVersion> latest = versionService.getVersion(submissionId).stream()
										.max(Comparator.comparingInt(CodeVersion::getVersionNo));
		if(latest.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(versionService.analyzeVersion(latest.get().getId()));
		}
		return ResponseEntity.notFound().build();
	}
	
	
	@GetMapping("/version/{submissionId}")
    public ResponseEntity<List<CodeVersion>> getVersions(@PathVariable UUID submissionId) {
        return ResponseEntity.ok(versionService.getVersions(submissionId));
    }
	
	@PostMapping("/version/{submissionId}/new")
    public ResponseEntity<CodeVersion> createNewVersion(@PathVariable UUID submissionId, @RequestBody String code) {
        return ResponseEntity.ok(versionService.createNextVersion(submissionId, code));
    }
	
	// see all code submission...
	@GetMapping("/submissions/{userId}")
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@PathVariable UUID userId) {
        return ResponseEntity.ok(codeService.getUserSubmissions(userId));
    }

	@PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareVersions(@RequestBody Map<String, UUID> request) {
        UUID versionAId = request.get("versionA");
        UUID versionBId = request.get("versionB");

        CodeVersion vA = versionService.getVersionByVersionId(versionAId);
        CodeVersion vB= versionService.getVersionByVersionId(versionBId);

        Map<String, Object> result = new HashMap<>();
        result.put("versionA", vA);
        result.put("versionB", vB);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
	
	
	
}
