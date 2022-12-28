package com.community.server;

import com.community.server.controller.dto.ServerDto;
import com.community.server.utils.MD5Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@SpringBootApplication
@ComponentScan
@EnableJpaAuditing
@EnableAutoConfiguration
public class ServerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.parse("128KB"));
		factory.setMaxRequestSize(DataSize.parse("128KB"));
		return factory.createMultipartConfig();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		SpringApplication.run(ServerApplication.class, args);

		logger.info("Start indexing minecraft clients.");
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
	}

}
