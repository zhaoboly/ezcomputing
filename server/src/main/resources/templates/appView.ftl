<!DOCTYPE html>
<html>
<header>
<title>App View</title>

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

#appResponseEditor { 
      height: 200px;    
    }
#appRequestEditor { 
      height: 200px;    
    }
 #appSourceEditor { 
      height: 440px;    
    }
</style>


</header>
<body>



	
<div class="container" >

	<h1> ${app.name}   </h1>
			<a href="/web/app/list">Back to App Store </a>
		
				<p/>
				
				<table class="table shadow rounded" style="background-color: #EEEEEE;font-size: large;">
				
				
				    <tbody>
				         <tr>				      	
				        <td >author: ${owner}     </td>

				      </tr>
				      
				     	  <tr><td>Publish Date: ${app.publishDate?datetime}</td></tr>
				         <tr><td>ID: ${app.id}</td></tr>
				         <tr><td>Description: ${app.description}</td></tr>
				         	
				       
				     
				    </tbody>
				  </table>
				
				<p/>
		<hr>




  <div class="row">
    <div class="col-sm-8">
      <h3>Javascript source</h3>
      <div id="appSourceEditor">${app.source!}</div>
    </div>
    <div class="col-sm-4">
      <h3>Request(JSON format):</h3>
      
       
      <div id="appRequestEditor">${app.request!}</div>
	  <h3>Job Response</h3>
      <div id="appResponseEditor">${app.response!}</div>
    </div>
    
  </div>
  <hr>
</div> <!-- end of container-->



	
<script src="https://zhaoboly.bitbucket.io/static/ace/src-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
<script>

	

	
		//load job source editor
	    var editorResponse = ace.edit("appResponseEditor");
	    editorResponse.setTheme("ace/theme/monokai");
	    editorResponse.session.setMode("ace/mode/javascript");
	    editorResponse.setReadOnly(true); 
	    
	    
		//load job request editor
	    var editorRequest = ace.edit("appRequestEditor");
	    editorRequest.setTheme("ace/theme/monokai");
	    editorRequest.session.setMode("ace/mode/javascript");
	    
		
	
		//load job source editor
	    var editor = ace.edit("appSourceEditor");
	    editor.setTheme("ace/theme/monokai");
	    editor.session.setMode("ace/mode/javascript");
	    
	    editor.setReadOnly(true); 
	    
	    
	    
		
	</script>

</body>
</html>

