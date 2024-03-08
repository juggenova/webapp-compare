# Sistemo la Vote con quello che mi ero dimenticato

ALTER TABLE Vote MODIFY day date;
alter table Vote add constraint UKo3ctus5pn84xoef6wfg9o8yd6 unique (poll_id, voter_id, day);
