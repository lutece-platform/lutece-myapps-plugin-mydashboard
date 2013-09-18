<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="mydashboardPortlet" scope="session" class="fr.paris.lutece.plugins.mydashboard.web.portlet.MyDashboardPortletJspBean" />

<% mydashboardPortlet.init( request, mydashboardPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= mydashboardPortlet.getCreate( request ) %>

