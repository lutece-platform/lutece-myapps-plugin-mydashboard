-- liquibase formatted sql
-- changeset mydashboard:init_db_core_mydashboard.sql
-- preconditions onFail:MARK_RAN onError:WARN
DELETE FROM core_portlet_type WHERE id_portlet_type = 'MYDASHBOARD_PORTLET';
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES
('MYDASHBOARD_PORTLET','mydashboard.portlet.myDashboardPortlet.name','plugins/mydashboard/GetCreateMyDashboardPortlet.jsp','plugins/mydashboard/GetModifyMyDashboardPortlet.jsp','fr.paris.lutece.plugins.mydashboard.business.portlet.MyDashboardPortletHome','mydashboard','plugins/mydashboard/DoCreateMyDashboardPortlet.jsp','/admin/portlet/script_create_portlet.html','/admin/plugins/mydashboard/portlet/create_portlet_mydashboard.html','','plugins/mydashboard/DoModifyMyDashboardPortlet.jsp','/admin/portlet/script_modify_portlet.html','/admin/plugins/mydashboard/portlet/modify_portlet_mydashboard.html','');

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'MYDASHBOARD_PANEL_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('MYDASHBOARD_PANEL_MANAGEMENT','mydashboard.adminFeature.ManageMydashboardPanel.name',1,'jsp/admin/plugins/mydashboard/ManageMyDashboardPanel.jsp','mydashboard.adminFeature.ManageMydashboardPanel.description',0,'mydashboard',NULL,NULL,NULL,4);



--
-- FreeMarker template available for the dashboard portlets (2.0.0), registered in the core (Section Template Management feature)
--
-- changeset mydashboard:init_db_core_mydashboard.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_portlet_template WHERE id_portlet_type = 'MYDASHBOARD_PORTLET'
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('MYDASHBOARD_PORTLET', 'Défaut', 'skin/plugins/mydashboard/portlet/mydashboard_portlet.html');
