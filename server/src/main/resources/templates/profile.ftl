<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">

<link rel="stylesheet" href="/css/console.css">

<style>
</style>
 

</head>
<body>

<div class="sidenav border-right" >
	
  <a href="/web/app/list">App Store </a>
  	<a href="/web/job/list">JS Jobs</a>
  	<a href="/web/worker/list">Worker Pool</a>
  
   <#if isTester >
  	 <a href="/web/module/list">Module</a>
   </#if>
  
  

	<br>
  <br>
<a href="/doc.html" target="_blank">Documents</a>
  <a href="/web/profile">Profile</a>
  <a href="/logout">Logout</a>
</div>

<div class="main" >
  
  <div class="container">
  <h2 id="title">Profile</h2>
  <p></p>
  
    
    
  <table class="table">
    
    <tbody style="font-size: large;">
        
     	 <tr>
 		  	
	        <td>First Name: ${user.givenName}</td>
	       
	       
   		   </tr>
   		   
         <tr><td>Family Name: ${user.familyName}</td></tr>
         
	        	 <tr><td>Email: ${user.email}</td></tr>
       
     
    </tbody>
  </table>
</div><!-- end of container -->
  
</div> <!-- end of main -->



  
  
  <script type="text/javascript">
	
		
</script>


</body>

</html> 
