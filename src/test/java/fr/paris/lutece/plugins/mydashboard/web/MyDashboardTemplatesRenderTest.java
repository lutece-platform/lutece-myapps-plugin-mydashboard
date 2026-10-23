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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mydashboard.business.MyDashboardConfiguration;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the plugin-mydashboard front office pages with HTML auto-escaping (LUT-33153): the HTML of the dashboard
 * components, marked with HtmlMarkup by plugin-mydashboard, keeps its tags in the dashboard page and the dashboard
 * portlet; a fragment left as a String prints as text; the data around it (portlet name, component descriptions, ids,
 * nickname) is escaped. The core macros the templates call are replaced by stand-ins that write their arguments (escaped)
 * and their nested content: only the FreeMarker engine is needed.
 */
public class MyDashboardTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_DASHBOARDS = "skin/plugins/mydashboard/get_dashboards.html";
    private static final String TEMPLATE_MODIFY_DASHBOARDS = "skin/plugins/mydashboard/modify_dashboards.html";
    private static final String TEMPLATE_PORTLET = "skin/plugins/mydashboard/portlet/mydashboard_portlet.html";
    private static final String TEMPLATE_NICKNAME = "skin/plugins/mydashboard/modules/nickname/mydashboardcomponent/mydashboardcomponent_nickname.html";

    /** Stand-ins of the core macros the templates call. cBtn writes its params, which must be a map. */
    private static final String MACROS = "macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cContainer rest...><div class=\"container\"><#nested></div></#macro>"
            + "<#macro cRow rest...><div class=\"row\"><#nested></div></#macro><#macro cCol rest...><div class=\"col\"><#nested></div></#macro>"
            + "<#macro cTitle level=2 rest...><h${level}><#nested></h${level}></#macro><#macro cIcon name='' rest...><i class=\"ti ti-${name}\"></i></#macro>"
            + "<#macro cLink href='' label='' rest...><a href=\"${href}\">${label}<#nested></a></#macro>"
            + "<#macro cCardLayout rest...><div class=\"cards\"><#nested></div></#macro>"
            + "<#macro cCard title='' rest...><div class=\"card\"><h3>${title}</h3><#nested></div></#macro><#macro cText rest...><p><#nested></p></#macro>"
            + "<#macro cForm action='' rest...><form action=\"${action}\"><#nested></form></#macro>"
            + "<#macro cField label='' rest...><label>${label}</label><#nested></#macro>"
            + "<#macro cInput name='' value='' type='text' rest...><input type=\"${type}\" name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro chList rest...><ul><#nested></ul></#macro><#macro chItem rest...><li><#nested></li></#macro>"
            + "<#macro cInline rest...><span><#nested></span></#macro><#macro cBadge rest...><b class=\"badge\"><#nested></b></#macro>"
            + "<#macro cBlock rest...><div><#nested></div></#macro>"
            + "<#macro cCheckbox name='' checked=false label='' rest...><input type=\"checkbox\" name=\"${name}\"<#if checked> checked</#if>></#macro>"
            + "<#macro cFormCheck name='' checked=false label='' rest...><input type=\"checkbox\" name=\"${name}\"<#if checked> checked</#if>><label>${label} <#nested></label></#macro>"
            + "<#macro cBtn label='' params={} rest...><button<#list params as key, param> ${key}=\"${param}\"</#list>>${label}<#nested></button></#macro>"
            + "<#macro cTpl rest...><#nested></#macro>"
            + "<#macro cSection id='' class='' rest...><section id=\"${id}\" class=\"${class}\"><#nested></section></#macro>";

    private static final String FRAGMENT = "<div class=\"card\"><h3 class=\"card-title\">Mes démarches</h3>"
            + "<p>Dossier &lt;b&gt;42&lt;/b&gt;</p><a href=\"jsp/site/Portal.jsp?page=demarches&amp;id=42\">Voir</a></div>";
    private static final String FRAGMENT_AS_TEXT = "&lt;div class=&quot;card&quot;&gt;";
    private static final String HOSTILE = "Jean <b>x</b> \"q\" </textarea><script>alert(1)</script>";
    private static final String HOSTILE_ESCAPED = "Jean &lt;b&gt;x&lt;/b&gt; &quot;q&quot; &lt;/textarea&gt;&lt;script&gt;alert(1)&lt;/script&gt;";

    /**
     * Renders a template of the plugin with the core configuration of the output format and of the URL escaping, and
     * the macro stand-ins.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        // as AbstractFreeMarkerTemplateService: ?url takes no charset argument
        cfg.setURLEscapingCharset( "UTF-8" );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                macros, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * A dashboard configuration line.
     *
     * @param strComponentId
     *            the component id
     * @param nOrder
     *            the order
     * @param bHide
     *            true if the dashboard is hidden
     * @return the configuration
     */
    private static MyDashboardConfiguration config( String strComponentId, int nOrder, boolean bHide )
    {
        MyDashboardConfiguration config = new MyDashboardConfiguration( );
        config.setMyDashboardComponentId( strComponentId );
        config.setOrder( nOrder );
        config.setHideDashboard( bHide );

        return config;
    }

    /**
     * The dashboard page writes the HTML of the components, marked by MyDashboardApp, as HTML.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testDashboardPageKeepsTheComponentsHtml( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "listDashboardsContent", HtmlMarkup.ofAll( List.of( FRAGMENT, "" ) ) );
        String strHtml = render( TEMPLATE_DASHBOARDS, model );

        assertTrue( strHtml.contains( FRAGMENT ), strHtml );
        assertFalse( strHtml.contains( FRAGMENT_AS_TEXT ), strHtml );
    }

    /**
     * A component fragment left as a String, as plugin-mydashboard put it before LUT-33153, prints as text: the reason of
     * the HtmlMarkup.ofAll of the consumer.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testDashboardFragmentLeftAsStringPrintsAsText( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "listDashboardsContent", List.of( FRAGMENT ) );
        String strHtml = render( TEMPLATE_DASHBOARDS, model );

        assertFalse( strHtml.contains( FRAGMENT ), strHtml );
        assertTrue( strHtml.contains( FRAGMENT_AS_TEXT ), strHtml );
    }

    /**
     * The dashboard portlet writes the HTML of the components as HTML and escapes its name.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testPortletKeepsTheComponentsHtmlAndEscapesItsName( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "portlet_id", 7 );
        model.put( "device_class", "d-none d-md-block" );
        model.put( "portlet_name", HOSTILE );
        model.put( "listDashboardsContent", HtmlMarkup.ofAll( List.of( FRAGMENT ) ) );
        String strHtml = render( TEMPLATE_PORTLET, model );

        assertTrue( strHtml.contains( FRAGMENT ), strHtml );
        assertTrue( strHtml.contains( HOSTILE_ESCAPED ), strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
        assertTrue( strHtml.contains( "id=\"portlet_7\"" ), strHtml );
    }

    /**
     * The configuration page renders (a captured block given to ?boolean failed under auto-escaping), checks the
     * displayed dashboards only, URL-encodes the component id in the move links, escapes the descriptions and gives
     * the back button its name through a params map.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testModifyDashboardsPage( ) throws Exception
    {
        String strOddId = "mydashboard.odd&x=\"<y>";
        Map<String, Object> model = new HashMap<>( );
        model.put( "listDashboardsConfig", List.of( config( "mydashboard.shown", 1, false ), config( strOddId, 2, true ) ) );
        model.put( "listComponentsDescription", Map.of( "mydashboard.shown", HOSTILE, strOddId, "Mon pseudonyme" ) );
        String strHtml = render( TEMPLATE_MODIFY_DASHBOARDS, model );

        assertTrue( strHtml.contains( "name=\"mydashboard.shown_display\" checked>" ), strHtml );
        assertTrue( strHtml.contains( "name=\"mydashboard.odd&amp;x=&quot;&lt;y&gt;_display\">" ), strHtml );
        assertTrue( strHtml.contains( "form=\"move-up-mydashboard.odd&amp;x=&quot;&lt;y&gt;\"" ), strHtml );
        assertTrue( strHtml.contains( HOSTILE_ESCAPED ), strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"back\">" ), strHtml );
    }

    /**
     * The nickname component escapes the nickname in the value of its field.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNicknameIsEscaped( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "nickname", HOSTILE );
        String strHtml = render( TEMPLATE_NICKNAME, model );

        assertTrue( strHtml.contains( "value=\"" + HOSTILE_ESCAPED + "\"" ), strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
    }
}
