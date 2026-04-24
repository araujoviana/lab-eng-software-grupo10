package com.zelodesk.identity.repository;

import com.zelodesk.identity.domain.Usuario;
import com.zelodesk.identity.projection.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    @Query(nativeQuery = true, value = """
				SELECT tb_usuario.email AS email, tb_usuario.senha AS senha, tb_role.id AS roleId, tb_role.authority AS authority
				FROM tb_usuario
				INNER JOIN tb_user_role ON tb_usuario.id = tb_user_role.usuario_id
				INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
				WHERE tb_usuario.email = :email
			""")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
