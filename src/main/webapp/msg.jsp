		<%@page import="com.dao.UserDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.entity.*"%>
<%@page import="java.util.List"%>
<%@page import="com.dao.CategoryDao"%>
<%@page import="com.helper.ConnectionProvider"%>
<%@page import="java.sql.Connection"%>
<%
		Message msg = (Message)session.getAttribute("msg");
		if(msg != null)
		{%>
							
			<div class="alert <%=msg.getCssclass() %>" role="alert">
				<%= msg.getContent() %>
			</div>
								
		<%}
			session.removeAttribute("msg");
	%>