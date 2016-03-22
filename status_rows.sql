INSERT INTO `client_status` (`id`, `name`) VALUES
(0, 'inactive'),
(1, 'active');

INSERT INTO `appointment_status` (`id`, `name`) VALUES
(0, 'available'),
(1, 'not_confirmed'),
(2, 'confirmed'),
(3, 'cancelled');

INSERT INTO `schedule_status` (`id`, `name`) VALUES
(0, 'open'),
(1, 'closed');