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
  <h2 id="title">Javascript Module List</h2>
  <p></p>
  
    
  
  <form id="deleteForm" action="/web/module/delete" method="post" style="display:none;">
					<input type="text"  id="deleteFormNamespace" name="namespace" value="test">
					<input type="text"  id="deleteFormVersion" name="version" value="test">
					<input type="submit"  value="Delete" ></input>
  </form>
 
  
  <button onclick="onClickCreateButton()" type="button" class="btn btn-light border">Create</button>
  
 <p/>
  
  <table class="table">
    <thead style="background-color: #EEEEEE; ">
      <tr>
      	
        <th>namespace</th>
        <th></th>	
        <th>version</th>
        <th>status</th>
        <th>update date</th>
       
      </tr>
    </thead>
    <tbody>
        
     
        
        <#list list as module>
 		  <tr>
 		  	
	        <td><a href="/web/module/view/${module.namespace}/${module.version}">${(module.namespace)!}</a></td>
	        
	        <td> <div onclick="onClickDeleteButton('${module.namespace}', '${module.version}')" class="fas fa-trash ml-1" style="color:grey;" onmouseover="this.style.color='black';" onmouseout="this.style.color='grey';"></div></td>

	        <td>${module.version}</td>
	        <td>${module.status!}</td>
	         
	        
	        <td>${module.updateDate?datetime}</td>
	        <td>
	        	
	        </td>
   		   </tr>
		</#list>
		
     
    </tbody>
  </table>
</div><!-- end of container -->
  
</div> <!-- end of main -->




<!-- The Modal -->
  <div class="modal" id="createModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          
         <h3>Create new Modal</h3>
         <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <form id="createForm" action="/web/module/create" method="post" >
					<div class="form-group">
					   
					    <input type="text" name="namespace" class="form-control"  value="" required>
					    
					  </div>
					  
					
					<input type="submit"  value="create" hidden></input>
			</form>
 			
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
            <button type="button" class="btn btn-primary" onclick="document.getElementById('createForm').submit();">Check Available</button>
        
        	<button type="button" class="btn btn-primary" onclick="document.getElementById('createForm').submit();">Create</button>
        
        </div>
        
      </div>
    </div>
  </div>
  <!-- end of Modal-->


  
  
  <script type="text/javascript">
  
	  function onClickCreateButton(){
			$("#createModal").modal();	
		}
		
		function onClickAllJobCheckBox(){
			var mainCheckbox = document.getElementById('allJobCheckbox');
			var checkboxes = document.getElementsByName('jobId');
		
			
			for (var i=0, n=checkboxes.length;i<n;i++) {
				checkboxes[i].checked = mainCheckbox.checked;
			}
		}
		
		
		
		function onClickDeleteButton(namespace, version) {
			 document.getElementById('deleteFormNamespace').value = namespace;
			 document.getElementById('deleteFormVersion').value = version;
			 document.getElementById('deleteForm').submit();
		}
		
		
		
		
</script>


</body>

</html> 
