<%@ page errorPage="../../ErrorPage.jsp" %>
<%@ page import = "fr.paris.lutece.plugins.mydashboard.modules.nickname.web.DashboardNicknameApp" %> 

<%
    response.sendRedirect(  DashboardNicknameApp.doChangeUserNickname( request )  );
%>
