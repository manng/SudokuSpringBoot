package sudoku;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SudokuSpringBootConfig {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(metadata())
				                                      .select()
				                                      .apis(RequestHandlerSelectors.basePackage("sudoku.rest.controller"))
				                                      .paths(regex("/sudoku.*"))
				                                      .build();
	}

	private ApiInfo metadata() {
		Contact contact = new Contact("Gary Mann"
				                      ,"https://www.linkedin.com/in/gary-mann-949a8515"
				                      ,"gary.mann1@optusnet.com.au");
		return new ApiInfoBuilder().title("Sudoku Solver REST API")
				                   .description("Sudoku Solver API written by Gary Mann")
				                   .contact(contact)
				                   .version("1.0")
				                   .build();	
	}
}
