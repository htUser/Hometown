<%@page import="com.platform.hometown.ProcessThirdPartyRequest.*" %>
<%@taglib prefix="c" uri="/c"%>
<%

Map<String,String[]> requestDataMap = request.getParameterMap();

String formType = request.getParameter("form_type");

ThirdPartyRequestProcessor tprp = new ThirdPartyRequestProcessor(formType , requestDataMap);

tprp.processRequest();


%>