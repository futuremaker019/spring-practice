<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>Custom Login Page</h1>
	<h2><c:out value="${error }"/></h2>
	<h2><c:out value="${logout }"/></h2>

	<form method="post" action="/login">
		<div>
			<input type="text" name="username" value='admin90'>
		</div>
		<div>
			<input type="password" name="password" value='pw90'>
		</div>
		<div>
			<input type="checkbox" name="remember-me"> Remember me
		</div>
		<div>
			<input type="submit">
		</div>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
	</form>
	<script>
		var paramName = '<c:out value="${_csrf.parameterName}"/>'
		var error = '<c:out value="${error }"/>'
		
		console.log("_csrf.paramName : " + paramName);
		// error라는 파라미터가 전달되는듯 하다.
		console.log("error : " + error);
	</script>
</body>

</html>