ALTER TABLE WcpRegistrationRequest
DROP COLUMN newsletterFlag,
DROP COLUMN username;

ALTER TABLE YadaAttachedFile
MODIFY height integer,
MODIFY width integer;

ALTER TABLE YadaClause
MODIFY content longtext;
