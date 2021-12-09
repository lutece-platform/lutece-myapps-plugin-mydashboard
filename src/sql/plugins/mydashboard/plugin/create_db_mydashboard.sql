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
id_panel int NOT NULL,
code varchar(50)  default '' NOT NULL,
title varchar(255)  default '' NOT NULL,
description long varchar NULL,
is_default  SMALLINT NOT NULL,
PRIMARY KEY (id_panel)
);

--
-- Structure for table mydashboard_dashboard_association
--



DROP TABLE IF EXISTS mydashboard_dashboard_association;
CREATE TABLE mydashboard_dashboard_association (
id_dashboard_association  int NOT NULL,
id_dashboard varchar(50) default '0' NOT NULL ,
id_panel int default '0' NOT NULL ,
position int default '0' NOT NULL ,
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
id_panel int(11) default '0' NOT NULL ,
PRIMARY KEY ( id_portlet )
);

