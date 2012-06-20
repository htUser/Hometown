<%@page import="com.platform.hometown.Client.*" %>


<%@page import="com.platform.api.*" %>
<%@taglib prefix="c" uri="/c"%>	


<%

	String cId= request.getParameter("campaign_id");
	
	Functions.debug("campaign_id is "+cId);
%>
	
<script type="text/javascript">
	function submitForm(){
		document.forms["mainForm"].submit();
	
	}


</script>
	
	<html>
	<body  onload="submitForm()">
		<form name="mainForm" action="/networking/controller/com/platform/hometown/Client/SelectBuildsController" method="POST">
                <input type="hidden" name="myValue" value="<%=cId%>"/>
                <input type="hidden" name="action" value="create" />
                               </form>	
			
	</body>
	
	</html>