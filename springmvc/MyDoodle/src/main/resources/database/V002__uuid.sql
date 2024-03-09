# Toglie nickname e mette uuid, e rende il voter obbligatorio

# Droppo tutti i voti perché appartengono a un non-utente
drop table Vote;

create table Vote (choice tinyint check (choice between 0 and 2), day date, id bigint not null auto_increment, poll_id bigint, version bigint not null, voter_id bigint not null, primary key (id)) engine=InnoDB;
alter table YadaUserProfile CHANGE nickname uuid varchar(32);




