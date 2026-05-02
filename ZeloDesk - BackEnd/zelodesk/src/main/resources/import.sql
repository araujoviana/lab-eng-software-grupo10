-- Roles
INSERT INTO tb_role (id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO tb_role (id, authority) VALUES (2, 'ROLE_USUARIO');
INSERT INTO tb_role (id, authority) VALUES (3, 'ROLE_SOLICITANTE');
INSERT INTO tb_role (id, authority) VALUES (4, 'ROLE_TRIAGEM');
INSERT INTO tb_role (id, authority) VALUES (5, 'ROLE_EXECUTOR');

-- Usuarios (senha: 123456 em BCrypt)
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (1, 'Admin Teste', 'admin@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (2, 'Usuario Teste', 'usuario@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (3, 'Carlos Silva', 'carlos@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (4, 'Ana Souza', 'ana@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (5, 'Maria Oliveira', 'solicitante@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (6, 'Carla Mendes', 'triagem@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');
INSERT INTO tb_usuario (id, nome, email, senha) VALUES (7, 'Joao Pereira', 'executor@zelodesk.com', '$2a$10$Gq9V2xcG86hsH282GPdE0u13p8rZUULOeO9IO43kc4zMPP834FDo2');

-- Associação de roles
-- Admin tem acesso ao findAll
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (1, 1);
-- Usuario tem acesso ao findById
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (3, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (4, 2);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (5, 3);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (6, 4);
INSERT INTO tb_user_role (usuario_id, role_id) VALUES (7, 5);
