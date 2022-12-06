package br.ufrn.imd.investbankapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InvestBankAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestBankAPIApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<LinkedHashMap<String, Object>> index(){
		LinkedHashMap<String, Object> content = new LinkedHashMap<String, Object>();

		content.put("project", "Invest Bank API");
    	content.put("version", "v1");

		ArrayList<String> technologies = new ArrayList<String>();
		technologies.add("Java");
		technologies.add("Spring Boot");
		technologies.add("Gradle");
		technologies.add("Postgres");
		technologies.add("Docker");

		content.put("technologies", technologies);

		LinkedHashMap<String, String> student = new LinkedHashMap<String, String>();
		student.put("name", "Rary Coringa");
		student.put("email", "rary.goncalves.123@ufrn.edu.br");
		student.put("id", "20210081823");

		content.put("student", student);

		LinkedHashMap<String, String> university_class = new LinkedHashMap<String, String>();
		university_class.put("name", "Programming Language II - IMD0040");
		university_class.put("course", "Information Technology");
		university_class.put("degree", "Bachelor");
		university_class.put("department", "Metrópole Digital Institute");
		university_class.put("university", "Federal University of the Rio Grande do Norte");

		content.put("class", university_class);

		LinkedHashMap<String, String> professor = new LinkedHashMap<String, String>();
		professor.put("name", "Itamir Filho");
		professor.put("email", "itamir.filho@imd.ufrn.br");

		content.put("professor", professor);

		return ResponseEntity.status(HttpStatus.OK).body(content);
	}
}
