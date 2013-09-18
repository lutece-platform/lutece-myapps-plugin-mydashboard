package fr.paris.lutece.plugins.mydashboard.web.portlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.mydashboard.business.portlet.MyDashboardPortlet;
import fr.paris.lutece.plugins.mydashboard.business.portlet.MyDashboardPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Jsp Bean for MyDashboard portlet management
 */
public class MyDashboardPortletJspBean extends PortletJspBean
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -2930449563274775264L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doCreate( HttpServletRequest request )
    {
        MyDashboardPortlet portlet = new MyDashboardPortlet( );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = StringUtils.isNotEmpty( strIdPage ) && StringUtils.isNumeric( strIdPage ) ? Integer
                .parseInt( strIdPage ) : 1;

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        //Portlet creation
        MyDashboardPortletHome.getInstance( ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );

        HtmlTemplate template = getModifyTemplate( portlet );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doModify( HttpServletRequest request )
    {
        // recovers portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );

        int nPortletId = StringUtils.isNotEmpty( strPortletId ) && StringUtils.isNumeric( strPortletId ) ? Integer
                .parseInt( strPortletId ) : 1;

        MyDashboardPortlet portlet = (MyDashboardPortlet) PortletHome.findByPrimaryKey( nPortletId );

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        MyDashboardPortletHome.getInstance( ).update( portlet );

        // displays the page withe the potlet updated
        return getPageUrl( portlet.getPageId( ) );
    }

}
