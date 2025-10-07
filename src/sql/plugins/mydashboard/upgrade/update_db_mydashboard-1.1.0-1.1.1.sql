-- liquibase formatted sql
-- changeset mydashboard:update_db_mydashboard-1.1.0-1.1.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
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
