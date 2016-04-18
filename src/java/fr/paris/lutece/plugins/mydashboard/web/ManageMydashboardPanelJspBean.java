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
import fr.paris.lutece.plugins.mydashboard.business.Panel;
import fr.paris.lutece.plugins.mydashboard.business.PanelHome;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Panel features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageMyDashboardPanel.jsp", controllerPath = "jsp/admin/plugins/mydashboard/", right = "MYDASHBOARD_PANEL_MANAGEMENT" )
public class ManageMydashboardPanelJspBean extends MVCAdminJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_PANELS = "/admin/plugins/mydashboard/manage_panels.html";
    private static final String TEMPLATE_CREATE_PANEL = "/admin/plugins/mydashboard/create_panel.html";
    private static final String TEMPLATE_MODIFY_PANEL = "/admin/plugins/mydashboard/modify_panel.html";

    // Parameters
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_ID_COMPONENT = "id_component";
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PANELS = "mydashboard.manage_panels.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PANEL = "mydashboard.modify_panel.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PANEL = "mydashboard.create_panel.pageTitle";
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "mydashboard.listItems.itemsPerPage";

    // Markers
    private static final String MARK_PANEL_LIST = "panel_list";
    private static final String MARK_PANEL = "panel";
    private static final String MARK_LIST_DASHBOARD_COMPONENT = "list_dashboard_component";
    private static final String MARK_LIST_DASHBOARD_COMPONENT_ASSOCIATED = "list_dashboard_component_associated";
    private static final String MARK_MAP_DASHBOARD_COMPONENT = "map_dashboard_component";
    private static final String MARK_MAP_PANEL_LIST_DASHBOARD_COMPONENT = "map_panel_list_dashboard_component";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
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
    private static final String ACTION_CREATE_PANEL_MANAGE_ASSOCIATIONS = "createPanelAndManageAssociations";
    private static final String ACTION_MODIFY_PANEL = "modifyPanel";
    private static final String ACTION_REMOVE_PANEL = "removePanel";
    private static final String ACTION_REMOVE_COMPONENT = "removeComponent";
    private static final String ACTION_ADD_COMPONENT = "addComponent";
    private static final String ACTION_MOVE_UP_COMPONENT = "moveUpComponent";
    private static final String ACTION_MOVE_DOWN_COMPONENT = "moveDownComponent";
    private static final String ACTION_CONFIRM_REMOVE_PANEL = "confirmRemovePanel";

    // Infos
    private static final String INFO_PANEL_CREATED = "mydashboard.info.panel.created";
    private static final String INFO_PANEL_UPDATED = "mydashboard.info.panel.updated";
    private static final String INFO_PANEL_REMOVED = "mydashboard.info.panel.removed";
    private static final String INFO_COMPONENT_REMOVED = "mydashboard.info.component.removed";

    // Session variable to store working values
    private Panel _panel;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

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

        ReferenceList refListDashBoardComponent = MyDashboardService.getInstance(  )
                                                                    .getMyDashboardComponentsRefList( getLocale(  ) );
        Map<String, String> mapDashboardComponents = refListDashBoardComponent.toMap(  );

        Map<String, List<DashboardAssociation>> mapPanelDashboardAssociations = new HashMap<String, List<DashboardAssociation>>(  );
        List<DashboardAssociation> listDashBoardAssociations = DashboardAssociationHome.getDashboardAssociationsList(  );

        for ( DashboardAssociation dashboardAssociation : listDashBoardAssociations )
        {
            if ( !mapPanelDashboardAssociations.containsKey( Integer.toString( dashboardAssociation.getIdPanel(  ) ) ) )
            {
                mapPanelDashboardAssociations.put( Integer.toString( dashboardAssociation.getIdPanel(  ) ),
                    new ArrayList<DashboardAssociation>(  ) );
            }

            mapPanelDashboardAssociations.get( Integer.toString( dashboardAssociation.getIdPanel(  ) ) )
                                         .add( dashboardAssociation );
        }

        model.put( MARK_MAP_PANEL_LIST_DASHBOARD_COMPONENT, mapPanelDashboardAssociations );
        model.put( MARK_MAP_DASHBOARD_COMPONENT, mapDashboardComponents );
        model.put( MARK_PANEL_LIST, listPanels );

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

        ReferenceList refListDashBoardComponent = MyDashboardService.getInstance(  )
                                                                    .getMyDashboardComponentsRefList( getLocale(  ) );

        model.put( MARK_LIST_DASHBOARD_COMPONENT, refListDashBoardComponent );

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
     * Process the data capture form of a new panel and redirect to modify view
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_PANEL_MANAGE_ASSOCIATIONS )
    public String doCreatePanelManageAssociations( HttpServletRequest request )
    {
        populate( _panel, request );

        // Check constraints
        if ( !validateBean( _panel, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_PANEL );
        }

        PanelHome.create( _panel );
        addInfo( INFO_PANEL_CREATED, getLocale(  ) );

        return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, _panel.getId(  ) );
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
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PANEL ) );
        url.addParameter( PARAMETER_ID, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PANEL,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

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
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );
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
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );

        if ( ( _panel == null ) || ( _panel.getId(  ) != nId ) )
        {
            _panel = PanelHome.findByPrimaryKey( nId );
        }

        ReferenceList refListDashBoardComponentNotSelected = new ReferenceList(  );
        ReferenceList refListDashBoardComponent = MyDashboardService.getInstance(  )
                                                                    .getMyDashboardComponentsRefList( getLocale(  ) );

        Map<String, Object> model = getModel(  );
        model.put( MARK_PANEL, _panel );

        //Add Empty Select
        boolean bAlreadyContains;
        List<DashboardAssociation> listDashBoardAssociations = DashboardAssociationHome.getDashboardAssociationsListByIdPanel( nId );
        Map<String, String> mapDashboardComponents = refListDashBoardComponent.toMap(  );

        for ( ReferenceItem refItem : refListDashBoardComponent )
        {
            bAlreadyContains = false;

            for ( DashboardAssociation dashboardAssociation : listDashBoardAssociations )
            {
                if ( dashboardAssociation.getIdDashboard(  ).equals( refItem.getCode(  ) ) )
                {
                    bAlreadyContains = true;

                    break;
                }
            }

            if ( !bAlreadyContains )
            {
                refListDashBoardComponentNotSelected.add( refItem );
            }
        }

        model.put( MARK_LIST_DASHBOARD_COMPONENT, refListDashBoardComponentNotSelected );
        model.put( MARK_LIST_DASHBOARD_COMPONENT_ASSOCIATED, listDashBoardAssociations );
        model.put( MARK_MAP_DASHBOARD_COMPONENT, mapDashboardComponents );

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
            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, _panel.getId(  ) );
        }

        PanelHome.update( _panel );
        addInfo( INFO_PANEL_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Add component in the panel
     *
     * @param request The Http request
     * @return the jsp URL to display modify panel
     */
    @Action( ACTION_ADD_COMPONENT )
    public String doAddComponent( HttpServletRequest request )
    {
        String strIdComponent = request.getParameter( PARAMETER_ID_COMPONENT );
        String strIdPanel = request.getParameter( PARAMETER_ID );

        if ( !StringUtils.isEmpty( strIdComponent ) && !StringUtils.isEmpty( strIdPanel ) )
        {
            DashboardAssociation newDashboardAssociation = new DashboardAssociation(  );
            newDashboardAssociation.setIdDashboard( strIdComponent );
            newDashboardAssociation.setIdPanel( Integer.parseInt( strIdPanel ) );

            DashboardAssociationHome.create( newDashboardAssociation );

            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, newDashboardAssociation.getIdPanel(  ) );
        }

        addInfo( INFO_COMPONENT_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     *  Move up component
     *
     * @param request The Http request
     * @return the jsp URL to display modify panel
     */
    @Action( ACTION_MOVE_UP_COMPONENT )
    public String doMoveUpComponent( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );

        DashboardAssociation dashboardAssociationSelected = DashboardAssociationHome.findByPrimaryKey( nId );

        if ( dashboardAssociationSelected != null )
        {
            List<DashboardAssociation> listDashboardAssociations = DashboardAssociationHome.getDashboardAssociationsListByIdPanel( dashboardAssociationSelected.getIdPanel(  ) );
            int nbPosition = 0;

            for ( DashboardAssociation dashboardAssociation : listDashboardAssociations )
            {
                if ( ( dashboardAssociation.getId(  ) == dashboardAssociationSelected.getId(  ) ) && ( nbPosition != 0 ) )
                {
                    DashboardAssociation dashboardAssociationPrev = listDashboardAssociations.get( nbPosition - 1 );
                    int nNewPosition = dashboardAssociationPrev.getPosition(  );
                    dashboardAssociationPrev.setPosition( dashboardAssociationSelected.getPosition(  ) );
                    dashboardAssociationSelected.setPosition( nNewPosition );
                    DashboardAssociationHome.update( dashboardAssociationPrev );
                    DashboardAssociationHome.update( dashboardAssociationSelected );
                }

                nbPosition++;
            }

            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, dashboardAssociationSelected.getIdPanel(  ) );
        }

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     *  move down component
     *
     * @param request The Http request
     * @return The jsp URL to display modify panel
     */
    @Action( ACTION_MOVE_DOWN_COMPONENT )
    public String doMoveDownComponent( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );

        DashboardAssociation dashboardAssociationSelected = DashboardAssociationHome.findByPrimaryKey( nId );

        if ( dashboardAssociationSelected != null )
        {
            List<DashboardAssociation> listDashboardAssociations = DashboardAssociationHome.getDashboardAssociationsListByIdPanel( dashboardAssociationSelected.getIdPanel(  ) );
            int nbPosition = 0;

            for ( DashboardAssociation dashboardAssociation : listDashboardAssociations )
            {
                if ( ( dashboardAssociation.getId(  ) == dashboardAssociationSelected.getId(  ) ) &&
                        ( nbPosition != ( listDashboardAssociations.size(  ) - 1 ) ) )
                {
                    DashboardAssociation dashboardAssociationNext = listDashboardAssociations.get( nbPosition + 1 );
                    int nNewPosition = dashboardAssociationNext.getPosition(  );
                    dashboardAssociationNext.setPosition( dashboardAssociationSelected.getPosition(  ) );
                    dashboardAssociationSelected.setPosition( nNewPosition );
                    DashboardAssociationHome.update( dashboardAssociationNext );
                    DashboardAssociationHome.update( dashboardAssociationSelected );
                }

                nbPosition++;
            }

            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, dashboardAssociationSelected.getIdPanel(  ) );
        }

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * remove component
     *
     * @param request The Http request
     * @return The jsp URL to display modify panel
     */
    @Action( ACTION_REMOVE_COMPONENT )
    public String doRemoveComponent( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );

        DashboardAssociation dashboardAssociation = DashboardAssociationHome.findByPrimaryKey( nId );

        if ( dashboardAssociation != null )
        {
            DashboardAssociationHome.remove( dashboardAssociation.getId(  ) );

            return redirect( request, VIEW_MODIFY_PANEL, PARAMETER_ID, dashboardAssociation.getIdPanel(  ) );
        }

        addInfo( INFO_COMPONENT_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_PANELS );
    }

    /**
     * Return a model that contains the list and paginator infos
     * @param request The HTTP request
     * @param strBookmark The bookmark
     * @param list The list of item
     * @param strManageJsp The JSP
     * @return The model
     */
    protected Map<String, Object> getPaginatedListModel( HttpServletRequest request, String strBookmark, List list,
        String strManageJsp )
    {
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( strManageJsp );
        String strUrl = url.getUrl(  );

        // PAGINATOR
        LocalizedPaginator paginator = new LocalizedPaginator( list, _nItemsPerPage, strUrl, PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex, getLocale(  ) );

        Map<String, Object> model = getModel(  );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( strBookmark, paginator.getPageItems(  ) );

        return model;
    }
}
