<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="statsviewer.ChartGenerator"%>
<%@ page import="statsviewer.DataSet"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%
	int chartHeight = request.getParameter("height") != null ? Integer
			.parseInt(request.getParameter("height")) : 600;
	int chartWidth = request.getParameter("width") != null ? Integer
			.parseInt(request.getParameter("width")) : 800;

	boolean clickable = (request.getParameter("clickable") != null && request
			.getParameter("clickable").equals("false")) ? false : true;

	String containerName = request.getParameter("container");

	String filename = ChartGenerator.generateXYAreaChart(containerName,
			chartHeight, chartWidth, request.getParameter("dayView"),
			clickable, session, new PrintWriter(out));

	String graphURL = request.getContextPath()
			+ "/servlet/DisplayChart?filename=" + filename;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="stylesheet" type="text/css" href="styles.css" />
<title><%=containerName%></title>
</head>
<body>
<center>
<table border=0>
	<tr>
		<td style="text-align: center">
		<h2><%=containerName%></h2>
		<br>
		<br>
		<img src="<%=graphURL%>" width="<%=chartWidth%>"
			height="<%=chartHeight%>" border=0 usemap="#<%= filename %>"></td>
	</tr>
</table>
</center>
</body>
</html>
