package fr.paris.lutece.plugins.mydashboard.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mydashboard.business.MyDashboardConfiguration;
import fr.paris.lutece.plugins.mydashboard.service.IMyDashboardComponent;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;


/**
 * My Dashboard application
 */
@Controller( xpageName = "mydashboard", pageTitleI18nKey = "mydashboard.getDashboard.pageTitle" )
public class MyDashboardApp extends MVCApplication
{
    private static final String VIEW_GET_DASHBOARDS = "getDashboards";
    private static final String VIEW_GET_MODIFY_DASHBOARDS = "getModifyDashboards";
    private static final String ACTION_DO_MODIFY_DASHBOARDS = "doModifyDashboards";
    private static final String ACTION_DO_MOVE_DASHBOARD = "doMoveDashboard";

    private static final String MARK_LIST_DASHBOARDS_CONTENT = "listDashboardsContent";
    private static final String MARK_LIST_DASHBOARDS_CONFIG = "listDashboardsConfig";
    private static final String MARK_LIST_DASHBOARDS_COMPONENTS = "listDashboardsComponents";
    private static final String MARK_LIST_COMPONENTS_DESCRIPTION = "listComponentsDescription";
    private static final String MARK_LOCALE = "locale";

    private static final String PARAMETER_BACK = "back";
    private static final String PARAMETER_SUFFIX_DISPLAY = "_display";

    private static final String TEMPLATE_GET_DASHBOARDS = "skin/plugins/mydashboard/get_dashboards.html";
    private static final String TEMPLATE_GET_MODIFY_DASHBOARDS = "skin/plugins/mydashboard/modify_dashboards.html";

    /**
     * Get the list of dash boards of the current user
     * @param request The request, with the user logged in
     * @return The XPage to display the list of dash boards of the user
     * @throws UserNotSignedException If the user is not logged in
     */
    @View( value = VIEW_GET_DASHBOARDS, defaultView = true )
    public XPage getDashboards( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }
        MyDashboardService dashboardService = MyDashboardService.getInstance( );

        Map<String, Object> model = new HashMap<String, Object>( );
        List<IMyDashboardComponent> listDashboardComponents = dashboardService
                .getDashboardComponentListFromUserName( luteceUser.getName( ) );

        List<String> listDashboardContent = new ArrayList<String>( listDashboardComponents.size( ) );
        for ( IMyDashboardComponent dashboardComponent : listDashboardComponents )
        {
            listDashboardContent.add( dashboardComponent.getDashboardData( request ) );
        }

        model.put( MARK_LIST_DASHBOARDS_CONTENT, listDashboardContent );
        return getXPage( TEMPLATE_GET_DASHBOARDS, request.getLocale( ), model );
    }

    /**
     * Get the XPage to modify front office dash boards of the current user
     * @param request The request
     * @return The XPage to display
     * @throws UserNotSignedException If the user has not signed in
     */
    @View( VIEW_GET_MODIFY_DASHBOARDS )
    public XPage getModifyDashboards( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }
        MyDashboardService dashboardService = MyDashboardService.getInstance( );

        Map<String, Object> model = new HashMap<String, Object>( );
        List<MyDashboardConfiguration> listDashboardConfig = dashboardService.getUserConfig( luteceUser.getName( ) );
        List<IMyDashboardComponent> listDashboardComponents = dashboardService.getMyDashboardComponentsList( );

        Map<String, String> mapComponentsDescription = new HashMap<String, String>( listDashboardComponents.size( ) );
        for ( IMyDashboardComponent component : listDashboardComponents )
        {
            mapComponentsDescription.put( component.getComponentId( ),
                    component.getComponentDescription( request.getLocale( ) ) );
        }

        model.put( MARK_LIST_DASHBOARDS_CONFIG, listDashboardConfig );
        model.put( MARK_LIST_DASHBOARDS_COMPONENTS, listDashboardComponents );
        model.put( MARK_LIST_COMPONENTS_DESCRIPTION, mapComponentsDescription );
        model.put( MARK_LOCALE, request.getLocale( ) );
        return getXPage( TEMPLATE_GET_MODIFY_DASHBOARDS, request.getLocale( ), model );
    }

    /**
     * Do the modification of front office dash boards
     * @param request The request
     * @return The next view to redirect to
     * @throws UserNotSignedException If the user has not signed in
     */
    @Action( ACTION_DO_MODIFY_DASHBOARDS )
    public XPage doModifyDashboards( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }

        if ( request.getParameter( PARAMETER_BACK ) != null )
        {
            return redirectView( request, VIEW_GET_DASHBOARDS );
        }

        MyDashboardService dashboardService = MyDashboardService.getInstance( );

        List<MyDashboardConfiguration> listDashboardConfig = dashboardService.getUserConfig( luteceUser.getName( ) );

        for ( MyDashboardConfiguration config : listDashboardConfig )
        {
            String strChecked = request.getParameter( config.getMyDashboardComponentId( ) + PARAMETER_SUFFIX_DISPLAY );
            config.setHideDashboard( strChecked == null );
        }
        dashboardService.updateConfigList( listDashboardConfig );

        return redirectView( request, VIEW_GET_MODIFY_DASHBOARDS );
    }

    /**
     * Do move a dash board up or down
     * @param request The request
     * @return The next view to redirect to
     * @throws UserNotSignedException If the user has not signed in
     */
    @Action( ACTION_DO_MOVE_DASHBOARD )
    public XPage doMoveDashboard( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }

        return redirectView( request, VIEW_GET_MODIFY_DASHBOARDS );
    }
}
