CREATE TABLE `mobile_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) NOT NULL,
  `city` varchar(30) DEFAULT NULL,
  `provice` varchar(30) DEFAULT NULL,
  `isp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ind_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
