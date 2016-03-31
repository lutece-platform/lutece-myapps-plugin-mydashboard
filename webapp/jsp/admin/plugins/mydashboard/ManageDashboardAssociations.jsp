<jsp:useBean id="managemydashboardpanelDashboardAssociation" scope="session" class="fr.paris.lutece.plugins.mydashboard.web.DashboardAssociationJspBean" />
<% String strContent = managemydashboardpanelDashboardAssociation.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
