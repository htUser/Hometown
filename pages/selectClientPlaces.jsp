<%@page import="com.platform.hometown.Client.*" %>
<%@page import="java.util.ArrayList" %>

<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>

<%
	java.util.HashMap params = (java.util.HashMap)controllerResponse.getData();
	ClientLocation cl= (ClientLocation)params.get("clientLocation");
	Client c = cl.getClient();
	String myAction = (String)params.get("action");
	ArrayList counties = cl.getCounties();
	counties.trimToSize();
	String error = (String)params.get("errorMsg");
	String LLerror = (String)params.get("LLerrorMsg");
	
	
		
	
%>

<head>

<script type="text/javascript">

var llCounty;

function submitSelectCounties()
{


document.forms["mainForm"].elements["action"].value  = "filterByCounty";
document.forms["mainForm"].submit();
}


function submitSelectMiles(){
	
	
	document.forms["mainForm"].elements["action"].value = "filterByMiles";
	document.forms["mainForm"].submit();
}

function submitSelectLL(county){

	//alert(county);
	
	document.forms["mainForm"].elements["action"].value = "filterByLL";
	document.forms["mainForm"].elements["llCounty"].value = county;
	document.forms["mainForm"].submit();
}

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
.c2 {
        color: red;
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
	<tr><td colspan="10" class="td1"><form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectClientPlacesController" method="POST">
        <table border="0">
        
        <tr><td class="td1"><input type="hidden" name="action" value="<%=myAction %>" />
        <input type="hidden" name="myValue" value="<%=cl.getLocationId() %>">
        </td></tr>
        <tr><td class="title1" colspan="10"><%= c.getName() %></td></tr>
		<tr><td class="title1" colspan="10"><%= cl.getAddress() %></td></tr>
		<tr><td colspan="10"><hr></td></tr>
		
	
	
		
		
		
		
		
		<tr>
			<td class="c1">Preselect places served from this location based on a distance:</td></tr>
			<% if((error!=null)&&(error.length()!=0)){ %>
			<tr> <td class="c2" ><%=error %></td></tr>
			<%} %>
		<tr>	<td class="c1"><button type="button" onclick="submitSelectMiles()">Filter By Distance</button> <input class="c1" size="4" type="text" name="miles" value="<% if((String)params.get("miles")!=null){ %><%=(String)params.get("miles") %><%}%>"> Miles 
			<br><br></td></tr>
			
			
			
			
		
					
		</table>
		
		
		
		
	<tr><td align="top" class="td1"><table id="places">
			<%if((LLerror!=null)&&(LLerror.length()!=0)){ %>
				<tr><td class="c2"><%=LLerror %>  </td></tr>
			<%} %>
			<tr>
				<% 
				int countPlaces=0;
				int count=0;
				for(int k=0; k < counties.size(); k++ )
				{
					Counties c2 = (Counties)counties.get(k); 
					count++;
					
					
						
					
					
					%>
					<th class="header1"><%=c2.getStateAB() %> - <%= c2.getCountyName() %></th>
					
					
					
					<% }
					
					%></tr>
					
					
					
					<tr><td colspan="<%=count%>" class="td1"><hr></td></tr>
					
							
							
					<tr>
					<% for(int k=0; k < counties.size(); k++ ){
						Counties c2 = (Counties)counties.get(k); 
				
					%>
					
					<td class="td1">
						<table>
							<tr><td colspan="2" class="td1"><button name="llButton<%=k %>"  value="<%=k %>" id="countyButton" type="button" onclick="submitSelectLL(<%=k%>)">Filter By Lat/Long</button></td></tr>
							
							<tr><td class="td1"><select name="latOperator<%=k %>" >
										<option id="countyItem" value="lessThan">&#8804;</option>
										<option id="countyItem" value="greaterThan">&#8805;</option>
									</select>
								</td>
								<td class="td1"><input id="countyItem" size="4" type="text" name="filterLat<%=k %>" value="<% if((String)params.get("filterLat"+k)!=null){ %><%=(String)params.get("filterLat"+k) %><%}%>"> Latitude </td></tr>
							
							
							<tr>
								<td class="td1"><select name="longOperator<%=k %>">
									<option id="countyItem" value="lessThan">&#8804;</option>
									<option id="countyItem" value="greaterThan">&#8805;</option>
								</select>
								</td>
								<td class="td1"><input id="countyItem" size="4" type="text" name="filterLong<%=k %>" value="<% if((String)params.get("filterLong"+k)!=null){ %><%=(String)params.get("filterLong"+k) %><%}%>"> Longitude</td></tr>
							
							
							
							<tr><td colspan="2" class="td1"><hr class="line1"></td></tr>
							<tr><td><input type="checkbox"  checked="checked"  id="acheckAll<%=c2.getCountyId() %>"  onclick="checkAll(this.id, '<%=c2.getCountyId() %>' ) " >All</td></tr>
							
							<tr><td colspan="2" id="<%=c2.getCountyId() %>" class="td1">
					
						
					
							<% 
								ArrayList<Places> placesForC = c2.getPlacesAvailable();
					
								countPlaces=0;
								for(int j=0; j< placesForC.size(); j++ ){
									Places tempP = (Places)placesForC.get(j);
									countPlaces++;%>
							
										<input    name="<%=k %>place<%=j%>" type="checkbox"  <% if((tempP.getSelected()==true)||(myAction.equals("getCounties")==true)){ %> checked="checked" <% } %> value="<%= tempP.getPlaceId() %>"><%= tempP.getPlaceName() %><br>
										
										
										<%  
								}%>
				
					
					
							<input type="hidden" name="numOfPlaces<%=k%>" value=<%=countPlaces %>></td></tr>
						</table>
							
							
					</td><% }
				%></tr>
							
							
		</table>
	
		</td></tr>
	
		<tr><td><input type="hidden" name="numOfCounties" value=<%=count %>></td></tr>
		<tr><td><input type="hidden" name="llCounty"></td></tr>
		</table></td></tr>
		
		<tr><td class="c1" colspan="<%=count%>" align="right"><button class="c1" type="button" onclick="submitSave()">Save Selected Places To This Location</button></td></tr>
		

		
		
		</form></td></tr>
		
	</table>
			
	
	

</body>

</html>