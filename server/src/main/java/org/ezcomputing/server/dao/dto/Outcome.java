
package org.ezcomputing.server.dao.dto;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


/**
 * @author Bo Zhao
 *
 */
@DynamoDBTable(tableName = "outcome")
public class Outcome {
	
	public Outcome() {
		
	}

	@DynamoDBHashKey(attributeName = "function-name")
    private String name;
	
	//use version and input to create hash value
	@DynamoDBRangeKey(attributeName = "hash-code")
    private String hash;

	@DynamoDBAttribute(attributeName = "version")
    private String version;

    @DynamoDBAttribute(attributeName = "source")
    private String source;

    @DynamoDBAttribute(attributeName = "request")
    private String request;
    
    @DynamoDBAttribute(attributeName = "response")
    private String response;
    
    //pending, success, failed
    @DynamoDBAttribute(attributeName = "outcomeStatus")
    private String outcomeStatus = Constants.OUTCOME_STATUS.pending;
    
    @DynamoDBAttribute(attributeName = "errorMessage")
    private String errorMessage = "";
    
    @DynamoDBAttribute(attributeName = "invokeCount")
    private long invokeCount = 0;
    
    @DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.CREATE)
    private Date createDate;
    
    @DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.ALWAYS)
    private Date updateDate;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public long getInvokeCount() {
		return invokeCount;
	}

	public void setInvokeCount(long invokeCount) {
		this.invokeCount = invokeCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getOutcomeStatus() {
		return outcomeStatus;
	}

	public void setOutcomeStatus(String outcomeStatus) {
		this.outcomeStatus = outcomeStatus;
	}

	
    
}