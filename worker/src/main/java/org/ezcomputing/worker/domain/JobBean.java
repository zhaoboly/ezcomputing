package org.ezcomputing.worker.domain;
/**
 * @author Bo Zhao
 *
 */
public class JobBean {

	public JobBean() {

	}

	private String id;

	private String source;

	private String request;

	private String response;

	
	private String error;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	
}
