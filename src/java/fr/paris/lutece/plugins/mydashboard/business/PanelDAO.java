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
 * This class provides Data Access methods for Panel objects
 */
public final class PanelDAO implements IPanelDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_panel ) FROM mydashboard_panel";
   
    private static final String SQL_QUERY_SELECTALL = "SELECT id_panel, code, title, description FROM mydashboard_panel";
    
    private static final String SQL_QUERY_SELECT =  SQL_QUERY_SELECTALL +" WHERE id_panel = ?";
    private static final String SQL_QUERY_SELECT_BY_CODE =  SQL_QUERY_SELECTALL +" WHERE code = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO mydashboard_panel ( id_panel, code, title, description ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM mydashboard_panel WHERE id_panel = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE mydashboard_panel SET id_panel = ?, code = ?, title = ?, description = ? WHERE id_panel = ?";
     private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_panel FROM mydashboard_panel";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
        daoUtil.executeQuery( );
        int nKey = 1;

        if( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free();
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Panel panel, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        panel.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , panel.getId( ) );
        daoUtil.setString( nIndex++ , panel.getCode( ) );
        daoUtil.setString( nIndex++ , panel.getTitle( ) );
        daoUtil.setString( nIndex++ , panel.getDescription( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Panel load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        Panel panel = null;

        if ( daoUtil.next( ) )
        {
            panel = new Panel();
            int nIndex = 1;
            
            panel.setId( daoUtil.getInt( nIndex++ ) );
            panel.setCode( daoUtil.getString( nIndex++ ) );
            panel.setTitle( daoUtil.getString( nIndex++ ) );
            panel.setDescription( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return panel;
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public Panel loadByCode( String strCode, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin );
        daoUtil.setString( 1 , strCode );
        daoUtil.executeQuery( );
        Panel panel = null;

        if ( daoUtil.next( ) )
        {
            panel = new Panel();
            int nIndex = 1;
            
            panel.setId( daoUtil.getInt( nIndex++ ) );
            panel.setCode( daoUtil.getString( nIndex++ ) );
            panel.setTitle( daoUtil.getString( nIndex++ ) );
            panel.setDescription( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return panel;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Panel panel, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;
        
        daoUtil.setInt( nIndex++ , panel.getId( ) );
        daoUtil.setString( nIndex++ , panel.getCode( ) );
        daoUtil.setString( nIndex++ , panel.getTitle( ) );
        daoUtil.setString( nIndex++ , panel.getDescription( ) );
        daoUtil.setInt( nIndex , panel.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Panel> selectPanelsList( Plugin plugin )
    {
        List<Panel> panelList = new ArrayList<>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Panel panel = new Panel(  );
            int nIndex = 1;
            
            panel.setId( daoUtil.getInt( nIndex++ ) );
            panel.setCode( daoUtil.getString( nIndex++ ) );
            panel.setTitle( daoUtil.getString( nIndex++ ) );
            panel.setDescription( daoUtil.getString( nIndex++ ) );

            panelList.add( panel );
        }

        daoUtil.free( );
        return panelList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdPanelsList( Plugin plugin )
    {
        List<Integer> panelList = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            panelList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return panelList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectPanelsReferenceList( Plugin plugin )
    {
        ReferenceList panelList = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            panelList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return panelList;
    }
}