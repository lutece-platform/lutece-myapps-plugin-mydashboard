package fr.paris.lutece.plugins.mydashboard.service;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Dashboard component to modify dashboard components
 */
public class DashboardComponentMyDashboard extends MyDashboardComponent
{
    private static final String DASHBOARD_COMPONENT_ID = "mydashboard.dashboardComponentMyDashboard";

    private static final String MESSAGE_COMPONENT_DESCRIPTION = "mydashboard.dashboardComponentMyDashboard.description";

    private static final String TEMPLATE_DASHBOARD_COMPONENT_MY_DASHBOARD = "skin/plugins/mydashboard/dashboardcomponent/dashboardComponentMyDashboard.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_COMPONENT_MY_DASHBOARD,
                request.getLocale( ), model );
        return template.getHtml( );
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
        return I18nService.getLocalizedString( MESSAGE_COMPONENT_DESCRIPTION, locale );
    }
}
