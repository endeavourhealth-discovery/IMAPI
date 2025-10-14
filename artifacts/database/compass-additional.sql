DROP TABLE IF EXISTS concept_set_member;

CREATE TABLE concept_set_member
(
  id       INT AUTO_INCREMENT PRIMARY KEY,
  `set`    VARCHAR(512) NOT NULL,
  `member` VARCHAR(512) NOT NULL,
  `im1id`  VARCHAR(150) NOT NULL,
  `self`   BIT,

  INDEX    idx_set_self (`set`, `self`),
  CONSTRAINT fk_set FOREIGN KEY (`set`) REFERENCES concept_set (`set`)
) ENGINE = INNODB DEFAULT CHARSET= UTF8MB4;

load data infile 'Z:\\Data\\tct_20250826105134.csv'
into table concept_set_member
FIELDS TERMINATED BY '\t'
(@s, @m, @i)
SET `set` = @s,
     `member` = @m,
     `im1Id` = @i,
     `self` = (`set` == `member`)
;
