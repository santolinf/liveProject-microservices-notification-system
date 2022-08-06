DROP TABLE IF EXISTS preferences;

CREATE TABLE preferences (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customerId VARCHAR(250) NOT NULL,
  smsPreferenceFlag BOOLEAN NOT NULL,
  emailPreferenceFlag BOOLEAN NOT NULL,
  phoneNumber VARCHAR(250) NOT NULL,
  emailAddress VARCHAR(250) DEFAULT NULL
);
