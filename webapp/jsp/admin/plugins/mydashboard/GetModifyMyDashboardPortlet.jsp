<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.mydashboard.web.portlet.MyDashboardPortletJspBean"%>

${ myDashboardPortletJspBean.init( pageContext.request, MyDashboardPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ myDashboardPortletJspBean.getModify( pageContext.request ) }
