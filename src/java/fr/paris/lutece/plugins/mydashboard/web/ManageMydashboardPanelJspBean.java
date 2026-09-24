/*
 * Copyright (c) 2002-2026, Mairie de Paris
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
import fr.paris.lutece.plugins.mydashboard.business.Panel;
import fr.paris.lutece.plugins.mydashboard.business.PanelHome;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.util.IPager;
import fr.paris.lutece.portal.web.util.Pager;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Panel features ( manage, create, modify, remove )
 */
@RequestScoped
@Named
@Controller( controllerJsp = ManageMydashboardPanelJspBean.CONTROLLER_JSP, controllerPath = ManageMydashboardPanelJspBean.CONTROLLER_PATH, right = ManageMydashboardPanelJspBean.RIGHT_MANAGE_PANELS, securityTokenEnabled = true )
public class ManageMydashboardPanelJspBean extends MVCAdminJspBean
{
    public static final String RIGHT_MANAGE_PANELS = "MYDASHBOARD_PANEL_MANAGEMENT";
    public static final String CONTROLLER_JSP = "ManageMyDashboardPanel.jsp";
    public static final String CONTROLLER_PATH = "jsp/admin/plugins/mydashboard/";

    private static final long serialVersionUID = -8224755001223060220L;

    // Templates
    private static final String TEMPLATE_MANAGE_PANELS = "/admin/plugins/mydashboard/manage_panels.html";
    private static final String TEMPLATE_CREATE_PANEL = "/admin/plugins/mydashboard/create_panel.html";
    private static final String TEMPLATE_MODIFY_PANEL = "/admin/plugins/mydashboard/modify_panel.html";

    // Parameters
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_ID_COMPONENT = "id_component";
    private static final String PARAMETER_CODE = "code";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_DEFAULT = "default";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PANELS = "mydashboard.managePanels.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PANEL = "mydashboard.modifyPanel.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PANEL = "mydashboard.createPanel.pageTitle";
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "mydashboard.listItems.itemsPerPage";

    // Markers
    private static final String MARK_PANEL_LIST = "panel_list";
    private static final String MARK_PANEL = "panel";
    private static final String MARK_LIST_DASHBOARD_COMPONENT = "list_dashboard_component";
    private static final String MARK_LIST_DASHBOARD_COMPONENT_ASSOCIATED = "list_dashboard_component_associated";
    private static final String MARK_MAP_DASHBOARD_COMPONENT = "map_dashboard_component";
    private static final String MARK_MAP_PANEL_LIST_DASHBOARD_COMPONENT = "map_panel_list_dashboard_component";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_PANEL = "mydashboard.message.confirmRemoveTableau";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "mydashboard.model.entity.panel.attribute.";

    // Views
    private static final String VIEW_MANAGE_PANELS = "managePanels";
    private static final String VIEW_CREATE_PANEL = "createPanel";
    private static final String VIEW_MODIFY_PANEL = "modifyPanel";
    private static final String VIEW_CONFIRM_REMOVE_PANEL = "confirmRemovePanel";

    // Actions : the actions of a form share the token of the view that renders it
    private static final String ACTION_CREATE_PANEL = VIEW_CREATE_PANEL;
    private static final String ACTION_CREATE_PANEL_MANAGE_ASSOCIATIONS = "createPanelAndManageAssociations";
    private static final String ACTION_MODIFY_PANEL = VIEW_MODIFY_PANEL;
    private static final String ACTION_REMOVE_PANEL = "removePanel";
    private static final String ACTION_REMOVE_COMPONENT = "removeComponent";
    private static final String ACTION_ADD_COMPONENT = "addComponent";
    private static final String ACTION_MOVE_UP_COMPONENT = "moveUpComponent";
    private static final String ACTION_MOVE_DOWN_COMPONENT = "moveDownComponent";

    // Infos
    private static final String INFO_PANEL_CREATED = "mydashboard.info.panel.created";
    private static final String INFO_PANEL_UPDATED = "mydashboard.info.panel.updated";
    private static final String INFO_PANEL_REMOVED = "mydashboard.info.panel.removed";
    private static final String INFO_COMPONENT_REMOVED = "mydashboard.info.component.removed";

    @Inject
    private MyDashboardService _dashboardService;

