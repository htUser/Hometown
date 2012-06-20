<%@page import="com.platform.hometown.Client.*" %>


<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>	


<%

	String locId= request.getParameter("location_id");
	
	Functions.debug("locId is "+locId);
%>
	
<script type="text/javascript">
	function submitForm(){
		document.forms["mainForm"].submit();
	
	}


</script>
	
	<html>
	<body  onload="submitForm()">
		<form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectClientPlacesController" method="POST">
                <input type="hidden" name="myValue" value="<%=locId%>"/>
                <input type="hidden" name="action" value="getCounties" />
                               </form>	
			
	</body>
	
	</html>