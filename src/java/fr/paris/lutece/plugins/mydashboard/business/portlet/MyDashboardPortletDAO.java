package fr.paris.lutece.plugins.mydashboard.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;


/**
 * DAO for MyDashboard portlets
 */
public class MyDashboardPortletDAO implements IPortletInterfaceDAO
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Portlet portlet )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nPortletId )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Portlet load( int nPortletId )
    {
        MyDashboardPortlet myDashboardPortlet = new MyDashboardPortlet( );
        myDashboardPortlet.setId( nPortletId );
        return myDashboardPortlet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Portlet portlet )
    {
        // Do nothing
    }

}
