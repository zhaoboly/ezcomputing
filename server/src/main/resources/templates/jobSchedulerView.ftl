<!DOCTYPE html>
<html>
<header>
<title>Job View</title>

<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>



<style type="text/css" media="screen">

</style>


</header>
<body>



	
<div class="container" >

	<h1> Configure Job Scheduler </h1>
	<a href="/web/job/list">Back to Job List</a>
	<p/>


 <form id="deleteForm" action="/web/job/scheduler/delete" method="post" style="display:none;">
			 <input type="text"  name="jobId" value="${job.id!}" hidden>
			  <button type="submit" class="btn btn-primary">Delete</button>
		</form>

		<form id="updateForm" action="/web/job/scheduler/submit" method="post">
		  <div class="form-group">
		    <label >Current Job Number: ${job.name}</label>
		  </div>
		  
		  <div class="form-group">
		    <label for="exampleFormControlSelect1">Select Job Scheduler Frequency</label>
		    <select class="form-control" name="type">
		      <option>EveryDay</option>
		      <option disabled>EveryMonth</option>
		      <option disabled>EveryWeek</option>
		     
		    </select>
		  </div>
		  <div class="form-group">
		    <label for="exampleInputPassword1">Hour</label>
		    <input type="number" name="hours" class="form-control"  value="${scheduler.hours}" required>
		    <small  class="form-text text-muted">The range is 1 - 23</small>
		    
		  </div>
		  <div class="form-group">
		    <label for="exampleInputPassword1">Minute</label>
		    <input type="number" name="minutes" class="form-control"  value="${scheduler.minutes}" required >
		    <small  class="form-text text-muted">The range is 0 - 59</small>
		    
		  </div>
		  
		  
		  <input type="text"  name="jobId" value="${job.id!}" hidden>
		  <button type="submit" class="btn btn-primary" hidden>Submit</button>
		  
		 
		  
		</form>
		
		<button onclick="document.getElementById('updateForm').submit();" class="btn btn-primary">Save</button>
		
		<button onclick="document.getElementById('deleteForm').submit();" class="btn btn-primary">Delete</button>
		
		<br>
		<br>
</div> <!-- end of container-->


	
<script>
 	
</script>

</body>
</html>

