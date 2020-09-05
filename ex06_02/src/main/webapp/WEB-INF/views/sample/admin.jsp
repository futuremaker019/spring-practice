<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>/sample/admin page</h1>
	
	<p> principal : <sec:authentication property="principal"/> </p>
	<p> MemberVO : <sec:authentication property="principal.memberVO"/> </p>
	<p> User Name : <sec:authentication property="principal.memberVO.userName"/> </p>
	<p> User ID : <sec:authentication property="principal.memberVO.userid"/> </p>
	<p> 사용자 권한 리스트 : <sec:authentication property="principal.memberVO.authList"/> </p>
	
	<a href="/sample/customLogout">move to Logout page</a>
</body>
</html>