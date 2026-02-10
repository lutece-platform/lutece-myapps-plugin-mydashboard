<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.mydashboard.modules.nickname.web.MyDashboardNicknameApp"%>

${ pageContext.response.sendRedirect( MyDashboardNicknameApp.doChangeUserNickname( pageContext.request ) ) }
