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

<div class="main">
  <div class="container">
  <h2>Worker Pool</h2>
  <p></p>
  
  <form id="createForm" action="/web/worker/create" method="post" style="display:none;">
		<input type="submit"  value="Create" ></input>
 </form> 
				
<form id="deleteForm" action="/web/worker/delete" method="post" style="display:none;">
					
		<input type="text"  id="deleteFormWorkerId" name="id" value="test" >
		<input type="submit"  value="Delete" ></input>
</form>
				
				
          
	<#if hasPrivateWorker>
   	 	<button onclick="onClickDeleteButton()" type="button" class="btn btn-light border">Delete</button>
   		<button onclick="onClickRenameButton()" class="btn btn-light border" >Rename</button></button>
   	</#if>
   
    <p/>
  
  <table class="table" >
    <thead style="background-color: #EEEEEE">
      <tr>
     	<th></th>	
        <th>name</th>
        <th></th>
        <th>verified</th>
        <th>Owner</th>
        <th>status</th>
        <th>Last Update time</th>
        <th>Start time</th>
      </tr>
    </thead>
    <tbody>
        
     
        
        <#list workerList as worker>
 		  <tr>
 		   	<td><input type="checkbox" name="workerId" value="${worker.id}"> </td>
	       
	        <td >${worker.name}    </td>
	         <td><div  class="fas fa-info-circle  ml-1" style="color:grey;" data-toggle="tooltip" data-placement="top" data-html="true" title="${worker.systemInfo!}"></div></td>
	       
	        <#if worker.verified  >
	        	<td>verified</td>
	       	<#else>
	       		<td><a class="text-danger" onclick="onClickVerifyButton('${worker.id}');">not verified</a></td>
	        </#if>
	       
	        <td>${worker.ownerName!}</td>
	        <#if worker.workerStatus = 'online' >
	        	<td class="text-success">${worker.workerStatus}</td>
	        <#else>
	        	<td >${worker.workerStatus}</td>
	        </#if>
	       
	        <td>${worker.updateDate?datetime}</td>
	        <td>${worker.createDate?datetime}</td>
		</#list>
     
      <#list publicWorkerList as worker>
 		  <tr>
 		   	<td></td>
	       
	        <td >${worker.name}    </td>
	         <td><div  class="fas fa-info-circle  ml-1" style="color:grey;" data-toggle="tooltip" data-placement="top" data-html="true" title="${worker.systemInfo!}"></div></td>
	       
	        
	        <td>verified</td>
	       	
	       
	        <td>${worker.ownerName!}</td>
	        <#if worker.workerStatus = 'online' >
	        	<td class="text-success">${worker.workerStatus}</td>
	        <#else>
	        	<td >${worker.workerStatus}</td>
	        </#if>
	       
	        <td>${worker.updateDate?datetime}</td>
	         <td>${worker.createDate?datetime}</td>
		</#list>
     
    </tbody>
  </table>
</div> <!-- end of container-->
  
</div><!-- end of main -->
   
   
 

<!-- The Modal -->
  <div class="modal" id="renameModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Rename Worker</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <form id="renameForm" action="/web/worker/changeName" method="post" >
					
					<input type="text"  name="id" id="renameFormId"
						value="test" hidden>
					<input type="text"  name="name" id="renameFormName"
						value="test">
					<input type="submit"  value="changeName" hidden></input>
				</form>
 
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        	<button type="button" class="btn btn-primary" onclick="document.getElementById('renameForm').submit();">Rename</button>
        </div>
        
      </div>
    </div>
  </div>
  <!-- end of Modal-->



<!-- The Modal -->
  <div class="modal" id="verifyModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Verify Worker</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
        	<p>I confirm this worker is my worker. I will allow EZComputing.org send Javascript codes to be executed in this worker.<p>
          <form id="verifyForm" action="/web/worker/verify" method="post" >
					
					<input type="text"  name="id" id="verifyFormId"
						value="test" hidden>
					
					<input type="submit"  value="Confirm" hidden></input>
				</form>
 
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        	<button type="button" class="btn btn-primary" onclick="document.getElementById('verifyForm').submit();">Confirm</button>
        </div>
        
      </div>
    </div>
  </div>
  <!-- end of Modal-->

   
 <script type="text/javascript">
 
		 $(document).ready(function(){
		  $('[data-toggle="tooltip"]').tooltip();   
		});
		
		function onClickAllCheckBox(){
			var mainCheckbox = document.getElementById('allCheckbox');
			var checkboxes = document.getElementsByName('workerId');
			
			for (var i=0, n=checkboxes.length;i<n;i++) {
				checkboxes[i].checked = mainCheckbox.checked;
			}
		}
		
		function onClickCreateButton() {
			document.getElementById('createForm').submit();	
		}
		
		function onClickVerifyButton(workerId){
		    document.getElementById('verifyFormId').value= workerId;
			$("#verifyModal").modal();	
		}
		
		function onClickRenameButton() {
			
			var workerListJson = ${ workerListJson};
			var list = getSelectedWorkerList();
			
			if(list){
				if(list.includes(';')){
					alert("Please select only one worker to rename.");
					return;
				}else{
					console.log('select workerid1:' + list);
					var workerId = list;
				    var name;
					for(var i = 0; i < workerListJson.length; i++) {
						//console.log('name'+ workerListJson[i].name);
						if(workerId === workerListJson[i].id){
							name= workerListJson[i].name;
							document.getElementById('renameFormName').value= name;
							document.getElementById('renameFormId').value= workerId;
							console.log('select name:' + name);
						}
					    
					}
					$("#renameModal").modal();	
				    
				}
			}else{
				alert("Please select one worker to rename.");
				return;
			}
			
			
		}
		
		function onClickDeleteButton(event) {
			var list = getSelectedWorkerList();
			if(list){
				if(list.includes(';')){
					alert("Please select only one worker to delete.");
					return;
				}else{
					
				    document.getElementById('deleteFormWorkerId').value = list;
				    document.getElementById('deleteForm').submit();
				    
				}
			}else{
				alert("Please select one worker to delete.");
				return;
			}
			
		}
		
		
		
		
		function getSelectedWorkerList(){
			var checkboxes = document.getElementsByName('workerId');
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



