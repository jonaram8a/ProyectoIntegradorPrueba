CREATE DATABASE chatapp;
USE chatapp;

CREATE TABLE usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    nombreUsuario VARCHAR(50) NOT NULL,
    contrasenia VARCHAR(100) NOT NULL
);

INSERT INTO usuario (nombreUsuario, contrasenia)
VALUES ('admin', '1234');

CREATE VIEW v_usuario AS
SELECT 
    idUsuario,
    nombreUsuario,
    contrasenia
FROM usuario;