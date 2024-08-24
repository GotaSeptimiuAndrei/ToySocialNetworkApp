CREATE TABLE Users
(
    username           VARCHAR(255) PRIMARY KEY,
    password           VARCHAR(255) NOT NULL,
    firstname          VARCHAR(255) NOT NULL,
    lastname           VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    gender             VARCHAR(255) NOT NULL,
    description        VARCHAR(255),
    profilePicturePath VARCHAR(255)
);

CREATE TABLE Messages
(
    idMessage   BIGINT PRIMARY KEY,
    sender      VARCHAR(255) NOT NULL,
    receiver    VARCHAR(255) NOT NULL,
    messageTime TIME         NOT NULL,
    messageDate DATE         NOT NULL,
    seen        BOOLEAN      NOT NULL,
    received    BOOLEAN      NOT NULL
);

CREATE TABLE messagesContent
(
    idMessage BIGINT       NOT NULL,
    partOrder INT          NOT NULL,
    content   VARCHAR(256) NOT NULL,
    FOREIGN KEY (idMessage) REFERENCES Messages (idMessage)
);

CREATE TABLE Friendships
(
    idFriend1         VARCHAR(255) NOT NULL,
    idFriend2         VARCHAR(255) NOT NULL,
    friendshipdate    DATE         NOT NULL,
    friendshiptime    TIME         NOT NULL,
    isPending         BOOLEAN      NOT NULL,
    isFromFirstFriend BOOLEAN      NOT NULL,
    PRIMARY KEY (idFriend1, idFriend2)
);