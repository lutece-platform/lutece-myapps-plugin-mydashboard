DROP TABLE IF EXISTS mydashboard_configuration;
CREATE TABLE mydashboard_configuration (
	my_dashboard_component_id VARCHAR(50) NOT NULL,
	id_config VARCHAR(255) NOT NULL,
	dashboard_order INT NOT NULL,
	hide_dashboard SMALLINT NOT NULL,
	PRIMARY KEY (my_dashboard_component_id, id_config)
);

--
-- Structure for table mydashboard_panel
--

DROP TABLE IF EXISTS mydashboard_panel;
CREATE TABLE mydashboard_panel (
id_panel int(6) NOT NULL,
code varchar(50) NOT NULL default '',
title varchar(255) NOT NULL default '',
description long varchar NULL,
is_default  SMALLINT NOT NULL,
PRIMARY KEY (id_panel)
);

--
-- Structure for table mydashboard_dashboard_association
--



DROP TABLE IF EXISTS mydashboard_dashboard_association;
CREATE TABLE mydashboard_dashboard_association (
id_dashboard_association  int(6) NOT NULL,
id_dashboard varchar(50) NOT NULL default '0',
id_panel int(11) NOT NULL default '0',
position int(11) NOT NULL default '0',
PRIMARY KEY (id_dashboard_association)
);

ALTER TABLE mydashboard_dashboard_association ADD CONSTRAINT fk_id_panel FOREIGN KEY (id_panel)
      REFERENCES mydashboard_panel (id_panel)  ON DELETE RESTRICT ON UPDATE RESTRICT ;

      
--
-- Structure for table mydashboard_portlet_panel
--



DROP TABLE IF EXISTS mydashboard_portlet_panel;
CREATE TABLE mydashboard_portlet_panel (
id_portlet int NOT NULL,
id_panel int(11) NOT NULL default '0',
PRIMARY KEY ( id_portlet )
);

