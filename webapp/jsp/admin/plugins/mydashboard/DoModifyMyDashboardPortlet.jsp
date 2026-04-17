<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.mydashboard.web.portlet.MyDashboardPortletJspBean"%>

${ myDashboardPortletJspBean.init( pageContext.request, MyDashboardPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( myDashboardPortletJspBean.doModify( pageContext.request ) ) }
