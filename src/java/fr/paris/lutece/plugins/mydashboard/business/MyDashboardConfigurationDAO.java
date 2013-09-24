package fr.paris.lutece.plugins.mydashboard.business;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.UniqueIDGenerator;
import fr.paris.lutece.util.sql.DAOUtil;


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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewConfigId( )
    {
        return UniqueIDGenerator.getNewId( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyDashboardConfiguration> findByConfigId( String strConfigId, Plugin plugin )
    {
        List<MyDashboardConfiguration> listConfig = new ArrayList<MyDashboardConfiguration>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_CONFIG_ID, plugin );
        daoUtil.setString( 1, strConfigId );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            MyDashboardConfiguration config = new MyDashboardConfiguration( );
            config.setMyDashboardComponentId( daoUtil.getString( 1 ) );
            config.setIdConfig( daoUtil.getString( 2 ) );
            config.setOrder( daoUtil.getInt( 3 ) );
            config.setHideDashboard( daoUtil.getBoolean( 4 ) );
            listConfig.add( config );
        }
        daoUtil.free( );
        return listConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConfiguration( MyDashboardConfiguration config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONFIGURATION, plugin );
        daoUtil.setInt( 1, config.getOrder( ) );
        daoUtil.setBoolean( 2, config.getHideDashboard( ) );
        daoUtil.setString( 3, config.getMyDashboardComponentId( ) );
        daoUtil.setString( 4, config.getIdConfig( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertConfiguration( MyDashboardConfiguration config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CONFIGURATION, plugin );
        daoUtil.setString( 1, config.getMyDashboardComponentId( ) );
        daoUtil.setString( 2, config.getIdConfig( ) );
        daoUtil.setInt( 3, config.getOrder( ) );
        daoUtil.setBoolean( 4, config.getHideDashboard( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByConfigId( String strConfigId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BY_CONFIG_ID, plugin );
        daoUtil.setString( 1, strConfigId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
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
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
