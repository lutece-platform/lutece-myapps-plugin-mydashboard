/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import fr.paris.lutece.plugins.mydashboard.business.Panel;
import fr.paris.lutece.plugins.mydashboard.business.PanelHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Panel features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageMyDashboard.jsp", controllerPath = "jsp/admin/plugins/mydashboard/", right = "MYDASHBOARD_MANAGEMENT" )
public class MyDashboardJspBean extends ManageMydashboardPanelJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_PANELS = "/admin/plugins/mydashboard/manage_panels.html";
    private static final String TEMPLATE_CREATE_PANEL = "/admin/plugins/mydashboard/create_panel.html";
    private static final String TEMPLATE_MODIFY_PANEL = "/admin/plugins/mydashboard/modify_panel.html";

    // Parameters
    private static final String PARAMETER_ID_PANEL = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PANELS = "mydashboard.manage_panels.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PANEL = "mydashboard.modify_panel.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PANEL = "mydashboard.create_panel.pageTitle";

    // Markers
    private static final String MARK_PANEL_LIST = "panel_list";
    private static final String MARK_PANEL = "panel";

    private static final String JSP_MANAGE_PANELS = "jsp/admin/plugins/mydashboard/ManagePanels.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_PANEL = "mydashboard.message.confirmRemovePanel";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "mydashboard.model.entity.panel.attribute.";

    // Views
    private static final String VIEW_MANAGE_PANELS = "managePanels";
    private static final String VIEW_CREATE_PANEL = "createPanel";
    private static final String VIEW_MODIFY_PANEL = "modifyPanel";

    // Actions
    private static final String ACTION_CREATE_PANEL = "createPanel";
    private static final String ACTION_MODIFY_PANEL = "modifyPanel";
    private static final String ACTION_REMOVE_PANEL = "removePanel";
    private static final String ACTION_CONFIRM_REMOVE_PANEL = "confirmRemovePanel";

    // Infos
    private static final String INFO_PANEL_CREATED = "mydashboard.info.panel.created";
    private static final String INFO_PANEL_UPDATED = "mydashboard.info.panel.updated";
    private static final String INFO_PANEL_REMOVED = "mydashboard.info.panel.removed";
    
    // Session variable to store working values
    private Panel _panel;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_PANELS, defaultView = true )
    public String getManagePanels( HttpServletRequest request )
    {
        _panel = null;
        List<Panel> listPanels = PanelHome.getPanelsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_PANEL_LIST, listPanels, JSP_MANAGE_PANELS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_PANELS, TEMPLATE_MANAGE_PANELS, model );
    }

    /**
     * Returns the form to create a panel
     *
     * @param request The Http request
     * @return the html code of the panel form
     */
    @View( VIEW_CREATE_PANEL )
    public String getCreatePanel( HttpServletRequest request )
    {
        _panel = ( _panel != null ) ? _panel : new Panel(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_PANEL, _panel );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PANEL, TEMPLATE_CREATE_PANEL, model );
    }

    /**
     * Process the data capture form of a new panel
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_PANEL )
    public String doCreatePanel( HttpServletRequest request )
    {
        populate( _panel, request );

        // Check constraints
        if ( !validateBean( _panel, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_PANEL );
        }

        PanelHome.create( _panel );
        addInfo( INFO_PANEL_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Manages the removal form of a panel whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_PANEL )
    public String getConfirmRemovePanel( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PANEL ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PANEL ) );
        url.addParameter( PARAMETER_ID_PANEL, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PANEL, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a panel
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage panels
     */
    @Action( ACTION_REMOVE_PANEL )
    public String doRemovePanel( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PANEL ) );
        PanelHome.remove( nId );
        addInfo( INFO_PANEL_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Returns the form to update info about a panel
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_PANEL )
    public String getModifyPanel( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PANEL ) );

        if ( _panel == null || ( _panel.getId(  ) != nId ))
        {
            _panel = PanelHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_PANEL, _panel );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PANEL, TEMPLATE_MODIFY_PANEL, model );
    }

    /**
     * Process the change form of a panel
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_PANEL )
    public String doModifyPanel( HttpServletRequest request )
    {
        populate( _panel, request );

        // Check constraints
        if ( !validateBean( _panel, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID_PANEL, _panel.getId( ) );
        }

        PanelHome.update( _panel );
        addInfo( INFO_PANEL_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }
}