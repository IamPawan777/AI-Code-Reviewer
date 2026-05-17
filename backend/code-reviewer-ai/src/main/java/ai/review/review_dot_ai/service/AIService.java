package ai.review.review_dot_ai.service;

import java.net.http.HttpRequest;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AIService {

//	@Value("${google_api_key}")
//	private String apiKey;
//	
//	private final RestTemplate restTemplate = new RestTemplate();
//	
//	private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent"; 
////	private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";
//
//	
//	// AI service to fix code...
//	public String analyzeCode(String code) {
//		if(apiKey==null || apiKey.isEmpty()) {
//			return "Error: Google API key is Missing. Please set GOOGLE_API_KEY environment variable.";
//		}
//		
//		HttpHeaders headers=new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("x-goog-api-key", apiKey);
//		
//		
//		// Groq REST : request body JSON 
//		// {"contents": [{"parts": [{"text": "...query?..."}] }]}
//		// { "model": "...model-name...",  "input": "...query..."	}		
//		
//		Map<String, Object> body = new HashMap<>();
//        List<Map<String, Object>> contents = new ArrayList<>();
//        Map<String, Object> contentPart = new HashMap<>();
//        List<Map<String, String>> parts = new ArrayList<>();
//        Map<String, String> testPart = new HashMap<>();
//        String promptText = "query: " + code;
//
//        testPart.put("text", promptText);
//        parts.add(testPart);
//        contentPart.put("parts", parts);
//        contents.add(contentPart);
//        body.put("contents", contents);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//		try {
//			// now go to gemini server here...
//			ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_URL, request, Map.class);
//			
//			if(response.getStatusCode() == HttpStatus.OK) {
//                Map<String, Object> responseBody = response.getBody();
//				
//				if(responseBody == null || !responseBody.containsKey("candidates")) {
//					return "Error: No candidates return from Gemini API";
//				}
//				
//				// Gemini REST : response JSON
//				// {"candidates": [{"content": [{"parts":[{"text": "...query?..."}] }]}     // for one user multiple response
//				 
//				// Grok REST : response JSON
//				// { "output": [{"content": [{"text": "..responce..."}] }] }
//				
//				 List<Map<String, Object>> candidates = (List<Map<String, Object>> )responseBody.get("candidates");
//				 if(candidates.isEmpty()){
//					 return "Error: Empty candidates list";
//				 }
//				 Map<String, Object> firstCandidate = candidates.get(0);
//				 Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
//				 List<Map<String, String>> responseParts = (List<Map<String, String>>) content.get("parts");
//						 
//				 if(responseParts.isEmpty()) {
//					 return "Error: No content parts in the response";
//				 }				 
//				 // final response...
//				 return (String) responseParts.get(0).get("text");
//			}
//			else {
//				return "Error analyzing code: " + response.getStatusCode();
//			}
//		}
//		catch (HttpClientErrorException e) {
////			 System.out.println(e.getResponseBodyAsString());
//			 return "Error in communicating with Google API service: " + e.getResponseBodyAsString();
//		}
//	}
	
	
	@Value("${groq_api_key}")
	private String apiKey;
	
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String GROQ_URL="https://api.groq.com/openai/v1/responses";	
	
	// AI service to analyze code..
	public String analyzeCode(String code) {
		// check api key at first...
		if(apiKey==null || apiKey.isEmpty()) {
			return "Error: Groq API key missing. Please set GROK_API_KEY in environment variable";
		}
		
		// 1. set header...
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.setBasicAuth(apiKey);
		headers.set("Authorization", "Bearer "+apiKey);
		
		String promptText = "You are a senior software engineer.\r\n"
				+ "\r\n"
				+ "Analyze the given code and respond STRICTLY in the defined format.\r\n"
				+ "\r\n"
				+ "RULES:\r\n"
				+ "- Do NOT add any explanation outside the format\r\n"
				+ "- Identify ALL errors (syntax, logical, structural, missing keywords like class, etc.)\r\n"
				+ "- Treat missing declarations (class, return types, imports) as ERRORS\r\n"
				+ "- Fix ALL errors in the FIXED CODE\r\n"
				+ "- Generate EXACTLY two versions:\r\n"
				+ "  1) FIXED CODE\r\n"
				+ "  2) OPTIMIZED VERSION\r\n"
				+ "\r\n"
				+ "FIXED CODE RULES:\r\n"
				+ "- Add comments ONLY on lines where errors originally existed\r\n"
				+ "- If a line is newly added to fix an error, comment on that line\r\n"
				+ "- Each comment must follow:\r\n"
				+ "  // Problem <n>: <3 to 6 words>\r\n"
				+ "- Number problems sequentially\r\n"
				+ "- If multiple issues on same line, use one comment\r\n"
				+ "- Do NOT add comments anywhere else\r\n"
				+ "\r\n"
				+ "OPTIMIZED VERSION RULES:\r\n"
				+ "- No comments\r\n"
				+ "- Clean and best-practice code\r\n"
				+ "\r\n"
				+ "GENERAL RULES:\r\n"
				+ "- Keep same programming language\r\n"
				+ "- Do NOT repeat input code\r\n"
				+ "- Do NOT skip structural errors\r\n"
				+ "- Ensure final code compiles correctly\r\n"
				+ "\r\n"
				+ "INPUT CODE:\r\n"
				+ "{code}\r\n"
				+ "\r\n"
				+ "OUTPUT FORMAT:\r\n"
				+ "\r\n"
				+ "FIXED ERROR:\r\n"
				+ "<fixed code with inline comments>\r\n"
				+ "\r\n"
				+ "OPTIMIZED VERSION:\r\n"
				+ "<optimized clean code>\r\n"
				+ "\r\n"
				+ "TIME COMPLEXITY:\r\n"
				+ "- Original: O(?)\r\n"
				+ "- Optimized: O(?)\r\n"
				+ "\r\n"
				+ "SPACE COMPLEXITY:\r\n"
				+ "- Original: O(?)\r\n"
				+ "- Optimized: O(?)"+code;
		
		// Groq REST : request body JSON 
		// { 
		//   "model": "...model-name...",  
		//   "input": "...query..."	
		// }	
	    Map<String, Object> body = new HashMap<>();
	    body.put("model", "qwen/qwen3-32b");
	    body.put("input", promptText);
	    
	    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
	    try {
	    	// go to Groq server here...
	    	ResponseEntity<Map> response = restTemplate.postForEntity(GROQ_URL, request, Map.class);
	    	
	    	if(response.getStatusCode()==HttpStatus.OK) {
	    		// Grok REST : response JSON
	    		// { "output": [{"content": [{"text": "..responce..."}] }] }
	    		
	    		Map<String, Object> responseBody = response.getBody();
	    		if (responseBody == null || !responseBody.containsKey("output")) {
	                return "Error: No output returned from Groq API";
	            }
	    		
	    		List<Map<String, Object>> output = (List<Map<String, Object>>) responseBody.get("output");
	    		if (output==null || output.isEmpty()) {
	                return "Error: Empty output list";
	            }
	    		
	    		for (Map<String, Object> item : output) {

	    		    String type = (String) item.get("type");

	    		    // we only care about "message"
	    		    if ("message".equals(type)) {

	    		        List<Map<String, Object>> content =
	    		                (List<Map<String, Object>>) item.get("content");

	    		        if (content == null || content.isEmpty()) {
	    		            return "Error: No content in message";
	    		        }

	    		        for (Map<String, Object> part : content) {
	    		            if ("output_text".equals(part.get("type"))) {
	    		                return (String) part.get("text");      // ✅ final response
	    		            }
	    		        }
	    		    }
	    		}

	    		// fallback if nothing found
	    		return "Error: No valid message content found";
	        }
	    	else {
	            return "Error analyzing code: " + response.getStatusCode();
	        }
	    }
	    catch(Exception e) {
	    	return "Excetion"+e.getMessage();
	    }
	}
	
	

}
