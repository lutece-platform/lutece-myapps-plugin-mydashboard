CREATE TABLE mydashboard_portlet_panel (
id_portlet int NOT NULL,
id_panel int(11) NOT NULL default '0',
PRIMARY KEY ( id_portlet )
);


UPDATE core_portlet_type
SET create_specific = '/admin/plugins/mydashboard/portlet/create_portlet_mydashboard.html',
modify_specific = '/admin/plugins/mydashboard/portlet/modify_portlet_mydashboard.html'
WHERE id_portlet_type = 'MYDASHBOARD_PORTLET';