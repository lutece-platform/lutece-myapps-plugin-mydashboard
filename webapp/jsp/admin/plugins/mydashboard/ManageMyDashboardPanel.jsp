<jsp:useBean id="managemydashboardpanel" scope="session" class="fr.paris.lutece.plugins.mydashboard.web.ManageMydashboardPanelJspBean" />
<% String strContent = managemydashboardpanel.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
