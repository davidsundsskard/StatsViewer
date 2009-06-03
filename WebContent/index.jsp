<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="styles.css" />
<title>StatsViewer</title>
</head>
<body>
<h1>StatsViewer</h1>
You can:
<ol>
<li>Start the <a href="chartrotator.jsp">Chart Rotator</a></li>
<li>View any of the following queues:
<ul>
	<li><a href="timechart.jsp?container=ID SA DSL - by priority">ID SA DSL, by priority</a></li>
	<li><a href="timechart.jsp?container=ID SA Voice - by priority">ID SA Voice, by priority</a></li>
	<li><a href="timechart.jsp?container=ID SA eBSA konv - by priority">ID SA eBSA konv - by priority</a></li>
	<li><a href="timechart.jsp?container=ID SA Drift - by priority">ID SA Drift - by priority</a></li>
	<li><a href="timechart.jsp?container=ID SA CM - by priority">ID SA CM - by priority</a></li>
	<li><a href="timechart.jsp?container=ID SA Billing - by priority">ID SA Billing - by priority</a></li>
	<li><a href="timechart.jsp?container=ID SA Total">ID SA Total</a></li>
</ul>
</li>
<li>See an <a href="queueOverview.jsp">overview of all Remedy queues</a></li>
</ol>
</body>
</html>