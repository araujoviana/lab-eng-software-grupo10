-- Roles
INSERT INTO tb_role (id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO tb_role (id, authority) VALUES (2, 'ROLE_USUARIO');

-- Usuarios (senha: 123456 em BCrypt)
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (1, 'Admin Teste', 'admin@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (2, 'Usuario Teste', 'usuario@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (3, 'Carlos Silva', 'carlos@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (4, 'Ana Souza', 'ana@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');

-- Associação de roles
-- Admin tem acesso ao findAll
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (1, 1);
-- Usuario tem acesso ao findById
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (3, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (4, 2);

