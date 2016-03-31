/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mydashboard.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mydashboard.business.MyDashboardConfiguration;
import fr.paris.lutece.plugins.mydashboard.business.Panel;
import fr.paris.lutece.plugins.mydashboard.business.PanelHome;
import fr.paris.lutece.plugins.mydashboard.service.IMyDashboardComponent;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.url.UrlItem;


/**
 * My Dashboard application
 */
@Controller( xpageName = "mydashboard", pageTitleI18nKey = "mydashboard.getDashboard.pageTitle", pagePathI18nKey = "mydashboard.getDashboard.pagePath" )
public class MyDashboardApp extends MVCApplication
{
    private static final long serialVersionUID = 5462159859477351128L;
    private static final String VIEW_GET_DASHBOARDS = "getDashboards";
    private static final String VIEW_GET_MODIFY_DASHBOARDS = "getModifyDashboards";
    private static final String ACTION_DO_MODIFY_DASHBOARDS = "doModifyDashboards";
    private static final String ACTION_DO_MOVE_DASHBOARD = "doMoveDashboard";
    private static final String MARK_LIST_DASHBOARDS_CONTENT = "listDashboardsContent";
    private static final String MARK_LIST_DASHBOARDS_CONFIG = "listDashboardsConfig";
    private static final String MARK_LIST_DASHBOARDS_COMPONENTS = "listDashboardsComponents";
    private static final String MARK_LIST_COMPONENTS_DESCRIPTION = "listComponentsDescription";
    private static final String MARK_LIST_PANEL = "listPanel";
    private static final String MARK_PANEL_SELECTED = "panelSelected";
    
    private static final String MARK_LOCALE = "locale";
    private static final String PARAMETER_BACK = "back";
    private static final String PARAMETER_PANEL= "panel";
    private static final String PARAMETER_SUFFIX_DISPLAY = "_display";
    private static final String PARAMETER_MOVE_UP = "moveUp";
    private static final String PARAMETER_DASHBOARD_COMPONENT_ID = "myDashboardComponentId";
    private static final String JSP_URL_PORTAL = "jsp/site/Portal.jsp";
    private static final String TEMPLATE_GET_DASHBOARDS = "skin/plugins/mydashboard/get_dashboards.html";
    private static final String TEMPLATE_GET_MODIFY_DASHBOARDS = "skin/plugins/mydashboard/modify_dashboards.html";

