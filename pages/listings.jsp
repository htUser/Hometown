<%@page import="com.platform.hometown.Client.*" %>
<%@page import="java.util.ArrayList" %>

<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>

<%
	java.util.HashMap params = (java.util.HashMap)controllerResponse.getData();
	CampaignBO c= (CampaignBO)params.get("campaign");
	String myAction = (String)params.get("action");
	ArrayList keyWords = c.getKeyWords();
	keyWords.trimToSize();
	
	
		
	
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
	<tr><td colspan="10" class="td1"><form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectListingsController" method="POST">
        <table border="0">
        
        <tr><td class="td1"><input type="hidden" name="action" value="<%=myAction %>" />
        <input type="hidden" name="myValue" value="<%=c.getCampaignId()%>">
        </td></tr>
        <tr><td class="t1" colspan="10" class="title1"><%= c.getCampaignName() %></td></tr>
		<tr><td colspan="10"><hr></td></tr>
		
	
		</table></td></tr>
		
		
		
		
		<tr><td align="top" class="td1"><table id="keywords">
			
			<tr>
				<% 
				int countKeyWords=0;
				int countCounties=0;
				for(int k=0; k < keyWords.size(); k++ )
				{
					KeyWordsBO key  = (KeyWordsBO)keyWords.get(k); 
					countKeyWords++;
					
					%>
					<th class="header1"><%=key.getKeyWordName() %> </th>
					
					
					
				<% 	}%></tr>
					
					
					
					<tr><td colspan="<%=countKeyWords %>" ><hr></td></tr>
							
							
							
					<tr>
					<% for(int k=0; k < keyWords.size(); k++ ){
						KeyWordsBO key  = (KeyWordsBO)keyWords.get(k); 
				
					%>
					
					<td class="td1">
						<table>
							
							<tr><td class="td1"><input type="checkbox"  id="acheckAll<%=key.getKeyWordId() %>"  checked="checked"  onclick="checkAll(this.id, '<%=key.getKeyWordId() %>' ) " >All</td></tr>
							
							<tr><td colspan="2" id="<%=key.getKeyWordId() %>"  class="td1">
							
					
						
					
							<% 
								ArrayList<Counties> countiesForK = key.getCountiesAvailable();
					
						
								for(int j=0; j< countiesForK.size(); j++ ){
									Counties tempC = (Counties)countiesForK.get(j);
									countCounties++;%>
							
										<input name="<%=k %>county<%=j %>" type="checkbox"  <% if((tempC.getSelected()==true)||(myAction.equals("create")==true)){ %> checked="checked" <% } %> value="<%= tempC.getCountyId() %>"><%= tempC.getCountyName() %><br>
										<%  
								}%>
				
					
					
							<input type="hidden" name="numOfCounties<%=k%>" value=<%=countCounties %>></td></tr>
						</table>
							
							
					</td><% }
				%></tr>
							
							
		</table>
	
		</td></tr>
	
		<tr><td class="td1"><input type="hidden" name="numOfKeyWords" value=<%=countKeyWords %>></td></tr>
		
		</table></td></tr>
		
		<tr><td class="td1" colspan="<%=countKeyWords%>" align="right"><button class="c1" type="button" onclick="submitSave()">Add selected Listings</button></td></tr>
		

		
		
		</form></td></tr>
		
	</table>
		
		
		
		
		
		
		
		
		

		
		
		</form></td></tr>
		
	</table>
			
	
	

</body>

</html>