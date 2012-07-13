<%@page import="com.platform.hometown.Client.*" %>
<%@page import="java.util.ArrayList" %>

<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>

<%
	java.util.HashMap params = (java.util.HashMap)controllerResponse.getData();
	
	String myAction = (String)params.get("action");
	
	
	
		
	
%>

<head>

<script type="text/javascript">

var llCounty;

function submitSave(){
	//alert("We are pretend saving");
	document.forms["mainForm"].elements["action"].value = "save";
	document.forms["mainForm"].submit();
}

</script>

<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">

function checkAll(me, boxesToCheck )
{
   

   $( "#" + boxesToCheck + " :checkbox").attr('checked', $('#' + me).is(':checked'));
  
   
}
</script>

<style type="text/css">

.body1 {
        background-color: #DCDEEE;
        font-family: Arial, Helvetica, sans-serif;
        font-size: 12px;
        line-height: 24px;
        color: #336699;
}

.td1 {
        font-family: Arial, Helvetica, sans-serif;
        font-size: 10px;
        color: #333333;
        vertical-align:text-top;
        
}

.a1 {
        color: #3366CC;
        text-decoration: none;
}

.form1 {
        background-color: #CCCC99;
        padding: 6px;
}

.title1 {
        font-family: Arial, Helvetica, sans-serif;
        font-size: 16px;
        padding: 4px;
        
}

.header1 {
        font-family: Arial, Helvetica, sans-serif;
        font-size: 12px;
        background-color: #006666;
        color: #DCDCDC;
        font-weight: bold;
}

 </style>

</head> 
<html>


<body class="body1">

	<table border = "0">
	<tr><td colspan="10" class="td1">Sorry - Not working just yet.</td></tr>
		
	</table>
			
	
	

</body>

</html>