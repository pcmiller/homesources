<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	iTunes Management Application  
</h1>

<P>The time on the server is ${serverTime}. </P>
<a href="byDirectories">Music Collection by Directories. </a>
<a href="byItunes">Music Collection recognized by iTunes. </a>
<a href="notInItunes">Music Collection not yet recognized by iTunes. </a>
</body>
</html>
