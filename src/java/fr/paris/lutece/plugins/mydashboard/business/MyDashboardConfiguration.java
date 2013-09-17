package fr.paris.lutece.plugins.mydashboard.business;

/**
 * User configuration of dashboards
 */
public class MyDashboardConfiguration implements Comparable<MyDashboardConfiguration>
{

    private int _nOrder;
    private String _strUserName;
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
     * Get the user name of the user associated with this dashboard
     * configuration
     * @return the user name
     */
    public String getUserName( )
    {
        return _strUserName;
    }

    /**
     * Set the user name of the user associated with this dashboard
     * configuration
     * @param strUserName the user name
     */
    public void setUserName( String strUserName )
    {
        _strUserName = strUserName;
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
}
