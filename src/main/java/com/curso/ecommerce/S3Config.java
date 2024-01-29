package com.curso.ecommerce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.internal.S3ClientOption;


@Configuration
public class S3Config {
	@Value("${aws.accesKeyId}")
	private String accesKey;
	
	@Value("${aws.secretKey}")
	private String secretKey;
	
	@Bean
	public S3Client s3Client() {
		Region region=Region.US_EAST_1;
		AwsBasicCredentials aws_cred=AwsBasicCredentials.create(accesKey, secretKey);
		S3Client s3Client=S3Client.builder()
				.region(region)
				.credentialsProvider(StaticCredentialsProvider.create(aws_cred))
				.build();
		return s3Client;
	}
}
