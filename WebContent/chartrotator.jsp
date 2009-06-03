<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	// Interval in seconds
	int refreshInterval = 20;

	// Which graphs to rotate
	String[] containers = new String[] {
		"ID SA DSL - by priority", 
		"ID SA Voice - by priority", 
		"ID SA eBSA konv - by priority", 
		"ID SA Drift - by priority", 
		"ID SA CM - by priority", 
		"ID SA Billing - by priority", 
		"ID SA Total"
	};

	// Which graph are we showing now?
	int graphIndex = 0; 
	if(session.getAttribute("Graph index") == null) {
		session.setAttribute("Graph index", graphIndex);	
	} else {
		graphIndex = Integer.parseInt(session.getAttribute("Graph index").toString());
		
		// Check if we have reached the end of the list
		if(graphIndex >= containers.length)
			graphIndex = 0; 
	}
	String containerName = containers[graphIndex];
	
	// Update graph index for next refresh
	session.setAttribute("Graph index", ++graphIndex);
	
	String dayView = request.getParameter("dayView");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="refresh" content="<%= refreshInterval %>">
<link rel="stylesheet" type="text/css" href="styles.css" />
<title>Chart rotator</title>
</head>
<body>
	<jsp:include page="timechart.jsp" flush="true">
		<jsp:param value="<%= containerName %>" name="container"/>
		<jsp:param value="<%= dayView %>" name="dayView"/>
	</jsp:include>
	<br><br>
	<center>	
		<div style="font-size: smaller; font-style: italic;">
			Refreshing every <%= refreshInterval %> seconds.
		</div>
	</center>
	
</body>
</html>