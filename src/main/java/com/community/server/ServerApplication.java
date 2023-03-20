package com.community.server;

import com.community.server.dto.ServerDto;
import com.community.server.utils.MD5Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);
	private static final MD5Files md5Files = new MD5Files();

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.parse("128KB"));
		factory.setMaxRequestSize(DataSize.parse("128KB"));
		return factory.createMultipartConfig();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		/* Indexes files for minecraft and launcher */
		generateLauncherFiles();
		for (ServerDto serverDto : md5Files.getServers())
			generateClientFiles(serverDto);

		SpringApplication.run(ServerApplication.class, args);


	}

	public static void generateClientFiles(ServerDto serverDto) throws IOException, NoSuchAlgorithmException {
		logger.info("Indexing client: " + serverDto.getClient());
		md5Files.generate("launcher/client/" + serverDto.getClient());
		md5Files.input(serverDto.getClient());
	}

	public static void generateLauncherFiles() throws IOException, NoSuchAlgorithmException {
		logger.info("Indexing launcher..");
		md5Files.generate();
		md5Files.inputLoader();
	}

}
