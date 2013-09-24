DROP TABLE IF EXISTS mydashboard_configuration;
CREATE TABLE mydashboard_configuration (
	my_dashboard_component_id VARCHAR(50) NOT NULL,
	id_config VARCHAR(255) NOT NULL,
	dashboard_order INT NOT NULL,
	hide_dashboard SMALLINT NOT NULL,
	PRIMARY KEY (my_dashboard_component_id, user_id)
);
