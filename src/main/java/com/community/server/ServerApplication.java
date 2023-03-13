package com.community.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {

	//private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.parse("128KB"));
		factory.setMaxRequestSize(DataSize.parse("128KB"));
		return factory.createMultipartConfig();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		SpringApplication.run(ServerApplication.class, args);

		/*logger.info("Start indexing minecraft clients.");
		Date start = new Date();
		MD5Files md5Files = new MD5Files();
		ServerDto[] serverDtos = md5Files.getServers();
		for (ServerDto serverDto : serverDtos) {
			logger.info("Indexing client: " + serverDto.getClient());
			md5Files.generate("launcher/client/" + serverDto.getClient());
			md5Files.input(serverDto.getClient());
		}
		Date end = new Date();
		logger.info("Completed indexing minecraft clients in " + (end.getTime() - start.getTime()) + " ms.");

		start = new Date();
		md5Files.generate();
		md5Files.inputLoader();
		end = new Date();
		logger.info("Completed indexing loader in " + (end.getTime() - start.getTime()) + " ms.");*/
	}

}
