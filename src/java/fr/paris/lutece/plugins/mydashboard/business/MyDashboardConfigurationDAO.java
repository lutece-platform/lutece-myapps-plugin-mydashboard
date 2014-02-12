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
package fr.paris.lutece.plugins.mydashboard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.UniqueIDGenerator;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of IMyDashboardConfigurationDAO
 */
public class MyDashboardConfigurationDAO implements IMyDashboardConfigurationDAO
{
    private static final String SQL_QUERY_FIND_BY_CONFIG_ID = " SELECT my_dashboard_component_id, id_config, dashboard_order, hide_dashboard FROM mydashboard_configuration WHERE id_config = ? ";
    private static final String SQL_QUERY_INSERT_CONFIGURATION = " INSERT INTO mydashboard_configuration (my_dashboard_component_id, id_config, dashboard_order, hide_dashboard) VALUES (?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE_CONFIGURATION = " UPDATE mydashboard_configuration SET dashboard_order = ?, hide_dashboard = ? WHERE my_dashboard_component_id = ? AND id_config = ? ";
    private static final String SQL_QUERY_REMOVE_BY_CONFIG_ID = " DELETE FROM mydashboard_configuration WHERE id_config = ? ";
    private static final String SQL_QUERY_REMOVE_BY_CONFIG_ID_AND_COMPONENT = " DELETE FROM mydashboard_configuration WHERE id_config = ? AND my_dashboard_component_id = ? ";
    private static final int CONSTANT_SIZE_RANDOM_PART_ID = 1000000;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewConfigId(  )
    {
        return UniqueIDGenerator.getNewId(  ) + (int) ( Math.random(  ) * CONSTANT_SIZE_RANDOM_PART_ID );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyDashboardConfiguration> findByConfigId( String strConfigId, Plugin plugin )
    {
        List<MyDashboardConfiguration> listConfig = new ArrayList<MyDashboardConfiguration>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_CONFIG_ID, plugin );
        daoUtil.setString( 1, strConfigId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MyDashboardConfiguration config = new MyDashboardConfiguration(  );
            config.setMyDashboardComponentId( daoUtil.getString( 1 ) );
            config.setIdConfig( daoUtil.getString( 2 ) );
            config.setOrder( daoUtil.getInt( 3 ) );
            config.setHideDashboard( daoUtil.getBoolean( 4 ) );
            listConfig.add( config );
        }

        daoUtil.free(  );

        return listConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConfiguration( MyDashboardConfiguration config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONFIGURATION, plugin );
        daoUtil.setInt( 1, config.getOrder(  ) );
        daoUtil.setBoolean( 2, config.getHideDashboard(  ) );
        daoUtil.setString( 3, config.getMyDashboardComponentId(  ) );
        daoUtil.setString( 4, config.getIdConfig(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertConfiguration( MyDashboardConfiguration config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONFIGURATION, plugin );
        daoUtil.setString( 1, config.getMyDashboardComponentId(  ) );
        daoUtil.setString( 2, config.getIdConfig(  ) );
        daoUtil.setInt( 3, config.getOrder(  ) );
        daoUtil.setBoolean( 4, config.getHideDashboard(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByConfigId( String strConfigId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BY_CONFIG_ID, plugin );
        daoUtil.setString( 1, strConfigId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByConfigIdAndDashboardId( String strConfigId, String strDashboardId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BY_CONFIG_ID_AND_COMPONENT, plugin );
        daoUtil.setString( 1, strConfigId );
        daoUtil.setString( 2, strDashboardId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
