package org.ezcomputing.server.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
/**
 * @author Bo Zhao
 *
 */
@Configuration
public class DynamoConfig {

	@Value("${amazon.dynamodb.region}")
    private String amazonDynamoRegion;
	
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;
    
   

    @Bean
    public AmazonDynamoDB getClient() {
    	return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)))
                .withRegion(Regions.fromName(amazonDynamoRegion))
                .build();
    }
    
    
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        
        return new DynamoDBMapper(getClient(), DynamoDBMapperConfig.DEFAULT);
    }
    
  

   
}