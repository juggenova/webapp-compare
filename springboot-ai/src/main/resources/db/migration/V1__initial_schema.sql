-- Initial schema for the Doodle Clone application

-- Polls table
CREATE TABLE polls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    start_day DATE NOT NULL,
    end_day DATE NOT NULL,
    deadline TIMESTAMP NOT NULL,
    chosen_day DATE
);

-- Votes table
CREATE TABLE votes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    poll_id BIGINT NOT NULL,
    day DATE NOT NULL,
    choice ENUM('NO', 'YES', 'MAYBE') NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (poll_id) REFERENCES polls(id),
    UNIQUE KEY unique_vote (poll_id, session_id, day)
);
