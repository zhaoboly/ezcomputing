package org.ezcomputing.server.dao.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
/**
 * @author Bo Zhao
 *
 */
@DynamoDBDocument
public class JobLog {
	
	public JobLog() {
		
	}

	@DynamoDBAttribute(attributeName = "workerName")
	private String workerName;
	@DynamoDBAttribute(attributeName = "endTime")
	private String endTime;
	@DynamoDBAttribute(attributeName = "duration")
	private String duration;
	@DynamoDBAttribute(attributeName = "status")
	private String status;
	@DynamoDBAttribute(attributeName = "error")
	private String error;

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}