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
package fr.paris.lutece.plugins.mydashboard.business.portlet;

import fr.paris.lutece.plugins.mydashboard.business.Panel;
import fr.paris.lutece.plugins.mydashboard.business.PanelHome;
import fr.paris.lutece.plugins.mydashboard.service.IMyDashboardComponent;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardService;
import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;

import jakarta.enterprise.inject.spi.CDI;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Portlet to display dashboards of front office users. The content is rendered with the FreeMarker template chosen for the portlet
 * among the templates registered for the portlet type in the core (Section Template Management feature).
 */
public class MyDashboardPortlet extends PortletHtmlContent
{
    private static final String MARK_LIST_DASHBOARDS_CONTENT = "listDashboardsContent";
    private static final String TEMPLATE_PORTLET_DEFAULT = "skin/plugins/mydashboard/portlet/mydashboard_portlet.html";

    // Variables declarations
    private int _nIdPanel;

    /**
     * Constructor
     */
    public MyDashboardPortlet( )
    {
        setPortletTypeId( MyDashboardPortletHome.getInstance( ).getPortletTypeId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( user == null )
            {
                return StringUtils.EMPTY;
            }
            
            MyDashboardService dashboardService = CDI.current().select( MyDashboardService.class ).get();
            List<IMyDashboardComponent> listDashboardComponents;
            
            if ( dashboardService.isPanelEnabled(  ) )
            {
                Panel panel = null;
                int idPanel = this.getIdPanel( );

                if ( idPanel != 0 )
                {
                    panel = PanelHome.findByPrimaryKey( idPanel );
                }

                if ( panel == null )
                {
                    panel = PanelHome.getDefaultPanel(  );
                }

                listDashboardComponents = dashboardService.getDashboardComponentListFromUser( user, panel );
                
            }else {
            	
            	listDashboardComponents = dashboardService.getDashboardComponentListFromUser( user );
            }
            
            List<String> listDashboardContent = new ArrayList<>( listDashboardComponents.size(  ) );

            for ( IMyDashboardComponent dashboardComponent : listDashboardComponents )
            {
                if ( dashboardComponent.isAvailable( user ) )
                {
                    listDashboardContent.add( dashboardComponent.getDashboardData( request ) );
                }
            }

            return renderDashboards( request, listDashboardContent );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Renders the dashboards content with the template chosen for the portlet, or the default one
     * @param request The HTTP request
     * @param listDashboardContent The HTML content of each dashboard to display
     * @return The HTML content of the portlet
     */
    protected String renderDashboards( HttpServletRequest request, List<String> listDashboardContent )
    {
        // portlet, portlet_id, device_class, portlet_name (only if the title is displayed)
        Map<String, Object> model = createPortletModel( );
        model.put( MARK_LIST_DASHBOARDS_CONTENT, listDashboardContent );

        return renderTemplate( request, TEMPLATE_PORTLET_DEFAULT, model );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBeCachedForAnonymousUsers(  )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBeCachedForConnectedUsers(  )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(  )
    {
        MyDashboardPortletHome.getInstance(  ).remove( this );
    }
    
    /**
     * Returns the IdPannel
     * @return The IdPannel
     */
    public int getIdPanel(  )
    {
        return _nIdPanel;
    }

    /**
     * Sets the IdPannel
     * @param nIdPannel The IdPannel
     */
    public void setIdPanel( int nIdPannel )
    {
        _nIdPanel = nIdPannel;
    }

    


}
