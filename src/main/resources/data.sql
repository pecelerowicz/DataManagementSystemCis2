delete from info
delete from user
delete from info_difr
delete from info_test

--this probably should be done programmatically
ALTER TABLE info ADD CONSTRAINT not_more_than_one_specific_info CHECK ((info_difr_id IS NULL AND info_test_id IS NULL) OR (info_difr_id IS NULL AND info_test_id IS NOT NULL) OR (info_difr_id IS NOT NULL AND info_test_id IS NULL))

insert into user (created, email, enabled, roles, password, username) values ("2021-06-08 08:49:10", "michal@gazeta.pl" , true, "ROLE_ADMIN", "$2a$10$OLwgc52vrQxWMgHz6WzPSepHYOt7WKI73yvjJwL.XYBgMwlpbT6ey", "michal")
insert into user (created, email, enabled, roles, password, username) values ("2021-06-08 08:53:56", "konrad@gazeta.pl", true, "ROLE_USER", "$2a$10$B5wtRob3.ksgjZGxDfc64eC0qq5kxNjMwmJ.1YAiOw9KcyUjYKBKi", "konrad")
insert into user (created, email, enabled, roles, password, username) values ("2021-06-08 08:53:56", "maksymilian@gazeta.pl", true, "ROLE_USER", "$2a$10$b.2pMcFmPAhu56K/d3y2a.aaEQut2x8ic9.mhOgOLsdU.U3M4fEk2", "maksymilian")

insert into info_test (test_field_1, test_field_2, test_field_3, test_field_4, test_field_5) values ("just a test 1", "just a test 2", "just a test 3", "just a test 4", "just a test 5")
insert into info_difr (geometry, incident_soller, incident_slit, detector_soller, detector_slit, detector_absorber, generator_voltage, generator_current, data_range_start, data_range_end, step_size, step_time, stage, spinning_rocking, spinning_rocking_velocity, temperature, comments) values ("BB", 1, 2, 3, 4, "CU01", 5, 6, 7, 8, 9, 10, "SPINNER", true, 11, 12, "some comment")
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package1", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, 1, 1)
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package2", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", 1, null, 1)
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package3", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, null, 1)
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package4", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, null, 1)

insert into info_test (test_field_1, test_field_2, test_field_3, test_field_4, test_field_5) values ("just another test 1", "just another test 2", "just another test 3", "just another test 4", "just another test 5")
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package1", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, 2, 2)
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package2", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, null, 2)
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package3", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, null, 2)
insert into info (access, description, info_name, long_name, short_name, local_date_time, info_difr_id, info_test_id, user_id) values ("PUBLIC", "some description", "package4", "This is some loooong name", "This is some short name", "2021-06-08 08:49:10", null, null, 2)

insert into project (project_name, description, local_date_time, owner_name) values ("test project 1", "test project description 1", "2021-06-08 08:53:56", "michal")
insert into project_user (user_id, project_id) values (1, 1)
insert into project_user (user_id, project_id) values (2, 1)
insert into project (project_name, description, local_date_time, owner_name) values ("test project 2", "test project description 2", "2021-06-08 08:53:56", "michal")
insert into project_user (user_id, project_id) values (1, 2)
insert into project_user (user_id, project_id) values (2, 2)

insert into project_info (info_id, project_id) values (1, 1)
insert into project_info (info_id, project_id) values (2, 1)
insert into project_info (info_id, project_id) values (5, 1)
insert into project_info (info_id, project_id) values (6, 1)
insert into project_info (info_id, project_id) values (3, 2)
insert into project_info (info_id, project_id) values (4, 2)
insert into project_info (info_id, project_id) values (7, 2)
insert into project_info (info_id, project_id) values (8, 2)