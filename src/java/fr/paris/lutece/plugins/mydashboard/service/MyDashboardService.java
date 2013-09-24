/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.mydashboard.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mydashboard.business.IMyDashboardConfigurationDAO;
import fr.paris.lutece.plugins.mydashboard.business.MyDashboardConfiguration;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.LocalVariables;


/**
 * Dashboard Service
 */
public final class MyDashboardService
{
    private static final String SESSION_LIST_DASHBOARD = "mydashboard.sessionListMyDashboard";
    private static final String SESSION_LIST_DASHBOARD_CONFIG = "mydashboard.sessionListMyDashboardConfig";

    private static final String PARAMETER_CONFIG_ID = "mydashboard.dashboardConfigId";

    private static MyDashboardService _singleton = new MyDashboardService( );
    private IMyDashboardConfigurationDAO _myDashboardComponentDAO = SpringContextService
            .getBean( "mydashboard.myDashboardConfigurationDAO" );

    /**
     * Private Constructor
     */
    private MyDashboardService( )
    {
    }

    /**
     * Return the unique instance
     * @return The instance
     */
    public static MyDashboardService getInstance( )
    {
        return _singleton;
    }

    /**
     * Get the list of MyDashboardComponent
     * @return The list of MyDashboard components
     */
    public List<IMyDashboardComponent> getMyDashboardComponentsList( )
    {
        return new ArrayList<IMyDashboardComponent>( SpringContextService.getBeansOfType( IMyDashboardComponent.class ) );
    }

    /**
     * Get the id of the dashboard configuration associated with a user
     * @param user The user to get the id of the dashboard configuration
     *            of
     * @return The id of the dashboard configuration
     */
    public String getUserConfigId( LuteceUser user )
    {
        String strConfigId;
        if ( LocalVariables.getRequest( ) != null )
        {
            strConfigId = (String) LocalVariables.getRequest( ).getSession( ).getAttribute( PARAMETER_CONFIG_ID );
            if ( strConfigId != null )
            {
                return strConfigId;
            }
        }
        strConfigId = UserPreferencesService.instance( ).get( user.getName( ), PARAMETER_CONFIG_ID, null );
        if ( strConfigId == null )
        {
            strConfigId = _myDashboardComponentDAO.getNewConfigId( );
            UserPreferencesService.instance( ).put( user.getName( ), PARAMETER_CONFIG_ID, strConfigId );
        }
        if ( LocalVariables.getRequest( ) != null )
        {
            LocalVariables.getRequest( ).getSession( ).setAttribute( PARAMETER_CONFIG_ID, strConfigId );
        }
        return strConfigId;
    }

    /**
     * Get the list of MyDashboardConfiguration associated with a given user
     * @param user The user to get the configurations of
     * @return The list of MyDashboardConfiguration. Note that the returned list
     *         is saved in the session if this is a request context
     */
    public List<MyDashboardConfiguration> getUserConfig( LuteceUser user )
    {
        List<MyDashboardConfiguration> listDashboardConfigs = getMyDashboardConfigListFromSession( );
        if ( listDashboardConfigs != null )
        {
            return listDashboardConfigs;
        }
        String strConfigId = getUserConfigId( user );
        listDashboardConfigs = _myDashboardComponentDAO.findByConfigId( strConfigId, MyDashboardPlugin.getPlugin( ) );
        if ( listDashboardConfigs.size( ) == 0 )
        {
            // If there is no dash board configured, we generate the configuration
            List<IMyDashboardComponent> listDashboardComponents = getMyDashboardComponentsList( );
            Collections.sort( listDashboardComponents );
            int nOrder = 1;
            for ( IMyDashboardComponent dashboardComponent : listDashboardComponents )
            {
                MyDashboardConfiguration config = new MyDashboardConfiguration( );
                config.setMyDashboardComponentId( dashboardComponent.getComponentId( ) );
                config.setIdConfig( strConfigId );
                config.setOrder( nOrder++ );
                config.setHideDashboard( false );
                _myDashboardComponentDAO.insertConfiguration( config, MyDashboardPlugin.getPlugin( ) );
                listDashboardConfigs.add( config );
            }
        }
        else
        {
            Collections.sort( listDashboardConfigs );
        }
        saveMyDashboardConfigListInSession( listDashboardConfigs );
        return listDashboardConfigs;
    }

