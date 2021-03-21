<!DOCTYPE html>
<html lang="en">
<head>
  <title>EZComputing.org</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body>

<div class="jumbotron text-center">
  <h1>EZComputing.org</h1>
  <p>new way for ground computing</p> 
</div>
  
<div class="container">
  <div class="row">
    
     <div class="col-sm-4">
      <h3>version</h3>
      <p>version: ${buildVersion!}</p>
      <p>build time: ${buildTimestamp!}</p>
      <p>profile: ${(profile)!}</p>
      <p>current Environment: ${(currentEnvironment)!}</p>
    </div>
    
    <div class="col-sm-4">
      <h3>Log</h3>
     	 <p>
      		<a class="btn btn-primary" href="/logs/logfile.log" role="button"> log file</a>
		</p>
      	
      
    </div>
    
     <div class="col-sm-4">
      <h3>version</h3>
		<form action="/topic/create" class="form-inline">
			<button type="submit" class="btn btn-primary">create topic</button>
			<input class="form-control" name="id" value="123"/>
		</form>
    </div>
  </div>
  
</div>

</body>
</html>

