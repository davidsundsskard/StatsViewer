<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	int refreshInterval = 300; 

	// Which graphs to show
	String[] containers = new String[] { "ID SA DSL - by priority",
			"ID SA Voice - by priority",
			"ID SA eBSA konv - by priority",
			"ID SA Drift - by priority", "ID SA CM - by priority",
			"ID SA Billing - by priority", "ID SA Total" };

	// Dimensions of each graph
	int height = 300;
	int width = 400;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="refresh" content="<%= refreshInterval %>">
<link rel="stylesheet" type="text/css" href="styles.css" />
<title>Remedy Queue Overview</title>
</head>
<body>
<center>
<table>
	<% for(int i = 0; i < containers.length; i ++) {
		if(i % 2 == 0) {
	%>
		<tr>
	<%  } %>
		<td><jsp:include page="timechart.jsp" flush="true">
			<jsp:param value="<%=containers[i] %>" name="container" />
			<jsp:param value="<%=height %>" name="height" />
			<jsp:param value="<%=width %>" name="width" />
			<jsp:param value="false" name="clickable" />
		</jsp:include></td>
	<%	if(i %2 == 0) { %>
		</td>
	<% }
	}
	%>		
</table>
</center>
</body>
</html>