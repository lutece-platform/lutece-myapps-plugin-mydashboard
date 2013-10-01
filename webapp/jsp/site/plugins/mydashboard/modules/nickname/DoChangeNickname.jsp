<%@ page errorPage="../../ErrorPage.jsp" %>
<%@ page import = "fr.paris.lutece.plugins.mydashboard.modules.nickname.web.MyDashboardNicknameApp" %> 

<%
     response.sendRedirect(  MyDashboardNicknameApp.doChangeUserNickname( request )  );
 %>
