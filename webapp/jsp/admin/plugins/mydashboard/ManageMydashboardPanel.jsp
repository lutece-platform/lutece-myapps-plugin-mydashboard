<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="managemydashboardpanel" scope="session" class="fr.paris.lutece.plugins.mydashboard.web.ManageMydashboardPanelJspBean" />

<% managemydashboardpanel.init( request, managemydashboardpanel.RIGHT_MANAGEMYDASHBOARDPANEL ); %>
<%= managemydashboardpanel.getManageMydashboardPanelHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
