package com.zelodesk.identity.projection;

public interface UserDetailsProjection {
    String getEmail();
    String getSenha();
    Long getRoleId();
    String getAuthority();
}
