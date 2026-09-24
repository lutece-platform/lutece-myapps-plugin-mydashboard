/*
 * Copyright (c) 2002-2026, Mairie de Paris
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
package fr.paris.lutece.plugins.mydashboard.business.portlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTemplate;
import fr.paris.lutece.portal.business.portlet.PortletTemplateHome;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;

/**
 * Renders a dashboard portlet with every shipped FreeMarker template
 */
public class MyDashboardPortletRenderingTest extends LuteceTestCase
{
    private static final String PORTLET_NAME = "MyDashboardPortletRenderingTest";
    private static final String DASHBOARD_CONTENT = "<div class=\"dashboard-test\">dashboard content</div>";
    private static final String MARKER_PORTLET = "portlet-mydashboard";
    private static final int UNKNOWN_TEMPLATE_ID = 99999;

    private MyDashboardPortlet _portlet;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _portlet = new MyDashboardPortlet( );
        _portlet.setPageId( PortalService.getRootPageId( ) );
        _portlet.setStyleId( 0 );
        _portlet.setColumn( 1 );
        _portlet.setOrder( 1 );
        _portlet.setName( PORTLET_NAME );
        _portlet.setStatus( Portlet.STATUS_PUBLISHED );
        _portlet.setDisplayPortletTitle( 0 );
        _portlet.setDeviceDisplayFlags( Portlet.FLAG_DISPLAY_ON_NORMAL_DEVICE | Portlet.FLAG_DISPLAY_ON_LARGE_DEVICE | Portlet.FLAG_DISPLAY_ON_XLARGE_DEVICE );
        MyDashboardPortletHome.getInstance( ).create( _portlet );
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        if ( _portlet != null )
        {
            MyDashboardPortletHome.getInstance( ).remove( _portlet );
        }
        LocalVariables.remove( );
        super.tearDown( );
    }

    /**
     * The template chosen for the portlet is persisted by the core
     */
    @Test
    public void testTemplateIsPersisted( )
    {
        List<PortletTemplate> listTemplates = PortletTemplateHome.findByPortletType( MyDashboardPortletHome.getInstance( ).getPortletTypeId( ) );
        assertEquals( 1, listTemplates.size( ), "the shipped template should be registered in the core for the dashboard portlet type" );

        int nIdTemplate = listTemplates.get( 0 ).getId( );
        _portlet.setIdTemplate( nIdTemplate );
        MyDashboardPortletHome.getInstance( ).update( _portlet );

        assertEquals( nIdTemplate, PortletHome.findByPrimaryKey( _portlet.getId( ) ).getIdTemplate( ), "the chosen template should be persisted" );
        assertTrue( PortletTemplateHome.isTemplateUsed( nIdTemplate ), "the chosen template should be marked as used" );
    }

    /**
     * Every shipped template renders the portlet title, the dashboards content and the device display classes
     */
    @Test
    public void testRenderEveryShippedTemplate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

        List<PortletTemplate> listTemplates = PortletTemplateHome.findByPortletType( MyDashboardPortletHome.getInstance( ).getPortletTypeId( ) );
        assertFalse( listTemplates.isEmpty( ), "at least one template should be registered for the dashboard portlet type" );

        for ( PortletTemplate template : listTemplates )
        {
            _portlet.setIdTemplate( template.getId( ) );
            String strContent = _portlet.renderDashboards( request, List.of( DASHBOARD_CONTENT ) );

            assertTrue( strContent.contains( MARKER_PORTLET ), "template " + template.getTemplatePath( ) + " should render the portlet wrapper" );
            assertTrue( strContent.contains( "portlet_" + _portlet.getId( ) ), "template " + template.getTemplatePath( ) + " should render the portlet id" );
            assertTrue( strContent.contains( PORTLET_NAME ), "template " + template.getTemplatePath( ) + " should render the portlet title" );
            assertTrue( strContent.contains( DASHBOARD_CONTENT ), "template " + template.getTemplatePath( ) + " should render the dashboards content" );
            assertTrue( strContent.contains( "d-none d-md-block" ), "template " + template.getTemplatePath( ) + " should hide the portlet on small devices" );
        }
    }

    /**
     * An unknown template falls back to the default one and a hidden title is not rendered
     */
    @Test
    public void testFallbackToDefaultTemplate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

        _portlet.setIdTemplate( UNKNOWN_TEMPLATE_ID );
        _portlet.setDisplayPortletTitle( 1 );
        String strContent = _portlet.renderDashboards( request, List.of( DASHBOARD_CONTENT ) );

        assertTrue( strContent.contains( MARKER_PORTLET ), "the default template should render the portlet wrapper" );
        assertTrue( strContent.contains( DASHBOARD_CONTENT ), "the default template should render the dashboards content" );
        assertFalse( strContent.contains( PORTLET_NAME ), "a hidden portlet title should not be rendered" );
    }
}