    /**
     * Get the list of dash boards components associated with a given
     * configuration id.
     * The list is sorted with the order of each component in the configuration,
     * and contains only enabled and displayed components
     * @param user The user to get the dash board components of
     * @return The list of dash boards components
     */
    public List<IMyDashboardComponent> getDashboardComponentListFromUser( LuteceUser user )
    {
        List<IMyDashboardComponent> listComponents = getMyDashboardListFromSession( );
        if ( listComponents != null )
        {
            return listComponents;
        }

        List<IMyDashboardComponent> listComponentsSorted;
        List<MyDashboardConfiguration> listUserConfig = getUserConfig( user );

        listComponents = getMyDashboardComponentsList( );
        listComponentsSorted = new ArrayList<IMyDashboardComponent>( listComponents.size( ) );

        for ( MyDashboardConfiguration config : listUserConfig )
        {
            for ( IMyDashboardComponent component : listComponents )
            {
                if ( StringUtils.equals( config.getMyDashboardComponentId( ), component.getComponentId( ) ) )
                {
                    if ( !config.getHideDashboard( ) )
                    {
                        listComponentsSorted.add( component );
                    }
                    listComponents.remove( component );
                    break;
                }
            }
        }
        if ( listComponents.size( ) > 0 )
        {
            AppLogService
                    .error( "MyDashboard : dashboard(s) found without user configuration - will proceed to the creation of the configuration" );
            Collections.sort( listComponents );
            int nLastUsedOrder = listUserConfig.size( ) > 0 ? listUserConfig.get( listUserConfig.size( ) - 1 )
                    .getOrder( ) + 1 : 1;
            String strConfigId = getUserConfigId( user );
            for ( IMyDashboardComponent component : listComponents )
            {
                MyDashboardConfiguration config = new MyDashboardConfiguration( );
                config.setMyDashboardComponentId( component.getComponentId( ) );
                config.setIdConfig( strConfigId );
                config.setOrder( nLastUsedOrder++ );
                config.setHideDashboard( false );
                _myDashboardComponentDAO.insertConfiguration( config, MyDashboardPlugin.getPlugin( ) );
                listUserConfig.add( config );
            }
            saveMyDashboardConfigListInSession( listUserConfig );
            listComponentsSorted.addAll( listComponents );
        }

        saveMyDashboardListInSession( listComponentsSorted );
        return listComponentsSorted;
    }

    /**
     * Delete a user configuration from its user, and reset the list of
     * configurations saved in session if any
     * @param user The user to remove the configuration of
     */
    public void deleteConfigByUser( LuteceUser user )
    {
        _myDashboardComponentDAO.removeByConfigId( getUserConfigId( user ), MyDashboardPlugin.getPlugin( ) );
        saveMyDashboardConfigListInSession( null );
    }

    /**
     * Update a dashboard configuration
     * @param myDashboardsConfig The configuration to update
     * @param bCleanCache True to update the dashboard configuration cache,
     *            false otherwise
     */
    public void updateConfig( MyDashboardConfiguration myDashboardsConfig, boolean bCleanCache )
    {
        _myDashboardComponentDAO.updateConfiguration( myDashboardsConfig, MyDashboardPlugin.getPlugin( ) );
        if ( bCleanCache )
        {
            saveMyDashboardConfigListInSession( null );
        }
    }

    /**
     * Update a list of configuration
     * @param listMyDashboardsConfig The list of configuration to update
     */
    public void updateConfigList( List<MyDashboardConfiguration> listMyDashboardsConfig )
    {
        for ( MyDashboardConfiguration config : listMyDashboardsConfig )
        {
            _myDashboardComponentDAO.updateConfiguration( config, MyDashboardPlugin.getPlugin( ) );
        }

        saveMyDashboardConfigListInSession( listMyDashboardsConfig );
        saveMyDashboardListInSession( null );
    }

    /**
     * Save a list of dashboards in session. If this is not a request context,
     * then do nothing
     * @param listMyDashboards The list of dashboards to save
     */
    private void saveMyDashboardListInSession( List<IMyDashboardComponent> listMyDashboards )
    {
        if ( LocalVariables.getRequest( ) != null )
        {
            HttpServletRequest request = LocalVariables.getRequest( );
            request.getSession( ).setAttribute( SESSION_LIST_DASHBOARD, listMyDashboards );
        }
    }

    /**
     * Get the list of dashboards from the session.
     * @return The list of dashboards from the session. If this is not a request
     *         context, or if there is no dashboard list saved in session,
     *         return null.
     */
    @SuppressWarnings( "unchecked" )
    private List<IMyDashboardComponent> getMyDashboardListFromSession( )
    {
        if ( LocalVariables.getRequest( ) != null )
        {
            HttpServletRequest request = LocalVariables.getRequest( );
            return (List<IMyDashboardComponent>) request.getSession( ).getAttribute( SESSION_LIST_DASHBOARD );
        }
        return null;
    }

    /**
     * Save a list of dashboard configurations in session. If this is not a
     * request context, then do nothing
     * @param listMyDashboardsConfig The list of dashboards to save
     */
    private void saveMyDashboardConfigListInSession( List<MyDashboardConfiguration> listMyDashboardsConfig )
    {
        if ( LocalVariables.getRequest( ) != null )
        {
            HttpServletRequest request = LocalVariables.getRequest( );
            request.getSession( ).setAttribute( SESSION_LIST_DASHBOARD_CONFIG, listMyDashboardsConfig );
        }
    }

    /**
     * Get the list of dashboard configurations from the session.
     * @return The list of dashboard configurations from the session. If this is
     *         not a request context, or if there is no dashboard configuration
     *         list saved in session, return null.
     */
    @SuppressWarnings( "unchecked" )
    private List<MyDashboardConfiguration> getMyDashboardConfigListFromSession( )
    {
        if ( LocalVariables.getRequest( ) != null )
        {
            HttpServletRequest request = LocalVariables.getRequest( );
            return (List<MyDashboardConfiguration>) request.getSession( ).getAttribute( SESSION_LIST_DASHBOARD_CONFIG );
        }
        return null;
    }
}
