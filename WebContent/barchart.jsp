<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="statsviewer.ChartGenerator"%>
<%@ page import="statsviewer.DataSet"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.util.Date"%>
<%
	// Get the date for which we're generating the bar chart
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	String sDate = request.getParameter("date");
	if (sDate == null)
		sDate = "All";
	Date dDate = null;
	try {
		dDate = sdf.parse(sDate);
	} catch (ParseException e) {
		//  Leave at null
	}
	
	String containerName = request.getParameter("container");
	
	String filename = ChartGenerator.generateBarChart(
			containerName, dDate, session, new PrintWriter(out));
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
		<b>Date [<%=dDate == null ? "All dates" : sdf.format(dDate)%>]<br>
		</b> <br>

		<img src="<%=graphURL%>" width=500 height=300 border=0
			usemap="#<%= filename %>"></td>
	</tr>
</table>
</center>
</body>
</html>

