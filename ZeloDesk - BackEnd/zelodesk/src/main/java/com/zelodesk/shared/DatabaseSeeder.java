package com.zelodesk.shared;

import com.zelodesk.identity.domain.Role;
import com.zelodesk.identity.domain.Usuario;
import com.zelodesk.identity.repository.RoleRepository;
import com.zelodesk.identity.repository.UsuarioRepository;
import com.zelodesk.tickets.application.TicketService;
import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.domain.PrioridadeEnum;
import com.zelodesk.tickets.domain.Ticket;
import com.zelodesk.tickets.repository.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!test")
public class DatabaseSeeder {

    @Bean
    CommandLineRunner seedDatabase(RoleRepository roleRepository,
                                   UsuarioRepository usuarioRepository,
                                   TicketRepository ticketRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            Role admin = role(roleRepository, "ROLE_ADMIN");
            Role usuario = role(roleRepository, "ROLE_USUARIO");
            Role solicitanteRole = role(roleRepository, "ROLE_SOLICITANTE");
            Role triagemRole = role(roleRepository, "ROLE_TRIAGEM");
            Role executorRole = role(roleRepository, "ROLE_EXECUTOR");

            Usuario adminUser = usuario(usuarioRepository, passwordEncoder, "Admin Teste", "admin@zelodesk.com", admin);
            Usuario solicitante = usuario(usuarioRepository, passwordEncoder, "Maria Oliveira", "solicitante@zelodesk.com", solicitanteRole);
            Usuario triagem = usuario(usuarioRepository, passwordEncoder, "Carla Mendes", "triagem@zelodesk.com", triagemRole);
            Usuario executor = usuario(usuarioRepository, passwordEncoder, "Joao Pereira", "executor@zelodesk.com", executorRole);
            usuario(usuarioRepository, passwordEncoder, "Usuario Teste", "usuario@zelodesk.com", usuario);

            if (ticketRepository.count() == 0) {
                Ticket aberto = ticket("Vazamento no bloco A",
                        "Morador informou vazamento constante proximo ao hall dos elevadores.",
                        CategoriaEnum.HIDRAULICA,
                        "Bloco A - 2o andar",
                        PrioridadeEnum.ALTA,
                        solicitante,
                        null,
                        TicketService.STATUS_ABERTO);
                aberto.addHistorico(solicitante.getNome(), "Ticket aberto", "Solicitacao inicial registrada.");

                Ticket execucao = ticket("Portao da garagem travando",
                        "Portao apresenta falha intermitente no fechamento durante a noite.",
                        CategoriaEnum.MANUTENCAO,
                        "Garagem subsolo",
                        PrioridadeEnum.MEDIA,
                        solicitante,
                        executor,
                        TicketService.STATUS_EM_EXECUCAO);
                execucao.setResponsavelNome(executor.getNome());
                execucao.addHistorico(triagem.getNome(), "Triagem registrada", "Ticket atribuido para execucao.");

                Ticket concluido = ticket("Limpeza tecnica da casa de bombas",
                        "Limpeza preventiva concluida apos vistoria da equipe de manutencao.",
                        CategoriaEnum.LIMPEZA,
                        "Casa de bombas",
                        PrioridadeEnum.BAIXA,
                        solicitante,
                        executor,
                        TicketService.STATUS_CONCLUIDO);
                concluido.setResponsavelNome(executor.getNome());
                concluido.setEvidenciaUrl("https://exemplo.com/evidencias/casa-bombas.jpg");
                concluido.setComentarioConclusao("Ambiente limpo e liberado para operacao.");
                concluido.addHistorico(executor.getNome(), "Ticket concluido", "Atendimento finalizado com evidencia.");

                ticketRepository.save(aberto);
                ticketRepository.save(execucao);
                ticketRepository.save(concluido);
            }

            adminUser.addRole(triagemRole);
            adminUser.addRole(executorRole);
            usuarioRepository.save(adminUser);
        };
    }

    private Role role(RoleRepository roleRepository, String authority) {
        return roleRepository.findByAuthority(authority)
                .orElseGet(() -> roleRepository.save(new Role(null, authority)));
    }

    private Usuario usuario(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                            String nome, String email, Role role) {
        return usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(passwordEncoder.encode("123456"));
            usuario.addRole(role);
            return usuarioRepository.save(usuario);
        });
    }

    private Ticket ticket(String titulo, String descricao, CategoriaEnum categoria, String localPredio,
                          PrioridadeEnum prioridade, Usuario solicitante, Usuario executor, String status) {
        Ticket ticket = new Ticket();
        ticket.setTitulo(titulo);
        ticket.setDescricao(descricao);
        ticket.setCategoria(categoria);
        ticket.setLocalPredio(localPredio);
        ticket.setPrioridade(prioridade);
        ticket.setStatus(status);
        ticket.setSolicitante(solicitante);
        ticket.setSolicitador(solicitante.getNome());
        ticket.setExecutor(executor);
        return ticket;
    }
}
