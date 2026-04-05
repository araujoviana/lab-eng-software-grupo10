package com.zelodesk.projections;

public interface UserDetailsProjection {
    String getEmail();
    String getSenha();
    Long getRoleId();
    String getAuthority();
}
