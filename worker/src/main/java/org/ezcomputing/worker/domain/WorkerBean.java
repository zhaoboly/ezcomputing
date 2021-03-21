package org.ezcomputing.worker.domain;
/**
 * @author Bo Zhao
 *
 */
public class WorkerBean {
	
	
	public WorkerBean() {
		
	}

	
	
    private String id;
    private String email;

	
    private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
    
}
