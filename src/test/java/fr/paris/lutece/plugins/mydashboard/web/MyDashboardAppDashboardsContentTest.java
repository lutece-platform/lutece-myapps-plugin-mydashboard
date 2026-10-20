/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.mydashboard.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mydashboard.service.IMyDashboardComponent;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.security.LuteceUser;
import freemarker.core.HTMLOutputFormat;
import freemarker.core.TemplateHTMLOutputModel;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The dashboard page puts the HTML of the available dashboard components in its model as markup (LUT-33153): the
 * template writes it as HTML, unchanged.
 */
public class MyDashboardAppDashboardsContentTest
{
    private static final String FRAGMENT = "<div class=\"card\"><p>Dossier &lt;b&gt;42&lt;/b&gt;</p></div>";

    /**
     * A dashboard component returning a fixed fragment.
     */
    private static final class FixedComponent extends MyDashboardComponent
    {
        private static final long serialVersionUID = 1L;
        private final String _strId;
        private final String _strHtml;
        private final boolean _bAvailable;

        FixedComponent( String strId, String strHtml, boolean bAvailable )
        {
            _strId = strId;
            _strHtml = strHtml;
            _bAvailable = bAvailable;
        }

        @Override
        public String getDashboardData( HttpServletRequest request )
        {
            return _strHtml;
        }

        @Override
        public String getComponentId( )
        {
            return _strId;
        }

        @Override
        public String getComponentDescription( Locale locale )
        {
            return _strId;
        }

        @Override
        public boolean isAvailable( LuteceUser user )
        {
            return _bAvailable;
        }
    }

    /**
     * The fragments of the available components are markup values holding their HTML unchanged, in order; an empty
     * fragment is an empty markup value; an unavailable component is left out.
     *
     * @throws Exception
     *             if a markup value cannot be read
     */
    @Test
    public void testAvailableComponentsAreMarkup( ) throws Exception
    {
        List<IMyDashboardComponent> listComponents = List.of( new FixedComponent( "a", FRAGMENT, true ), new FixedComponent( "b", "<p>hidden</p>", false ),
                new FixedComponent( "c", null, true ) );

        List<?> listContent = MyDashboardApp.getDashboardsContent( listComponents, null, null );

        assertEquals( 2, listContent.size( ) );
        assertTrue( listContent.get( 0 ) instanceof TemplateHTMLOutputModel );
        assertEquals( FRAGMENT, HTMLOutputFormat.INSTANCE.getMarkupString( (TemplateHTMLOutputModel) listContent.get( 0 ) ) );
        assertEquals( "", HTMLOutputFormat.INSTANCE.getMarkupString( (TemplateHTMLOutputModel) listContent.get( 1 ) ) );
    }
}
