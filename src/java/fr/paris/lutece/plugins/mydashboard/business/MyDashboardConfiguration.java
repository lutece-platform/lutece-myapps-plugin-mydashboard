package fr.paris.lutece.plugins.mydashboard.business;

import org.apache.commons.lang.StringUtils;


/**
 * User configuration of dashboards
 */
public class MyDashboardConfiguration implements Comparable<MyDashboardConfiguration>
{

    private int _nOrder;
    private String _strIdConfig;
    private String _strMyDashboardComponentId;
    private boolean _bHideDashboard;

    /**
     * Returns the Order
     * @return The Order
     */
    public int getOrder( )
    {
        return _nOrder;
    }

    /**
     * Sets the Order
     * @param nOrder The Order
     */
    public void setOrder( int nOrder )
    {
        _nOrder = nOrder;
    }

    /**
     * Get the id of the config of this dashboard configuration
     * @return the user name
     */
    public String getIdConfig( )
    {
        return _strIdConfig;
    }

    /**
     * Set the id of the config of this dashboard configuration
     * @param strIdConfig the id of the config
     */
    public void setIdConfig( String strIdConfig )
    {
        _strIdConfig = strIdConfig;
    }

    /**
     * Get the id of the dashboard component
     * @return The id of the dashboard component
     */
    public String getMyDashboardComponentId( )
    {
        return _strMyDashboardComponentId;
    }

    /**
     * Set the id of the dashboard component
     * @param strMyDashboardComponentId The id of the dashboard component
     */
    public void setMyDashboardComponentId( String strMyDashboardComponentId )
    {
        _strMyDashboardComponentId = strMyDashboardComponentId;
    }

    /**
     * Check if the dashboard should be hidden for the associated user or not
     * @return True if the dashboard should be hidden for this user, false
     *         otherwise
     */
    public boolean getHideDashboard( )
    {
        return _bHideDashboard;
    }

    /**
     * Set the visibility of the dashboard for the associated user
     * @param bHideDashboard True if the dashboard should be hidden for this
     *            user, false otherwise
     */
    public void setHideDashboard( boolean bHideDashboard )
    {
        this._bHideDashboard = bHideDashboard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo( MyDashboardConfiguration o )
    {
        return o != null ? this.getOrder( ) - o.getOrder( ) : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        return o instanceof MyDashboardConfiguration ? StringUtils.equals( this.getMyDashboardComponentId( ),
                ( (MyDashboardConfiguration) o ).getMyDashboardComponentId( ) ) : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode( )
    {
        return getMyDashboardComponentId( ) != null ? getMyDashboardComponentId( ).hashCode( ) : 0;
    }
}
