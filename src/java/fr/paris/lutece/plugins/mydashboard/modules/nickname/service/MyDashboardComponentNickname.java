package fr.paris.lutece.plugins.mydashboard.modules.nickname.service;

import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * Dashboard component to manage nicknames
 */
public class MyDashboardComponentNickname extends MyDashboardComponent
{
    private static final String DASHBOARD_COMPONENT_ID = "mydashboard.myDashboardComponentNickname";

    private static final String MESSAGE_DASHBOARD_COMPONENT_NICKNAME_DESCRIPTION = "module.mydashboard.nickname.dashboardComponent.nickname.description";

    private static final String MARK_USER_NICKNACME = "nickname";

    private static final String TEMPLATE_DASHBOARD_COMPONENT_PSEUDO = "skin/plugins/mydashboard/modules/nickname/mydashboardcomponent/mydashboardcomponent_nickname.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user != null )
            {
                String strUserNickName = UserPreferencesService.instance( ).getNickname( user );
                Map<String, Object> model = new HashMap<String, Object>( );
                model.put( MARK_USER_NICKNACME, strUserNickName );
                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_COMPONENT_PSEUDO,
                        request.getLocale( ), model );
                return template.getHtml( );
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComponentId( )
    {
        return DASHBOARD_COMPONENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_DASHBOARD_COMPONENT_NICKNAME_DESCRIPTION, locale );
    }

}
