<jsp:useBean id="managemydashboard" scope="session" class="fr.paris.lutece.plugins.mydashboard.web.MyDashboardJspBean" />
<% String strContent = managemydashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
