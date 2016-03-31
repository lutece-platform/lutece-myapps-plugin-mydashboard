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
 package fr.paris.lutece.plugins.mydashboard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for DashboardAssociation objects
 */
public final class DashboardAssociationHome
{
    // Static variable pointed at the DAO instance
    private static IDashboardAssociationDAO _dao = SpringContextService.getBean( "mydashboard.dashboardAssociationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "mydashboard" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DashboardAssociationHome(  )
    {
    }

    /**
     * Create an instance of the dashboardAssociation class
     * @param dashboardAssociation The instance of the DashboardAssociation which contains the informations to store
     * @return The  instance of dashboardAssociation which has been created with its primary key.
     */
    public static DashboardAssociation create( DashboardAssociation dashboardAssociation )
    {
        _dao.insert( dashboardAssociation, _plugin );

        return dashboardAssociation;
    }

    /**
     * Update of the dashboardAssociation which is specified in parameter
     * @param dashboardAssociation The instance of the DashboardAssociation which contains the data to store
     * @return The instance of the  dashboardAssociation which has been updated
     */
    public static DashboardAssociation update( DashboardAssociation dashboardAssociation )
    {
        _dao.store( dashboardAssociation, _plugin );

        return dashboardAssociation;
    }

    /**
     * Remove the dashboardAssociation whose identifier is specified in parameter
     * @param nKey The dashboardAssociation Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a dashboardAssociation whose identifier is specified in parameter
     * @param nKey The dashboardAssociation primary key
     * @return an instance of DashboardAssociation
     */
    public static DashboardAssociation findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the dashboardAssociation objects and returns them as a list
     * @return the list which contains the data of all the dashboardAssociation objects
     */
    public static List<DashboardAssociation> getDashboardAssociationsList( )
    {
        return _dao.selectDashboardAssociationsList( _plugin );
    }
    
    /**
     * return the number of association
     * @return the association number
     */
    public static int  getCountDashboardAssociation( )
    {
        return _dao.selectCountDashboardAssociations( _plugin );
    }
    
    /**
     * Load the data of all the dashboardAssociation objects and returns them as a list
     * @param nIdPanel the panel id
     * @return the list which contains the data of all the dashboardAssociation objects
     */
    public static List<DashboardAssociation> getDashboardAssociationsListByIdPanel(int nIdPanel )
    {
        return _dao.selectDashboardAssociationsListByIdPanel(nIdPanel, _plugin );
    }
    
    /**
     * Load the id of all the dashboardAssociation objects and returns them as a list
     * @return the list which contains the id of all the dashboardAssociation objects
     */
    public static List<Integer> getIdDashboardAssociationsList( )
    {
        return _dao.selectIdDashboardAssociationsList( _plugin );
    }
    
    /**
     * Load the data of all the dashboardAssociation objects and returns them as a referenceList
     * @return the referenceList which contains the data of all the dashboardAssociation objects
     */
    public static ReferenceList getDashboardAssociationsReferenceList( )
    {
        return _dao.selectDashboardAssociationsReferenceList(_plugin );
    }
}

