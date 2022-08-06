DROP TABLE IF EXISTS notifications;

CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customerId VARCHAR(250) NOT NULL,
    notificationMode VARCHAR(250),
    notificationTemplateName VARCHAR(250)
);

DROP TABLE IF EXISTS parameters;

CREATE TABLE parameters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    notificationId INT,
    parameterName VARCHAR(250) NOT NULL,
    parameterValue VARCHAR(250) NOT NULL
);
