CREATE TABLE "user" (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        role VARCHAR(50) NOT NULL
);

CREATE TABLE topic (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50)
);

CREATE TABLE programming_language (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(50)
);

CREATE TABLE task (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      condition TEXT NOT NULL,
                      topic INT NOT NULL,
                      difficulty VARCHAR(50),
                      FOREIGN KEY (topic) REFERENCES topic(id)
);

CREATE TABLE task_language_accessibility (
                                             task INT NOT NULL,
                                             language INT NOT NULL,
                                             PRIMARY KEY(task, language),
                                             FOREIGN KEY (task) REFERENCES task(id),
                                             FOREIGN KEY (language) REFERENCES programming_language(id)
);

CREATE TABLE solution (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          task INT NOT NULL,
                          "user" INT NOT NULL,
                          submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          execution_time INT,
                          code TEXT NOT NULL,
                          FOREIGN KEY (task) REFERENCES task(id),
                          FOREIGN KEY ("user") REFERENCES "user"(id)
);

CREATE TABLE test_case (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           task INT NOT NULL,
                           input_data VARCHAR(100) NOT NULL,
                           output_data VARCHAR(100) NOT NULL,
                           FOREIGN KEY (task) REFERENCES task(id)
);

CREATE TABLE comment (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         task INT NOT NULL,
                         "user" INT NOT NULL,
                         comment_text TEXT NOT NULL,
                         post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (task) REFERENCES task(id),
                         FOREIGN KEY ("user") REFERENCES "user"(id)
);

CREATE TABLE comment_likes (
                               comment INT NOT NULL,
                               "user" INT NOT NULL,
                               PRIMARY KEY (comment, "user"),
                               FOREIGN KEY (comment) REFERENCES comment(id),
                               FOREIGN KEY ("user") REFERENCES "user"(id)
);
