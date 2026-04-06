INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_USUARIO');

INSERT INTO tb_usuario (nome, email, senha) VALUES ('Admin Teste', 'admin@zelodesk.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_usuario (nome, email, senha) VALUES ('Usuario Teste', 'usuario@zelodesk.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_usuario (nome, email, senha) VALUES ('Carlos Silva', 'carlos@zelodesk.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');
INSERT INTO tb_usuario (nome, email, senha) VALUES ('Ana Souza', 'ana@zelodesk.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_user_role (usuario_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (3, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (4, 2)

