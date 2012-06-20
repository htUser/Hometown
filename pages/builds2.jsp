<%@page import="com.platform.hometown.Client.*" %>
<%@page import="java.util.ArrayList" %>

<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>

<%
	java.util.HashMap params = (java.util.HashMap)controllerResponse.getData();
	CampaignBO c= (CampaignBO)params.get("campaign");
	String myAction = (String)params.get("action");
	ArrayList<ListingBO> listings = c.getCurrentListings();
	
	
		
	
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

function checkAll( )
{
   

  
  $(#"please:checkbox").attr('checked','checked');
  
   
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
	<tr><td colspan="10" ><form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectBuildsController" method="POST">
        <table border="0">
        
        <tr><td ><input type="hidden" name="action" value="<%=myAction %>" />
        <input type="hidden" name="myValue" value="<%=c.getCampaignId()%>">
        </td></tr>
        <tr><td class="title1" colspan="10"><%= c.getCampaignName() %></td></tr>
		<tr><td colspan="10"><hr></td></tr>
		
		
		<tr><td align="top" class="td1"><table id="listings">
			
			<tr>
				<% 
				int countListings=0;
				int countPages=0;
				for(int k=0; k < listings.size(); k++ )
				{
					ListingBO listing  = (ListingBO)listings.get(k); 
					countListings++;
					
					%>
					<th class="header1"><%=listing.getCountyName() %> <br> <%=listing.getKeyWordName() %> </th>
					
					
					
					
				<% 	}%></tr>
					
					
					
					<tr><td colspan="<%=countListings%>" class="td1"><hr></td></tr>
					
							
							
					<tr>
					<% for(int k=0; k < listings.size(); k++ ){
						ListingBO listing  = (ListingBO)listings.get(k); 
				
					%>
					
					<td>
						<table>
							
							<tr><td><input type="checkbox"  id="acheckAll"  onclick="checkAll()" >All</td></tr>
							
							<tr><td colspan="2" id="please">
					
					
						
					
							<% 
								ArrayList<PageBO> pagesForK = listing.getPagesAvailable();
					
						
								for(int j=0; j< pagesForK.size(); j++ ){
									PageBO aPage = (PageBO)pagesForK.get(j);
									countPages++; %>
							
										<input    name="<%=k %>page<%=j%>" type="checkbox"  <% if((aPage.getSelected()==true)||(myAction.equals("create")==true)){ %> checked="checked" <% } %> value="<%= aPage.getPlaceId() %>"><%= aPage.getPlaceName() %><br>
										
										
										<% 
								}%>
				
					
					
							<input type="hidden" name="numOfPages<%=k%>" value=<%=countPages %>></td></tr>
						</table>
							
							
					</td><% }
				%></tr>
							
							
		</table>
	
		</td></tr>
	
		<tr><td class="td1"><input type="hidden" name="numOfListings" value=<%=countListings %>></td></tr>
		<tr><td class="td1" colspan="<%=countListings%>" align="right"><button class="c1" type="button" onclick="submitSave()">Create Builds</button></td></tr>
		
		</table>
			
		</form></td></tr>
		
	</table>
		
	
</body>

</html>