<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="mydashboardPortlet" scope="session" class="fr.paris.lutece.plugins.mydashboard.web.portlet.MyDashboardPortletJspBean" />

<%
	mydashboardPortlet.init( request, mydashboardPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect(  mydashboardPortlet.doCreate( request )  );
%>




