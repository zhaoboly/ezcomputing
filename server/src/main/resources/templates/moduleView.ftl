<!DOCTYPE html>
<html>
<header>
<title>Module View</title>

<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

 

<script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.13.6/js/lib/beautify.js"></script>
<script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.13.6/js/lib/beautify-css.js"></script>
<script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.13.6/js/lib/beautify-html.js"></script>



<style type="text/css" media="screen">

   
</style>


</header>
<body>



	
<div class="container" >

	<h1> Javascript Module View </h1>
		<a href="/web/module/list">Back to Module List</a>
				<p/>
		<table class="table shadow rounded" style="background-color: #EEEEEE;font-size: large;">
				
				
				    <tbody>
				       <tr>				      	
				        <td >namespace:  ${module.namespace}     
				        </td>
				      </tr>
				     	 
				     <tr><td>version: ${module.version}</td></tr>
				      	 
				         <tr><td>status: ${module.status}</td></tr>
				         
				         <#if module.status == 'failed'>
				          <tr><td class="text-danger">error: ${module.error!}</td></tr>
					     </#if> 	
				       
				     
				    </tbody>
		</table>		
				
				
			
				
		<form id="saveForm" action="/web/module/save" method="post"  >
			
			
			<input type="submit"  value="Save" hidden>
			
			<textarea id="source" name="source" rows="20" cols="150"  style="display:none;">${module.source!}</textarea>
	
			<input type="text"  name="namespace"
								value="${module.namespace!}" hidden>
			<input type="text"  name="version"
								value="${module.version!}" hidden>
			
		</form>
		
		<form id="runForm" action="/web/module/run" method="post" style="display:none;">
			<input type="text"  name="namespace"
								value="${module.namespace!}" hidden>
			<input type="text"  name="version"
								value="${module.version!}" hidden>
			<input type="submit"  value="run" hidden>					
		</form>
		
	<div id="alertSuccess" class="alert alert-success" style="display:none;">
	  good
	</div>
	<div id="alertDanger" class="alert alert-danger" style="display:none;">
	  error
	</div>
	
	
	<button onclick="document.getElementById('saveForm').submit();" type="button" class="btn btn-light border ">Save</button>
 	<button onclick="document.getElementById('runForm').submit();" type="button" class="btn btn-light border">Run</button>
  	
	
		<p/>
		<hr>




  <div class="row">
    <div class="col-sm-12">
      <h3>Javascript source</h3>
      <div id="sourceEditor" style="height: 440px;  "></div>
    </div>
    
    
  </div><!-- end of row -->
  
  
  <br>
  <br>
 </div> <!-- end of container-->


	
<script src="https://zhaoboly.bitbucket.io/static/ace/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
<script>



	
		
		//load job source editor
	    var editor = ace.edit("sourceEditor");
	    editor.setTheme("ace/theme/monokai");
	    editor.session.setMode("ace/mode/javascript");
	    //editor.session.setOptions({ tabSize: 2, useSoftTabs: true });
	    //editor.setOption("showInvisibles", true)
	    
		var textarea = $('textarea[id="source"]');
		
		//console.log("textarea:"+ textarea.val());
		editor.getSession().setValue(textarea.val());
		editor.getSession().on('change', function(){
		  textarea.val(editor.getSession().getValue());
		});
		
		
		
		
	</script>

</body>
</html>

