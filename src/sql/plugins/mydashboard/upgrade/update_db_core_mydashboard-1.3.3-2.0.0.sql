-- liquibase formatted sql
-- changeset mydashboard:update_db_core_mydashboard-1.3.3-2.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'core_portlet' AND column_name = 'id_template'
--
-- The dashboard portlets are rendered with a FreeMarker template chosen per portlet among the templates registered in the core
-- for the portlet type (core_portlet_template, core_portlet.id_template, Section Template Management feature).
-- Existing portlets keep id_template = 0 and are rendered with the default template.
--
-- The plugin upgrade scripts run BEFORE the core upgrade script in the same liquibase run (sql/plugins/* sorts before sql/upgrade/*) :
-- the core structures are created here when they do not exist yet, with the very same statements as the core script, which is then skipped.
--
ALTER TABLE core_portlet ADD COLUMN id_template int default 0 NOT NULL;

-- changeset mydashboard:update_db_core_mydashboard-1.3.3-2.0.0.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'core_portlet_template'
CREATE TABLE IF NOT EXISTS core_portlet_template (
	id_template int AUTO_INCREMENT NOT NULL,
	id_portlet_type varchar(50) default NULL,
	description varchar(255) default NULL,
	template_path varchar(255) default NULL,
	PRIMARY KEY (id_template)
);

--
-- Template available for the dashboard portlets
--
-- changeset mydashboard:update_db_core_mydashboard-1.3.3-2.0.0.sql-rev2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_portlet_template WHERE id_portlet_type = 'MYDASHBOARD_PORTLET'
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('MYDASHBOARD_PORTLET', 'Défaut', 'skin/plugins/mydashboard/portlet/mydashboard_portlet.html');
