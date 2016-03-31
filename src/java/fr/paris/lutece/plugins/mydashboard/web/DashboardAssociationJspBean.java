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

import fr.paris.lutece.plugins.mydashboard.business.DashboardAssociation;
import fr.paris.lutece.plugins.mydashboard.business.DashboardAssociationHome;
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
 * This class provides the user interface to manage DashboardAssociation features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDashboardAssociations.jsp", controllerPath = "jsp/admin/plugins/mydashboard/", right = "MYDASHBOARD_PANEL_MANAGEMENT" )
public class DashboardAssociationJspBean extends ManageMydashboardPanelJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_DASHBOARDASSOCIATIONS = "/admin/plugins/mydashboard/manage_dashboardassociations.html";
    private static final String TEMPLATE_CREATE_DASHBOARDASSOCIATION = "/admin/plugins/mydashboard/create_dashboardassociation.html";
    private static final String TEMPLATE_MODIFY_DASHBOARDASSOCIATION = "/admin/plugins/mydashboard/modify_dashboardassociation.html";

    // Parameters
    private static final String PARAMETER_ID_DASHBOARDASSOCIATION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DASHBOARDASSOCIATIONS = "mydashboard.manage_dashboardassociations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DASHBOARDASSOCIATION = "mydashboard.modify_dashboardassociation.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DASHBOARDASSOCIATION = "mydashboard.create_dashboardassociation.pageTitle";

    // Markers
    private static final String MARK_DASHBOARDASSOCIATION_LIST = "dashboardassociation_list";
    private static final String MARK_DASHBOARDASSOCIATION = "dashboardassociation";

    private static final String JSP_MANAGE_DASHBOARDASSOCIATIONS = "jsp/admin/plugins/mydashboard/ManageDashboardAssociations.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DASHBOARDASSOCIATION = "mydashboard.message.confirmRemoveDashboardAssociation";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "mydashboard.model.entity.dashboardassociation.attribute.";

    // Views
    private static final String VIEW_MANAGE_DASHBOARDASSOCIATIONS = "manageDashboardAssociations";
    private static final String VIEW_CREATE_DASHBOARDASSOCIATION = "createDashboardAssociation";
    private static final String VIEW_MODIFY_DASHBOARDASSOCIATION = "modifyDashboardAssociation";

    // Actions
    private static final String ACTION_CREATE_DASHBOARDASSOCIATION = "createDashboardAssociation";
    private static final String ACTION_MODIFY_DASHBOARDASSOCIATION = "modifyDashboardAssociation";
    private static final String ACTION_REMOVE_DASHBOARDASSOCIATION = "removeDashboardAssociation";
    private static final String ACTION_CONFIRM_REMOVE_DASHBOARDASSOCIATION = "confirmRemoveDashboardAssociation";

    // Infos
    private static final String INFO_DASHBOARDASSOCIATION_CREATED = "mydashboard.info.dashboardassociation.created";
    private static final String INFO_DASHBOARDASSOCIATION_UPDATED = "mydashboard.info.dashboardassociation.updated";
    private static final String INFO_DASHBOARDASSOCIATION_REMOVED = "mydashboard.info.dashboardassociation.removed";
    
    // Session variable to store working values
    private DashboardAssociation _dashboardassociation;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DASHBOARDASSOCIATIONS, defaultView = true )
    public String getManageDashboardAssociations( HttpServletRequest request )
    {
        _dashboardassociation = null;
        List<DashboardAssociation> listDashboardAssociations = DashboardAssociationHome.getDashboardAssociationsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DASHBOARDASSOCIATION_LIST, listDashboardAssociations, JSP_MANAGE_DASHBOARDASSOCIATIONS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DASHBOARDASSOCIATIONS, TEMPLATE_MANAGE_DASHBOARDASSOCIATIONS, model );
    }

    /**
     * Returns the form to create a dashboardassociation
     *
     * @param request The Http request
     * @return the html code of the dashboardassociation form
     */
    @View( VIEW_CREATE_DASHBOARDASSOCIATION )
    public String getCreateDashboardAssociation( HttpServletRequest request )
    {
        _dashboardassociation = ( _dashboardassociation != null ) ? _dashboardassociation : new DashboardAssociation(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DASHBOARDASSOCIATION, _dashboardassociation );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DASHBOARDASSOCIATION, TEMPLATE_CREATE_DASHBOARDASSOCIATION, model );
    }

    /**
     * Process the data capture form of a new dashboardassociation
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DASHBOARDASSOCIATION )
    public String doCreateDashboardAssociation( HttpServletRequest request )
    {
        populate( _dashboardassociation, request );

        // Check constraints
        if ( !validateBean( _dashboardassociation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DASHBOARDASSOCIATION );
        }

        DashboardAssociationHome.create( _dashboardassociation );
        addInfo( INFO_DASHBOARDASSOCIATION_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DASHBOARDASSOCIATIONS );
    }

    /**
     * Manages the removal form of a dashboardassociation whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DASHBOARDASSOCIATION )
    public String getConfirmRemoveDashboardAssociation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DASHBOARDASSOCIATION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DASHBOARDASSOCIATION ) );
        url.addParameter( PARAMETER_ID_DASHBOARDASSOCIATION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DASHBOARDASSOCIATION, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a dashboardassociation
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage dashboardassociations
     */
    @Action( ACTION_REMOVE_DASHBOARDASSOCIATION )
    public String doRemoveDashboardAssociation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DASHBOARDASSOCIATION ) );
        DashboardAssociationHome.remove( nId );
        addInfo( INFO_DASHBOARDASSOCIATION_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DASHBOARDASSOCIATIONS );
    }

    /**
     * Returns the form to update info about a dashboardassociation
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DASHBOARDASSOCIATION )
    public String getModifyDashboardAssociation( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DASHBOARDASSOCIATION ) );

        if ( _dashboardassociation == null || ( _dashboardassociation.getId(  ) != nId ))
        {
            _dashboardassociation = DashboardAssociationHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_DASHBOARDASSOCIATION, _dashboardassociation );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DASHBOARDASSOCIATION, TEMPLATE_MODIFY_DASHBOARDASSOCIATION, model );
    }

    /**
     * Process the change form of a dashboardassociation
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DASHBOARDASSOCIATION )
    public String doModifyDashboardAssociation( HttpServletRequest request )
    {
        populate( _dashboardassociation, request );

        // Check constraints
        if ( !validateBean( _dashboardassociation, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DASHBOARDASSOCIATION, PARAMETER_ID_DASHBOARDASSOCIATION, _dashboardassociation.getId( ) );
        }

        DashboardAssociationHome.update( _dashboardassociation );
        addInfo( INFO_DASHBOARDASSOCIATION_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DASHBOARDASSOCIATIONS );
    }
}