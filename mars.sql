-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: May 10, 2016 at 07:20 PM
-- Server version: 5.5.42
-- PHP Version: 5.6.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `medical_app`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `cancel_day`(appt_day DATE)
BEGIN

  UPDATE `medical_app`.`appointments` SET status=3 WHERE date=appt_day;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `iterate_day`(p1 TIME, p2 TIME, appt_day DATE)
BEGIN
  SET @startHour = p1;
  SET @currHour = p1;
  SET @endHour = p2;

  WHILE @currHour < @endHour DO
  -- execute your queries for every hour
  SET @currHour = ADDTIME(@currHour,'00:15:00');
  INSERT INTO `medical_app`.`appointments` (`date`, `timeStart`, `timeEnd`, `status`, `patient_id`, `appointment_id`) VALUES (appt_day, @startHour, @currHour, '0', NULL, NULL);
  SET @startHour = ADDTIME(@startHour,'00:15:00');

  END WHILE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `date` date NOT NULL,
  `timeStart` time NOT NULL,
  `timeEnd` time NOT NULL,
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `patient_id` int(11) unsigned DEFAULT NULL,
  `appointment_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8;



--
-- Table structure for table `appointment_status`
--

CREATE TABLE `appointment_status` (
  `id` tinyint(4) unsigned NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `appointment_status`
--

INSERT INTO `appointment_status` (`id`, `name`) VALUES
(0, 'available'),
(1, 'not_confirmed'),
(2, 'confirmed'),
(3, 'cancelled');

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `client_id` int(11) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  `l_name` varchar(50) NOT NULL,
  `email` varchar(320) NOT NULL,
  `pwd` varchar(128) NOT NULL,
  `status` tinyint(2) unsigned NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- --------------------------------------------------------

--
-- Table structure for table `client_status`
--

CREATE TABLE `client_status` (
  `id` tinyint(2) unsigned NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client_status`
--

INSERT INTO `client_status` (`id`, `name`) VALUES
(0, 'inactive'),
(1, 'active');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `sinch_id` varchar(320) NOT NULL,
  `sender_id` varchar(320) NOT NULL,
  `recipient_id` varchar(320) NOT NULL,
  `messageText` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE `schedule` (
  `schedule_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `timeOpen` time DEFAULT NULL,
  `timeClose` time DEFAULT NULL,
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Triggers `schedule`
--
DELIMITER $$
CREATE TRIGGER `change_appts` AFTER UPDATE ON `schedule`
 FOR EACH ROW BEGIN
  IF NEW.status = 1 THEN
    call cancel_day(NEW.date);
  END IF;

END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `create_appts` AFTER INSERT ON `schedule`
 FOR EACH ROW BEGIN
    call iterate_day(NEW.timeOpen,NEW.timeClose,NEW.date);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `schedule_status`
--

CREATE TABLE `schedule_status` (
  `id` tinyint(2) unsigned NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `schedule_status`
--

INSERT INTO `schedule_status` (`id`, `name`) VALUES
(0, 'open'),
(1, 'closed');

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `staff_id` int(11) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  `l_name` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `pwd` varchar(128) NOT NULL,
  `position` tinyint(2) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `staff_status`
--

CREATE TABLE `staff_status` (
  `id` tinyint(2) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `staff_status`
--

INSERT INTO `staff_status` (`id`, `name`) VALUES
(0, 'admin'),
(1, 'doctors'),
(2, 'staff');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`appointment_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `status` (`status`);

--
-- Indexes for table `appointment_status`
--
ALTER TABLE `appointment_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`client_id`),
  ADD KEY `status` (`status`),
  ADD KEY `status_2` (`status`);

--
-- Indexes for table `client_status`
--
ALTER TABLE `client_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `schedule`
--
ALTER TABLE `schedule`
  ADD PRIMARY KEY (`schedule_id`),
  ADD KEY `status` (`status`);

--
-- Indexes for table `schedule_status`
--
ALTER TABLE `schedule_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`staff_id`),
  ADD KEY `position` (`position`);

--
-- Indexes for table `staff_status`
--
ALTER TABLE `staff_status`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `appointments`
--
ALTER TABLE `appointments`
  MODIFY `appointment_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=203;
--
-- AUTO_INCREMENT for table `clients`
--
ALTER TABLE `clients`
  MODIFY `client_id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `schedule`
--
ALTER TABLE `schedule`
  MODIFY `schedule_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `staff`
--
ALTER TABLE `staff`
  MODIFY `staff_id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `appointments_status_relation` FOREIGN KEY (`status`) REFERENCES `appointment_status` (`id`),
  ADD CONSTRAINT `patient_relation` FOREIGN KEY (`patient_id`) REFERENCES `clients` (`client_id`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Constraints for table `clients`
--
ALTER TABLE `clients`
  ADD CONSTRAINT `clients_schedule_status` FOREIGN KEY (`status`) REFERENCES `client_status` (`id`);

--
-- Constraints for table `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `schedule_status_relation` FOREIGN KEY (`status`) REFERENCES `schedule_status` (`id`);

--
-- Constraints for table `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `staff_position_relation` FOREIGN KEY (`position`) REFERENCES `staff_status` (`id`);