    /**
     * Get the list of dash boards of the current user
     * @param request The request, with the user logged in
     * @return The XPage to display the list of dash boards of the user
     * @throws UserNotSignedException If the user is not logged in
     */
    @View( value = VIEW_GET_DASHBOARDS, defaultView = true )
    public XPage getDashboards( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.isAuthenticationEnable(  )
            ? SecurityService.getInstance(  ).getRegisteredUser( request ) : null;

        if ( luteceUser == null )
        {
            throw new UserNotSignedException(  );
        }

        MyDashboardService dashboardService = MyDashboardService.getInstance(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        List<IMyDashboardComponent> listDashboardComponents;
        if(dashboardService.isPanelEnabled())
        {	Panel panel=null;
        	String strPanelCode=request.getParameter(PARAMETER_PANEL);
        	if(!StringUtils.isEmpty(strPanelCode))
        	{
        		panel=PanelHome.findByCode(strPanelCode);
        		
        	}
        	if(panel==null)
        	{
        		panel=PanelHome.getDefaultPanel();
        	}
   
        	listDashboardComponents = dashboardService.getDashboardComponentListFromUser( luteceUser,panel );
        	 model.put( MARK_LIST_PANEL, PanelHome.getPanelsList() );
        	 model.put(MARK_PANEL_SELECTED, panel);
       }
        else
        {
        	
        	 listDashboardComponents = dashboardService.getDashboardComponentListFromUser( luteceUser );
        		
        }
        
        List<String> listDashboardContent = new ArrayList<String>( listDashboardComponents.size(  ) );

        for ( IMyDashboardComponent dashboardComponent : listDashboardComponents )
        {
            if ( dashboardComponent.isAvailable( luteceUser ) )
            {
                listDashboardContent.add( dashboardComponent.getDashboardData( request ) );
            }
        }

        model.put( MARK_LIST_DASHBOARDS_CONTENT, listDashboardContent );

        return getXPage( TEMPLATE_GET_DASHBOARDS, request.getLocale(  ), model );
    }

    /**
     * Get the XPage to modify front office dash boards of the current user
     * @param request The request
     * @return The XPage to display
     * @throws UserNotSignedException If the user has not signed in
     */
    @View( VIEW_GET_MODIFY_DASHBOARDS )
    public XPage getModifyDashboards( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.isAuthenticationEnable(  )
            ? SecurityService.getInstance(  ).getRegisteredUser( request ) : null;

        if ( luteceUser == null )
        {
            throw new UserNotSignedException(  );
        }

        MyDashboardService dashboardService = MyDashboardService.getInstance(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        List<MyDashboardConfiguration> listDashboardConfig = dashboardService.getUserConfig( luteceUser );
        List<IMyDashboardComponent> listDashboardComponents = dashboardService.getMyDashboardComponentsList( luteceUser );

        Map<String, String> mapComponentsDescription = new HashMap<String, String>( listDashboardComponents.size(  ) );

        for ( IMyDashboardComponent component : listDashboardComponents )
        {
            mapComponentsDescription.put( component.getComponentId(  ),
                component.getComponentDescription( request.getLocale(  ) ) );
        }

        model.put( MARK_LIST_DASHBOARDS_CONFIG, listDashboardConfig );
        model.put( MARK_LIST_DASHBOARDS_COMPONENTS, listDashboardComponents );
        model.put( MARK_LIST_COMPONENTS_DESCRIPTION, mapComponentsDescription );
        model.put( MARK_LOCALE, request.getLocale(  ) );

        return getXPage( TEMPLATE_GET_MODIFY_DASHBOARDS, request.getLocale(  ), model );
    }

    /**
     * Do the modification of front office dash boards
     * @param request The request
     * @return The next view to redirect to
     * @throws UserNotSignedException If the user has not signed in
     */
    @Action( ACTION_DO_MODIFY_DASHBOARDS )
    public XPage doModifyDashboards( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.isAuthenticationEnable(  )
            ? SecurityService.getInstance(  ).getRegisteredUser( request ) : null;

        if ( luteceUser == null )
        {
            throw new UserNotSignedException(  );
        }

        if ( request.getParameter( PARAMETER_BACK ) != null )
        {
            return redirectView( request, VIEW_GET_DASHBOARDS );
        }

        MyDashboardService dashboardService = MyDashboardService.getInstance(  );

        List<MyDashboardConfiguration> listDashboardConfig = dashboardService.getUserConfig( luteceUser );

        for ( MyDashboardConfiguration config : listDashboardConfig )
        {
            String strChecked = request.getParameter( config.getMyDashboardComponentId(  ) + PARAMETER_SUFFIX_DISPLAY );
            config.setHideDashboard( strChecked == null );
        }

        dashboardService.updateConfigList( listDashboardConfig );

        return redirectView( request, VIEW_GET_DASHBOARDS );
    }

    /**
     * Do move a dash board up or down
     * @param request The request
     * @return The next view to redirect to
     * @throws UserNotSignedException If the user has not signed in
     */
    @Action( ACTION_DO_MOVE_DASHBOARD )
    public XPage doMoveDashboard( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.isAuthenticationEnable(  )
            ? SecurityService.getInstance(  ).getRegisteredUser( request ) : null;

        if ( luteceUser == null )
        {
            throw new UserNotSignedException(  );
        }

        String strDashboardComponentId = request.getParameter( PARAMETER_DASHBOARD_COMPONENT_ID );

        if ( StringUtils.isNotEmpty( strDashboardComponentId ) )
        {
            MyDashboardService dashboardService = MyDashboardService.getInstance(  );
            boolean bMoveUp = Boolean.parseBoolean( request.getParameter( PARAMETER_MOVE_UP ) );
            List<MyDashboardConfiguration> listConfig = dashboardService.getUserConfig( luteceUser );
            int nOldOrder = 0;
            int nNewOrder = 0;
            MyDashboardConfiguration configToSave = null;

            for ( MyDashboardConfiguration config : listConfig )
            {
                if ( StringUtils.equals( config.getMyDashboardComponentId(  ), strDashboardComponentId ) )
                {
                    nOldOrder = config.getOrder(  );

                    if ( bMoveUp )
                    {
                        nNewOrder = nOldOrder - 1;
                    }
                    else
                    {
                        nNewOrder = nOldOrder + 1;
                    }

                    config.setOrder( nNewOrder );
                    configToSave = config;

                    break;
                }
            }

            boolean bSaved = false;

            for ( MyDashboardConfiguration config : listConfig )
            {
                if ( ( config.getOrder(  ) == nNewOrder ) &&
                        !StringUtils.equals( config.getMyDashboardComponentId(  ), strDashboardComponentId ) )
                {
                    config.setOrder( nOldOrder );
                    dashboardService.updateConfig( config, false );
                    dashboardService.updateConfig( configToSave, false );
                    bSaved = true;

                    break;
                }
            }

            if ( !bSaved && ( configToSave != null ) )
            {
                configToSave.setOrder( nOldOrder );
            }
            else
            {
                Collections.sort( listConfig );
            }
        }

        return redirectView( request, VIEW_GET_MODIFY_DASHBOARDS );
    }

    /**
     * Get the URL of the default view of this application
     * @param strBaseUrl The base URL to use
     * @return The URL of the default view of this application
     */
    public static String getUrlDefaultView( String strBaseUrl )
    {
        UrlItem url = new UrlItem( strBaseUrl + JSP_URL_PORTAL );
        url.addParameter( MVCUtils.PARAMETER_PAGE, "mydashboard" );

        return url.getUrl(  );
    }
}
