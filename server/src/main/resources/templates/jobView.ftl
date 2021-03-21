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

 <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">


<script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.13.6/js/lib/beautify.js"></script>
<script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.13.6/js/lib/beautify-css.js"></script>
<script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.13.6/js/lib/beautify-html.js"></script>



<style type="text/css" media="screen">

#jobResponseEditor { 
      height: 200px;    
    }
	#jobRequestEditor { 
      height: 200px;    
    }
    #jobSourceEditor { 
      height: 440px;    
    }
</style>


</header>
<body>



	
<div class="container" >

	<h1> Javascript Job View </h1>
		<a href="/web/job/list">Back to Job List</a>
				<p/>
				
				<table class="table shadow rounded" style="background-color: #EEEEEE;font-size: large;">
				
				
				    <tbody>
				         <tr>				      	
				        <td >Name:  ${job.name}     <div onclick="onClickEditNameButton()" class="fas fa-edit ml-1" style="color:grey;" onmouseover="this.style.color='black';" onmouseout="this.style.color='grey';"></div></td>

				      </tr>
				     	 
				         <tr><td>Status: ${job.status}</td></tr>
				         <#if job.status == 'failed'>
				          <tr><td class="text-danger">error: ${job.error!}</td></tr>
					     </#if> 
					        	
				       
				     
				    </tbody>
				  </table>
				
				<form id="validateForm" action="/web/job/validate" method="post" style="display:none;">
					
					<input type="text"  name="id"
						value="${job.id!}" hidden>
					<input type="submit"  value="Validate" ></input>
				</form>
	
				
				
				
				<form id="runLocalForm" action="/web/job/runLocal" method="post" style="display:none;">
						
						<input type="text"  name="id"
							value="${job.id!}" hidden>
					<input type="submit"  value="Run on Local" ></input>
				  </form>
				  
				<form id="runForm" action="/web/job/run" method="post" style="display:none;">
						
						<input type="text"  name="id"
							value="${job.id!}" hidden>
					<input type="submit"  value="Run on Worker" ></input>
				  </form>
				
				
		<form id="saveForm" action="/web/job/save" method="post" >
			
			
			<input type="submit"  value="Save" style="display:none;">
			<hr>
			
			<textarea id="jobRequest" name="request" rows="10" cols="150">${job.request!}</textarea>
			
			<textarea id="jobSource" name="source" rows="20" cols="150">${job.source!}</textarea>
	
			<input type="text"  name="id"
								value="${job.id!}" hidden>
			
		</form>
		
	<div id="alertSuccess" class="alert alert-success" style="display:none;">
	  good
	</div>
	<div id="alertDanger" class="alert alert-danger" style="display:none;">
	  error
	</div>
	
	
	<button onclick="document.getElementById('saveForm').submit();" type="button" class="btn btn-light border ">Save</button>
 
	 <button onclick="validate();" type="button" class="btn btn-light border">Validate</button>
  	<button onclick="document.getElementById('runLocalForm').submit();" type="button" class="btn btn-light border">Run</button>
  	<button onclick="document.getElementById('runForm').submit();" type="button" class="btn btn-light border ">Run on worker</button>
 
		<p/>
		<hr>




  <div class="row">
    <div class="col-sm-8">
      <h3>Javascript source</h3>
      <div id="jobSourceEditor"></div>
    </div>
    <div class="col-sm-4">
      <h3>Request(JSON format):</h3>
      
      <button id="jobRequestFormatBtn" type="button" class="btn btn-light btn-sm">Format</button>
      
      <div id="jobRequestEditor"></div>
	  <h3>Job Response</h3>
      <div id="jobResponseEditor">${job.response!}</div>
    </div>
    
  </div>
  <hr>
  <div class="row">
  	<h3>Job Execution Log</h3>
    			<table class="table">
				  <thead style="background-color: #F0F0F0;">
				    <tr>
				      
				      <th scope="col" style="width: 250px;">Worker Name</th>
				      <th scope="col" style="width: 200px;">finish time</th>
				      <th scope="col">duration</th>
				      <th scope="col">status</th>
				      <th scope="col">error</th>
				    </tr>
				  </thead>
				  <tbody>
				 <#list job.jobLogList as log>
				 <tr>
				 	<td>${log.workerName!}</td>
				 	<td>${log.endTime}</td>
				 	<td>${log.duration} ms</td>
				 	<td>${log.status!}</td>
				 	<td>${log.error!}</td>
				 </tr>
				 </#list>
				 
				   
				  </tbody>
				</table>

  </div>
