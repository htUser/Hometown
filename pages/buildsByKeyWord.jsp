<%@page import="com.platform.hometown.Client.*" %>
<%@page import="java.util.ArrayList" %>

<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>

<%
	java.util.HashMap params = (java.util.HashMap)controllerResponse.getData();
	CampaignBO c= (CampaignBO)params.get("campaign");
	String myAction = (String)params.get("action");
	String keyWord = (String)params.get("keyword");
	ArrayList<Counties> counties = c.getClientLocation().getCounties();
	KeyWordsBO theKeyWordBO = c.getSelectedKeyWord();
	
	
		
	
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
	<tr><td colspan="10" class="td1"><form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectBuildByKeyWordController" method="POST">
        <table border="0">
        
        <tr><td class="td1"><input type="hidden" name="action" value="<%=myAction %>" />
        <input type="hidden" name="myValue" value="<%=c.getCampaignId()%>">
        <input type="hidden" name="keyWord" value="<%=keyWord%>">
        
        </td></tr>
        <tr><td class="title1" colspan="10"><%= c.getCampaignName() %></td></tr>
		<tr><td colspan="10"><hr></td></tr>
		
	
	<tr><td align="top" class="td1"><table id="listings">
			
			<tr>
				<% 
				int countListings=0;
				int countPages=0;
				
				for(int k=0; k < counties.size(); k++ )
				{
					Counties aCounty  = (Counties)counties.get(k); 
					countListings++;
					
					%>
					<th class="header1"><%=aCounty.getCountyName() %> <br> <%=theKeyWordBO.getKeyWordName() %> </th>
					
					
					
					
				<% 	}%></tr>
					
					
					
					
					<tr><td colspan="<%=countListings%>" class="td1"><hr></td></tr>
					
					
							
							
					<tr>
					<% for(int k=0; k < counties.size(); k++ ){
						Counties county  = (Counties)counties.get(k); 
				
					%>
					
					<td class="td1">
						<table>
							<tr><td class="td1"><input  type="checkbox"  checked="checked" id="acheckAll<%=county.getCountyId() %>"  onclick="checkAll(this.id, '<%=county.getCountyId() %>' ) " >All</td></tr>
							
							<tr><td colspan="2" id="<%=county.getCountyId() %>" class="td1">
					
						
					
							<% 
							ArrayList<PageBO> pagesForK = county.getPagesAvailable();
							
							
							for(int j=0; j< pagesForK.size(); j++ ){
								PageBO aPage = (PageBO)pagesForK.get(j);
								countPages++; %>
							
										<input    name="<%=k %>page<%=j%>" type="checkbox"  <% if((aPage.getSelected()==true)||(myAction.equals("fill")==true)){ %> checked="checked" <% } %> value="<%= aPage.getPlaceId() %>"><%= aPage.getPlaceName() %><br>
										
										
										<%  
								}%>
				
					
					
							<input type="hidden" name="numOfPages<%=k%>" value=<%=countPages %>></td></tr>
						</table>
							
							
					</td><% }
				%></tr>
							
							
		</table>
	
		</td></tr>
	
		<tr><td class="td1"><input type="hidden" name="numOfListings" value=<%=countListings %>></td></tr>
		
		</table></td></tr>
		
		<tr><td class="td1" colspan="<%=countListings%>" align="right"><button class="c1" type="button" onclick="submitSave()">Create Builds</button></td></tr>
		

		
		
		</form></td></tr>
		
	</table>
			
	
	

</body>

</html>