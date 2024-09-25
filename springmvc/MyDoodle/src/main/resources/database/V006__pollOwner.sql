ALTER TABLE Poll
ADD COLUMN owner_id BIGINT;

alter table Poll add constraint FK47dx1n0k3d9r1jpd8nl25q590 foreign key (owner_id) references YadaUserProfile (id);