    @Inject
    @Pager( listBookmark = MARK_PANEL_LIST, defaultItemsPerPage = PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, baseUrl = CONTROLLER_PATH + CONTROLLER_JSP )
    private IPager<Panel, Panel> _pager;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @param model The model
     * @return The page
     */
    @View( value = VIEW_MANAGE_PANELS, defaultView = true )
    public String getManagePanels( HttpServletRequest request, Models model )
    {
        Map<String, String> mapDashboardComponents = _dashboardService.getMyDashboardComponentsRefList( getLocale( ) ).toMap( );

        Map<String, List<DashboardAssociation>> mapPanelDashboardAssociations = new HashMap<>( );

        for ( DashboardAssociation dashboardAssociation : DashboardAssociationHome.getDashboardAssociationsList( ) )
        {
            mapPanelDashboardAssociations.computeIfAbsent( Integer.toString( dashboardAssociation.getIdPanel( ) ), k -> new ArrayList<>( ) )
                    .add( dashboardAssociation );
        }

        model.put( MARK_MAP_PANEL_LIST_DASHBOARD_COMPONENT, mapPanelDashboardAssociations );
        model.put( MARK_MAP_DASHBOARD_COMPONENT, mapDashboardComponents );
        _pager.withListItem( PanelHome.getPanelsList( ) ).populateModels( request, model, getLocale( ) );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_PANELS, TEMPLATE_MANAGE_PANELS, model );
    }

    /**
     * Returns the form to create a panel
     *
     * @param request The Http request
     * @param model The model
     * @return the html code of the panel form
     */
    @View( VIEW_CREATE_PANEL )
    public String getCreatePanel( HttpServletRequest request, Models model )
    {
        if ( model.get( MARK_PANEL ) == null )
        {
            model.put( MARK_PANEL, new Panel( ) );
        }

        model.put( MARK_LIST_DASHBOARD_COMPONENT, _dashboardService.getMyDashboardComponentsRefList( getLocale( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PANEL, TEMPLATE_CREATE_PANEL, model );
    }

    /**
     * Process the data capture form of a new panel
     *
     * @param request The Http Request
     * @param model The model
     * @param strCode The code
     * @param strTitle The title
     * @param strDescription The description
     * @param strDefault The default flag
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_PANEL )
    public String doCreatePanel( HttpServletRequest request, Models model,
            @RequestParam( value = PARAMETER_CODE, defaultValue = "" ) String strCode,
            @RequestParam( value = PARAMETER_TITLE, defaultValue = "" ) String strTitle,
            @RequestParam( value = PARAMETER_DESCRIPTION, defaultValue = "" ) String strDescription,
            @RequestParam( value = PARAMETER_DEFAULT, defaultValue = "false" ) String strDefault )
    {
        Panel panel = buildPanel( new Panel( ), strCode, strTitle, strDescription, strDefault );

        if ( !validateBean( panel, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            model.put( MARK_PANEL, panel );

            return getCreatePanel( request, model );
        }

        PanelHome.create( panel );
        addInfo( INFO_PANEL_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Process the data capture form of a new panel and redirect to modify view
     *
     * @param request The Http Request
     * @param model The model
     * @param strCode The code
     * @param strTitle The title
     * @param strDescription The description
     * @param strDefault The default flag
     * @return The Jsp URL of the process result
     */
    @Action( value = ACTION_CREATE_PANEL_MANAGE_ASSOCIATIONS, securityTokenAction = ACTION_CREATE_PANEL )
    public String doCreatePanelManageAssociations( HttpServletRequest request, Models model,
            @RequestParam( value = PARAMETER_CODE, defaultValue = "" ) String strCode,
            @RequestParam( value = PARAMETER_TITLE, defaultValue = "" ) String strTitle,
            @RequestParam( value = PARAMETER_DESCRIPTION, defaultValue = "" ) String strDescription,
            @RequestParam( value = PARAMETER_DEFAULT, defaultValue = "false" ) String strDefault )
    {
        Panel panel = buildPanel( new Panel( ), strCode, strTitle, strDescription, strDefault );

        if ( !validateBean( panel, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            model.put( MARK_PANEL, panel );

            return getCreatePanel( request, model );
        }

        PanelHome.create( panel );
        addInfo( INFO_PANEL_CREATED, getLocale( ) );

        return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, panel.getId( ) );
    }

    /**
     * Manages the removal form of a panel whose identifier is in the http request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_PANEL, securityTokenAction = ACTION_REMOVE_PANEL )
    public String getConfirmRemovePanel( HttpServletRequest request )
    {
        int nId = getIntParameter( request, PARAMETER_ID );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PANEL ) );
        url.addParameter( PARAMETER_ID, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PANEL, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

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
        int nId = getIntParameter( request, PARAMETER_ID );

        if ( PanelHome.findByPrimaryKey( nId ) != null )
        {
            PanelHome.remove( nId );
            addInfo( INFO_PANEL_REMOVED, getLocale( ) );
        }

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Returns the form to update info about a panel
     *
     * @param request The Http request
     * @param model The model
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_PANEL )
    public String getModifyPanel( HttpServletRequest request, Models model )
    {
        int nId = getIntParameter( request, PARAMETER_ID );
        Panel panel = PanelHome.findByPrimaryKey( nId );

        if ( panel == null )
        {
            return redirectView( request, VIEW_MANAGE_PANELS );
        }

        if ( model.get( MARK_PANEL ) == null )
        {
            model.put( MARK_PANEL, panel );
        }

        ReferenceList refListDashBoardComponent = _dashboardService.getMyDashboardComponentsRefList( getLocale( ) );
        List<DashboardAssociation> listDashBoardAssociations = DashboardAssociationHome.getDashboardAssociationsListByIdPanel( nId );

        // Components that are not associated with the panel yet
        ReferenceList refListDashBoardComponentNotSelected = new ReferenceList( );

        for ( ReferenceItem refItem : refListDashBoardComponent )
        {
            boolean bAlreadyContains = listDashBoardAssociations.stream( )
                    .anyMatch( dashboardAssociation -> dashboardAssociation.getIdDashboard( ).equals( refItem.getCode( ) ) );

            if ( !bAlreadyContains )
            {
                refListDashBoardComponentNotSelected.add( refItem );
            }
        }

        model.put( MARK_LIST_DASHBOARD_COMPONENT, refListDashBoardComponentNotSelected );
        model.put( MARK_LIST_DASHBOARD_COMPONENT_ASSOCIATED, listDashBoardAssociations );
        model.put( MARK_MAP_DASHBOARD_COMPONENT, refListDashBoardComponent.toMap( ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PANEL, TEMPLATE_MODIFY_PANEL, model );
    }

    /**
     * Process the change form of a panel
     *
     * @param request The Http request
     * @param model The model
     * @param strCode The code
     * @param strTitle The title
     * @param strDescription The description
     * @param strDefault The default flag
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_PANEL )
    public String doModifyPanel( HttpServletRequest request, Models model,
            @RequestParam( value = PARAMETER_CODE, defaultValue = "" ) String strCode,
            @RequestParam( value = PARAMETER_TITLE, defaultValue = "" ) String strTitle,
            @RequestParam( value = PARAMETER_DESCRIPTION, defaultValue = "" ) String strDescription,
            @RequestParam( value = PARAMETER_DEFAULT, defaultValue = "false" ) String strDefault )
    {
        Panel panel = PanelHome.findByPrimaryKey( getIntParameter( request, PARAMETER_ID ) );

        if ( panel == null )
        {
            return redirectView( request, VIEW_MANAGE_PANELS );
        }

        buildPanel( panel, strCode, strTitle, strDescription, strDefault );

        if ( !validateBean( panel, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            model.put( MARK_PANEL, panel );

            return getModifyPanel( request, model );
        }

        PanelHome.update( panel );
        addInfo( INFO_PANEL_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Add component in the panel
     *
     * @param request The Http request
     * @return the jsp URL to display modify panel
     */
    @Action( value = ACTION_ADD_COMPONENT, securityTokenAction = ACTION_MODIFY_PANEL )
    public String doAddComponent( HttpServletRequest request )
    {
        String strIdComponent = request.getParameter( PARAMETER_ID_COMPONENT );
        int nIdPanel = getIntParameter( request, PARAMETER_ID );

        if ( StringUtils.isNotEmpty( strIdComponent ) && PanelHome.findByPrimaryKey( nIdPanel ) != null )
        {
            DashboardAssociation newDashboardAssociation = new DashboardAssociation( );
            newDashboardAssociation.setIdDashboard( strIdComponent );
            newDashboardAssociation.setIdPanel( nIdPanel );

            DashboardAssociationHome.create( newDashboardAssociation );

            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, nIdPanel );
        }

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Move up component
     *
     * @param request The Http request
     * @return the jsp URL to display modify panel
     */
    @Action( value = ACTION_MOVE_UP_COMPONENT, securityTokenAction = ACTION_MODIFY_PANEL )
    public String doMoveUpComponent( HttpServletRequest request )
    {
        return moveComponent( request, -1 );
    }

    /**
     * Move down component
     *
     * @param request The Http request
     * @return The jsp URL to display modify panel
     */
    @Action( value = ACTION_MOVE_DOWN_COMPONENT, securityTokenAction = ACTION_MODIFY_PANEL )
    public String doMoveDownComponent( HttpServletRequest request )
    {
        return moveComponent( request, 1 );
    }

    /**
     * remove component
     *
     * @param request The Http request
     * @return The jsp URL to display modify panel
     */
    @Action( value = ACTION_REMOVE_COMPONENT, securityTokenAction = ACTION_MODIFY_PANEL )
    public String doRemoveComponent( HttpServletRequest request )
    {
        DashboardAssociation dashboardAssociation = DashboardAssociationHome.findByPrimaryKey( getIntParameter( request, PARAMETER_ID ) );

        if ( dashboardAssociation != null )
        {
            DashboardAssociationHome.remove( dashboardAssociation.getId( ) );
            addInfo( INFO_COMPONENT_REMOVED, getLocale( ) );

            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, dashboardAssociation.getIdPanel( ) );
        }

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Swaps the position of a component with its neighbour in the panel
     *
     * @param request The Http request
     * @param nOffset -1 to move the component up, 1 to move it down
     * @return The jsp URL to display modify panel
     */
    private String moveComponent( HttpServletRequest request, int nOffset )
    {
        DashboardAssociation dashboardAssociationSelected = DashboardAssociationHome.findByPrimaryKey( getIntParameter( request, PARAMETER_ID ) );

        if ( dashboardAssociationSelected == null )
        {
            return redirectView( request, VIEW_MANAGE_PANELS );
        }

        List<DashboardAssociation> listDashboardAssociations = DashboardAssociationHome
                .getDashboardAssociationsListByIdPanel( dashboardAssociationSelected.getIdPanel( ) );

        for ( int nPosition = 0; nPosition < listDashboardAssociations.size( ); nPosition++ )
        {
            int nNeighbour = nPosition + nOffset;

            if ( listDashboardAssociations.get( nPosition ).getId( ) == dashboardAssociationSelected.getId( ) && nNeighbour >= 0
                    && nNeighbour < listDashboardAssociations.size( ) )
            {
                DashboardAssociation dashboardAssociationNeighbour = listDashboardAssociations.get( nNeighbour );
                int nNewPosition = dashboardAssociationNeighbour.getPosition( );
                dashboardAssociationNeighbour.setPosition( dashboardAssociationSelected.getPosition( ) );
                dashboardAssociationSelected.setPosition( nNewPosition );
                DashboardAssociationHome.update( dashboardAssociationNeighbour );
                DashboardAssociationHome.update( dashboardAssociationSelected );

                break;
            }
        }

        return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, dashboardAssociationSelected.getIdPanel( ) );
    }

    /**
     * Fills a panel with the data of the form
     *
     * @param panel The panel to fill
     * @param strCode The code
     * @param strTitle The title
     * @param strDescription The description
     * @param strDefault The default flag
     * @return The panel
     */
    private static Panel buildPanel( Panel panel, String strCode, String strTitle, String strDescription, String strDefault )
    {
        panel.setCode( strCode.trim( ) );
        panel.setTitle( strTitle.trim( ) );
        panel.setDescription( strDescription.trim( ) );
        panel.setDefault( Boolean.parseBoolean( strDefault ) );

        return panel;
    }

    /**
     * Reads an integer parameter of the request
     *
     * @param request The Http request
     * @param strParameter The parameter name
     * @return The value, or 0 if the parameter is missing or not a number
     */
    private static int getIntParameter( HttpServletRequest request, String strParameter )
    {
        return NumberUtils.toInt( request.getParameter( strParameter ), 0 );
    }
}
