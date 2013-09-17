package fr.paris.lutece.plugins.mydashboard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of IMyDashboardConfigurationDAO
 */
public class MyDashboardConfigurationDAO implements IMyDashboardConfigurationDAO
{
    private static final String SQL_QUERY_FIND_BY_USER_NAME = " SELECT my_dashboard_component_id, user_id, dashboard_order, hide_dashboard FROM mydashboard_configuration WHERE user_id = ? ";
    private static final String SQL_QUERY_INSERT_CONFIGURATION = " INSERT INTO mydashboard_configuration (my_dashboard_component_id, user_id, dashboard_order, hide_dashboard) VALUES (?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE_CONFIGURATION = " UPDATE mydashboard_configuration SET dashboard_order = ?, hide_dashboard = ? WHERE my_dashboard_component_id = ? AND user_id = ? ";
    private static final String SQL_QUERY_REMOVE_BY_USER_NAME = " DELETE FROM mydashboard_configuration WHERE user_id = ? ";
    private static final String SQL_QUERY_REMOVE_BY_USER_NAME_AND_COMPONENT = " DELETE FROM mydashboard_configuration WHERE user_id = ? AND my_dashboard_component_id = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MyDashboardConfiguration> findByUserName( String strUserName, Plugin plugin )
    {
        List<MyDashboardConfiguration> listConfig = new ArrayList<MyDashboardConfiguration>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_USER_NAME, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            MyDashboardConfiguration config = new MyDashboardConfiguration( );
            config.setMyDashboardComponentId( daoUtil.getString( 1 ) );
            config.setUserName( daoUtil.getString( 2 ) );
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
        daoUtil.setString( 4, config.getUserName( ) );
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
        daoUtil.setString( 2, config.getUserName( ) );
        daoUtil.setInt( 3, config.getOrder( ) );
        daoUtil.setBoolean( 4, config.getHideDashboard( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByUserName( String strUserName, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BY_USER_NAME, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByUserNameAndDashboardId( String strUserName, String strDashboardId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BY_USER_NAME_AND_COMPONENT, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.setString( 2, strDashboardId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
