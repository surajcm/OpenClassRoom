--
-- Create schema poseidon
--

CREATE DATABASE IF NOT EXISTS openclassroom;
USE openclassroom;
--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `LogId` varchar(45) NOT NULL,
  `Pass` varchar(45) NOT NULL,
  `Role` varchar(45) NOT NULL,
  `createdOn` datetime NOT NULL,
  `modifiedOn` datetime DEFAULT NULL,
  `createdBy` varchar(45) NOT NULL,
  `modifiedBy` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`,`Name`,`LogId`,`Pass`,`Role`,`createdOn`,`modifiedOn`,`createdBy`,`modifiedBy`) VALUES
 (1,'admin','admin','admin','ADMIN','2012-06-02 00:00:00','2012-06-02 00:00:00','admin','admin'),
 (2,'guest','guest','guest','GUEST','2012-06-02 00:00:00','2012-06-02 00:00:00','admin','admin');
