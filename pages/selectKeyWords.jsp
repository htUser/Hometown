<%@page import="com.platform.hometown.Client.*" %>
<%@page import="java.util.ArrayList" %>

<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>

<%
	java.util.HashMap params = (java.util.HashMap)controllerResponse.getData();
	ArrayList<KeyWordsBO> keyWordsArray = (ArrayList<KeyWordsBO>)params.get("keyWords");
	String myAction = (String)params.get("action");
	String cId = (String)params.get("cId");
		
	
%>

<head>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript">

function submitSave(){
		
	//check if only 1 selected
	var numChecked = $(":checked").size();
   
	if(numChecked>1){
		alert("Please select only one Keyword.");
		
	} else if(numChecked==1){
		
		document.forms["mainForm"].elements["action"].value = "select";
		document.forms["mainForm"].submit();
	
	} else {
		alert("Please select a Keyword.");
	}

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
	<tr><td colspan="10" class="td1"><form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectKeyWordsController" method="POST">
        <table border="0">
        
        <tr><td class="td1"><input type="hidden" name="action" value="<%=myAction %>" />
        <input type="hidden" name="myValue" value="<%=cId%>">
        </td></tr>
        <tr><td class="title1" colspan="10">KeyWords</td></tr>
		<tr><td colspan="10"><hr></td></tr>
		
	
	<tr><td align="top" class="td1">
		<table id="keyWords">
				<% 	for(int i=0; i < keyWordsArray.size(); i++ )
					{
						KeyWordsBO k  = (KeyWordsBO)keyWordsArray.get(i); %>
										
					<tr><td class="td1"><input type="checkbox"  name="keyWordId" value="<%= k.getKeyWordId() %>"><%= k.getKeyWordName() %>
					</td></tr>
							
					<% }%>
							
							
		</table>
	
		</td></tr>
	
		
		</table></td></tr>
		
		<tr><td class="td1" align="right"><button class="c1" type="button" onclick="submitSave()">Select KeyWord</button></td></tr>

		</form></td></tr>
		
	</table>

</body>

</html>