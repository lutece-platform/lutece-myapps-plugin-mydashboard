package fr.paris.lutece.plugins.mydashboard.business.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mydashboard.service.IMyDashboardComponent;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardService;
import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Portlet to display dashboards of front office users
 */
public class MyDashboardPortlet extends PortletHtmlContent
{
    private static final String MARK_LIST_DASHBOARDS_CONTENT = "listDashboardsContent";
    private static final String MARK_PORTLET = "portlet";

    private static final String TEMPLATE_PORTLET_MY_DASHBOARDS = "skin/plugins/mydashboard/portlet/portlet_my_dashboards.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user == null )
            {
                return StringUtils.EMPTY;
            }

            Map<String, Object> model = new HashMap<String, Object>( );

            List<IMyDashboardComponent> listDashboardComponents = MyDashboardService.getInstance( )
                    .getDashboardComponentListFromUserName( user.getName( ) );

            List<String> listDashboardContent = new ArrayList<String>( listDashboardComponents.size( ) );
            for ( IMyDashboardComponent dashboardComponent : listDashboardComponents )
            {
                listDashboardContent.add( dashboardComponent.getDashboardData( request ) );
            }

            model.put( MARK_LIST_DASHBOARDS_CONTENT, listDashboardContent );
            model.put( MARK_PORTLET, this );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PORTLET_MY_DASHBOARDS,
                    request.getLocale( ), model );
            return template.getHtml( );
        }

        return StringUtils.EMPTY;
    }
}
