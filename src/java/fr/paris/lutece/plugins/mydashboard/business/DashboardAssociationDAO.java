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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for DashboardAssociation objects
 */
public final class DashboardAssociationDAO implements IDashboardAssociationDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_dashboard_association ) FROM mydashboard_dashboard_association";
    private static final String SQL_QUERY_SELECT_COUNT_ALL = "SELECT count(id_dashboard) FROM mydashboard_dashboard_association";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_dashboard_association, id_dashboard, id_panel, position FROM mydashboard_dashboard_association";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE id_dashboard_association = ?";
    private static final String SQL_QUERY_SELECT_BY_PANEL = SQL_QUERY_SELECTALL +
        " WHERE id_panel = ? ORDER BY position DESC ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mydashboard_dashboard_association ( id_dashboard_association, id_dashboard, id_panel, position ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mydashboard_dashboard_association WHERE id_dashboard_association = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mydashboard_dashboard_association SET id_dashboard_association = ?, id_dashboard = ?, id_panel = ?, position = ? WHERE id_dashboard_association = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_dashboard_association FROM mydashboard_dashboard_association";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DashboardAssociation dashboardAssociation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        dashboardAssociation.setId( newPrimaryKey( plugin ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, dashboardAssociation.getId(  ) );
        daoUtil.setString( nIndex++, dashboardAssociation.getIdDashboard(  ) );
        daoUtil.setInt( nIndex++, dashboardAssociation.getIdPanel(  ) );
        daoUtil.setInt( nIndex++, dashboardAssociation.getPosition(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DashboardAssociation load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        DashboardAssociation dashboardAssociation = null;

        if ( daoUtil.next(  ) )
        {
            dashboardAssociation = new DashboardAssociation(  );

            int nIndex = 1;

            dashboardAssociation.setId( daoUtil.getInt( nIndex++ ) );
            dashboardAssociation.setIdDashboard( daoUtil.getString( nIndex++ ) );
            dashboardAssociation.setIdPanel( daoUtil.getInt( nIndex++ ) );
            dashboardAssociation.setPosition( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free(  );

        return dashboardAssociation;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DashboardAssociation dashboardAssociation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, dashboardAssociation.getId(  ) );
        daoUtil.setString( nIndex++, dashboardAssociation.getIdDashboard(  ) );
        daoUtil.setInt( nIndex++, dashboardAssociation.getIdPanel(  ) );
        daoUtil.setInt( nIndex++, dashboardAssociation.getPosition(  ) );
        daoUtil.setInt( nIndex, dashboardAssociation.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DashboardAssociation> selectDashboardAssociationsList( Plugin plugin )
    {
        List<DashboardAssociation> dashboardAssociationList = new ArrayList<DashboardAssociation>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DashboardAssociation dashboardAssociation = new DashboardAssociation(  );
            int nIndex = 1;

            dashboardAssociation.setId( daoUtil.getInt( nIndex++ ) );
            dashboardAssociation.setIdDashboard( daoUtil.getString( nIndex++ ) );
            dashboardAssociation.setIdPanel( daoUtil.getInt( nIndex++ ) );
            dashboardAssociation.setPosition( daoUtil.getInt( nIndex++ ) );

            dashboardAssociationList.add( dashboardAssociation );
        }

        daoUtil.free(  );

        return dashboardAssociationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DashboardAssociation> selectDashboardAssociationsListByIdPanel( int nIdPanel, Plugin plugin )
    {
        List<DashboardAssociation> dashboardAssociationList = new ArrayList<DashboardAssociation>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PANEL, plugin );
        daoUtil.setInt( 1, nIdPanel );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DashboardAssociation dashboardAssociation = new DashboardAssociation(  );
            int nIndex = 1;

            dashboardAssociation.setId( daoUtil.getInt( nIndex++ ) );
            dashboardAssociation.setIdDashboard( daoUtil.getString( nIndex++ ) );
            dashboardAssociation.setIdPanel( daoUtil.getInt( nIndex++ ) );
            dashboardAssociation.setPosition( daoUtil.getInt( nIndex++ ) );

            dashboardAssociationList.add( dashboardAssociation );
        }

        daoUtil.free(  );

        return dashboardAssociationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int selectCountDashboardAssociations( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COUNT_ALL, plugin );
        daoUtil.executeQuery(  );

        int nCount = 0;

        if ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDashboardAssociationsList( Plugin plugin )
    {
        List<Integer> dashboardAssociationList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            dashboardAssociationList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return dashboardAssociationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDashboardAssociationsReferenceList( Plugin plugin )
    {
        ReferenceList dashboardAssociationList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            dashboardAssociationList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return dashboardAssociationList;
    }
}
