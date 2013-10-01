package fr.paris.lutece.plugins.mydashboard.modules.nickname.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mydashboard.web.MyDashboardApp;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPathService;


/**
 * Application to change user nicknames
 */
public class MyDashboardNicknameApp
{
    private static final String PARAMETER_NICKNAME = "nickname";

    private static final String HTML_PARAMETER_REFERER = "referer";

    /**
     * Do change the nickname of the user
     * @param request The request
     * @return The next URL to redirect to
     */
    public static String doChangeUserNickname( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.isAuthenticationEnable( ) ? SecurityService.getInstance( ).getRegisteredUser(
                request ) : null;
        if ( user != null )
        {
            String strNickname = request.getParameter( PARAMETER_NICKNAME );
            UserPreferencesService.instance( ).setNickname( user.getName( ), strNickname );
        }

        String strReferer = request.getHeader( HTML_PARAMETER_REFERER );

        if ( StringUtils.isEmpty( strReferer ) )
        {
            return MyDashboardApp.getUrlDefaultView( AppPathService.getBaseUrl( request ) );
        }

        return strReferer;
    }
}
