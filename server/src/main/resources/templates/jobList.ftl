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
  <h2 id="title">Javascript Job List</h2>
  <p></p>
  
    
  <form id="createForm"  action="/web/job/create" method="post" style="display:none;">
		<input type="submit"  style="font-weight:bold;" value="Create" ></input>
  </form> 
  <form id="deleteForm" action="/web/job/delete" method="post" style="display:none;">
					<input type="text"  id="deleteFormJobId" name="id" value="test">
					<input type="submit"  value="Delete" ></input>
  </form>
  <form id="runForm" action="/web/job/run-all" method="post" style="display:none;">
						
						<input type="text"  id="runFormJobId" name="id" value="test">
					<input type="submit"  value="Run on Worker" ></input>
	</form>
  
  
  <button onclick="onClickCreateButton()" type="button" class="btn btn-light border">Create</button>
  <button onclick="onClickDeleteButton()" type="button" class="btn btn-light border">Delete</button>
  <button onclick="onClickRunButton()" type="button" class="btn btn-light border ">Run on worker</button>
  <button onclick="onClickPublishButton()" type="button" class="btn btn-light border ">Publish to App Store</button>
  
  
  <p/>
  
  <table class="table">
    <thead style="background-color: #EEEEEE; ">
      <tr>
      	<th><input type="checkbox" id="allJobCheckbox" onclick="onClickAllJobCheckBox()"></th>	
        <th>name</th>
        <th>language</th>
        <th>status</th>
        <th>schedule</th>
        <th>create date</th>
       
       <th>notes</th>
      </tr>
    </thead>
    <tbody>
        
     
        
        <#list jobList as job>
 		  <tr>
 		  	<td><input type="checkbox" name="jobId" value="${job.id}"> </td>
	        <td><a href="/web/job/view/${job.id}">${(job.name)!}</a></td>
	        <td>${job.language}</td>
	        <td>${job.status}</td>
	         <#if job.scheduler ?? >
	         	<td><a href="/web/job/scheduler/view/${job.id}">${job.scheduler.toName()}</a></td>
	         <#else>
	         	<td style="text-align: center;"><a href="/web/job/scheduler/view/${job.id}" class="fas fa-plus-circle " style="color:grey;"></a></td>
	         </#if>
	        
	        <td>${job.createDate?datetime}</td>
	       
	        <td>
	        	<#if job.publishedAppId ?? >
	        		<a href="/web/app/view/${job.publishedAppId}" class="fas fa-globe " style="color:grey;" data-toggle="tooltip" title="This javascript job has been published to App Store." target="_blank"></a>
	        	</#if>
	         
	         	<#if job.fromAppId ?? >
	        		<a href="/web/app/view/${job.fromAppId}" class="fas fa-download " style="color:grey;" data-toggle="tooltip" title="This javascript job is imported from App Store." target="_blank"></a>
	        	</#if>
	        </td>
   		   </tr>
		</#list>
		
     
    </tbody>
  </table>
</div><!-- end of container -->
  
</div> <!-- end of main -->



<!-- The Modal -->
  <div class="modal" id="publishModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          
         <h3>Publish the current Job to APP Store</h3>
         <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <form id="publishForm" action="/web/job/publish" method="post" >
					<div class="form-group">
					   
					    <input type="text" id="publishFormName" name="name" class="form-control"  value="" required>
					    <input type="text" id="publishFormDesc" name="description" class="form-control"  value="" required>
					  </div>
					  
					<input type="text"  id="publishFormJobId" name="jobId" value="test" hidden>
					<input type="submit"  value="changeName" hidden></input>
			</form>
 			
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
            
        	<button type="button" class="btn btn-primary" onclick="document.getElementById('publishForm').submit();">Publish</button>
        </div>
        
      </div>
    </div>
  </div>
  <!-- end of Modal-->

  
  
  <script type="text/javascript">
  
	  $(document).ready(function(){
	  	$('[data-toggle="tooltip"]').tooltip();   
	});
	  
	  	function onClickPublishButton(event) {
			var list = getSelectedJobList();
			if(list){
				if(list.includes(';')){
					alert("Please select only one job.");
					return;
				}else{
					
				    document.getElementById('publishFormJobId').value = list;
				    document.getElementById('publishFormName').value = "";
				    document.getElementById('publishFormDesc').value = "";
				    $("#publishModal").modal();	
				    
				}
			}else{
				alert("Please select one job.");
				return;
			}
			
		}
		
		
		function onClickAllJobCheckBox(){
			var mainCheckbox = document.getElementById('allJobCheckbox');
			var checkboxes = document.getElementsByName('jobId');
		
			
			for (var i=0, n=checkboxes.length;i<n;i++) {
				checkboxes[i].checked = mainCheckbox.checked;
			}
		}
		
		function onClickCreateButton() {
			console.log("ready to create.");
			
			document.getElementById('createForm').submit();
			
		}
		
		function onClickDeleteButton(event) {
			var list = getSelectedJobList();
			if(list){
				if(list.includes(';')){
					alert("Please select only one job.");
					return;
				}else{
					
				    document.getElementById('deleteFormJobId').value = list;
				    document.getElementById('deleteForm').submit();
				    
				}
			}else{
				alert("Please select one job.");
				return;
			}
			
		}
		
		
		
		function onClickRunButton(event) {
			var list = getSelectedJobList();
			if(list){
				document.getElementById('runFormJobId').value = list;
				document.getElementById('runForm').submit();
			}else{
				alert("Please select one job to delete.");
				return;
			}
			
			
		}
		
		
		
		function getSelectedJobList(){
			var checkboxes = document.getElementsByName('jobId');
			var vals = "";
			for (var i=0, n=checkboxes.length;i<n;i++) 
			{
			    if (checkboxes[i].checked) 
			    {
			        vals += ";"+checkboxes[i].value;
			    }
			}
			if (vals) vals = vals.substring(1);
		
			return vals;
		}
		
		
		
		
</script>


</body>

</html> 