</div> <!-- end of container-->


<!-- The Modal -->
  <div class="modal" id="renameModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          
         <h3>Change Job Name</h3>
         <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          <form id="renameForm" action="/web/job/changeName" method="post" >
					<div class="form-group">
					   
					    <input type="text" name="name" class="form-control"  value="${job.name}" required>
					    
					  </div>
					  
					<input type="text"  name="id" value="${job.id}" hidden>
					<input type="submit"  value="changeName" hidden></input>
			</form>
 			
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
            
        	<button type="button" class="btn btn-primary" onclick="document.getElementById('renameForm').submit();">Rename</button>
        </div>
        
      </div>
    </div>
  </div>
  <!-- end of Modal-->


	
<script src="https://zhaoboly.bitbucket.io/static/ace/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
<script>


	function onClickEditNameButton(){
		$("#renameModal").modal();	
	}


	 function validate() {
		var http = new XMLHttpRequest();
		var url = '/web/job/validate';
		var params = 'id=${job.id}'  ;
		http.open('POST', url, true);
		
		//Send the proper header information along with the request
		http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		
		http.onreadystatechange = function() {
		    if(http.readyState == 4 ) {
		    	if(http.status == 200){
		    		document.getElementById('alertSuccess').style.display='block';
		        	document.getElementById('alertSuccess').innerHTML = "Validation result: "+ http.responseText;
		        	
		        	document.getElementById('alertDanger').style.display='none';
		        }else{
		        	document.getElementById('alertDanger').style.display='block';
		        	document.getElementById('alertDanger').innerHTML = "Validation result: "+ http.responseText;
		        	
		        	document.getElementById('alertSuccess').style.display='none';
		        }
		    }
		}
		http.send(params);
	}
	
	

	
		//load job source editor
	    var editorResponse = ace.edit("jobResponseEditor");
	    editorResponse.setTheme("ace/theme/monokai");
	    editorResponse.session.setMode("ace/mode/javascript");
	    editorResponse.setReadOnly(true); 
	    
	    
		//load job request editor
	    var editorRequest = ace.edit("jobRequestEditor");
	    editorRequest.setTheme("ace/theme/monokai");
	    editorRequest.session.setMode("ace/mode/javascript");
	    
		var textareaRequest = $('textarea[id="jobRequest"]').hide();
		//console.log("textarea:"+ textarea.val());
		editorRequest.getSession().setValue(textareaRequest.val());
		editorRequest.getSession().on('change', function(){
		  textareaRequest.val(editorRequest.getSession().getValue());
		});
	
		//load job source editor
	    var editor = ace.edit("jobSourceEditor");
	    editor.setTheme("ace/theme/monokai");
	    editor.session.setMode("ace/mode/javascript");
	    //editor.session.setOptions({ tabSize: 2, useSoftTabs: true });
	    //editor.setOption("showInvisibles", true)
	    if('fromApp' === '${job.type}'){
	    	editor.setReadOnly(true); 
	    }
	    
	    
		var textarea = $('textarea[id="jobSource"]').hide();
		
		//console.log("textarea:"+ textarea.val());
		editor.getSession().setValue(textarea.val());
		editor.getSession().on('change', function(){
		  textarea.val(editor.getSession().getValue());
		});
		
		
		const jobRequestFormatBtn = document.getElementById('jobRequestFormatBtn');
		jobRequestFormatBtn.addEventListener('click', function() {		
			let formatString = js_beautify(editorRequest.getValue());			
		  	editorRequest.setValue(formatString);
		  	
		});
		
	</script>

</body>
</html>

