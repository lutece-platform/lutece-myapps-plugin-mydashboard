package fr.paris.lutece.plugins.mydashboard.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * Home for portlet
 */
public class MyDashboardPortletHome extends PortletHome
{
    private static MyDashboardPortletHome _instance;
    IPortletInterfaceDAO _dao = SpringContextService.getBean( "mydashboard.myDashboardPortletDAO" );

    /**
     * Get the instance of the home
     * @return The instance of the hom
     */
    public static MyDashboardPortletHome getInstance( )
    {
        if ( _instance == null )
        {
            _instance = new MyDashboardPortletHome( );
        }
        return _instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPortletInterfaceDAO getDAO( )
    {
        return _dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPortletTypeId( )
    {
        String strCurrentClassName = this.getClass( ).getName( );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

}
