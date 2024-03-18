insert into programming_language (name) values ('java');
insert into topic (name) values ('arrays');
insert into task (name, condition, topic, difficulty) values ('task', 'condition', 1, 'HARD');
insert into test_case (task, input_data, output_data) values (1, '123', '321');
insert into "user" (username, password, role) values ('user', 'user', 'ROLE_USER');
insert into "user" (username, password, role) values ('admin', 'admin', 'ROLE_ADMIN');
insert into comment (task, "user", comment_text, post_date) values (1, 1, 'comm1', default);
insert into comment (task, "user", comment_text, post_date) values (1, 2, 'comm2', default);

