package com.zelodesk.tickets.application;

import com.zelodesk.identity.domain.Usuario;
import com.zelodesk.identity.repository.UsuarioRepository;
import com.zelodesk.shared.exception.ResourceNotFoundException;
import com.zelodesk.tickets.domain.CategoriaEnum;
import com.zelodesk.tickets.domain.Ticket;
import com.zelodesk.tickets.dto.TicketComentarioDTO;
import com.zelodesk.tickets.dto.TicketConclusaoDTO;
import com.zelodesk.tickets.dto.TicketDTO;
import com.zelodesk.tickets.dto.TicketMetricaDTO;
import com.zelodesk.tickets.dto.TicketTriagemDTO;
import com.zelodesk.tickets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TicketService {

    public static final String STATUS_ABERTO = "Aberto";
    public static final String STATUS_EM_TRIAGEM = "Em triagem";
    public static final String STATUS_AGUARDANDO_SOLICITANTE = "Aguardando solicitante";
    public static final String STATUS_EM_EXECUCAO = "Em execucao";
    public static final String STATUS_CONCLUIDO = "Concluido";
    public static final String STATUS_CANCELADO = "Cancelado";
    public static final String STATUS_ENCERRADO = "Encerrado";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TicketDTO insertTicket(TicketDTO ticketDTO) {
        return insertTicket(ticketDTO, null);
    }

    @Transactional
    public TicketDTO insertTicket(TicketDTO ticketDTO, String username) {
        Usuario solicitante = usuarioAtual(username).orElse(null);
        String autor = nomeDoUsuario(solicitante, username, ticketDTO.getSolicitador());

        Ticket ticket = new Ticket();
        ticket.setTitulo(ticketDTO.getTitulo().trim());
        ticket.setDescricao(ticketDTO.getDescricao().trim());
        ticket.setCategoria(ticketDTO.getCategoria());
        ticket.setLocalPredio(ticketDTO.getLocalPredio().trim());
        ticket.setPrioridade(ticketDTO.getPrioridade());
        ticket.setStatus(STATUS_ABERTO);
        ticket.setSolicitador(autor);
        ticket.setSolicitante(solicitante);
        ticket.setAnexoUrl(textOrNull(ticketDTO.getAnexoUrl()));
        ticket.addHistorico(autor, "Ticket aberto", "Solicitacao registrada para triagem.");

        ticket = ticketRepository.save(ticket);
        return new TicketDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> findAll(String status, CategoriaEnum categoria, String localPredio) {
        String statusNormalizado = StringUtils.hasText(status) ? normalizarStatus(status) : null;
        String localNormalizado = StringUtils.hasText(localPredio) ? localPredio.trim() : null;

        List<Ticket> tickets = StringUtils.hasText(localNormalizado)
                ? ticketRepository.searchWithLocalPredio(statusNormalizado, categoria, localNormalizado)
                : ticketRepository.search(statusNormalizado, categoria);

        return tickets
                .stream()
                .map(TicketDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketDTO findById(Long id) {
        return new TicketDTO(buscarTicket(id));
    }

    @Transactional
    public TicketDTO updateStatus(Long id, String novoStatus) {
        Ticket ticket = buscarTicket(id);
        String status = normalizarStatus(novoStatus);
        ticket.setStatus(status);
        ticket.addHistorico("Sistema", "Status atualizado", "Status alterado para " + status + ".");
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO realizarTriagem(Long id, TicketTriagemDTO triagemDTO, String username) {
        Ticket ticket = buscarTicket(id);
        Usuario autorUsuario = usuarioAtual(username).orElse(null);
        String autor = nomeDoUsuario(autorUsuario, username, "Triagem");

        if (!ticket.getStatus().equals(STATUS_ABERTO)
                && !ticket.getStatus().equals(STATUS_EM_TRIAGEM)
                && !ticket.getStatus().equals(STATUS_AGUARDANDO_SOLICITANTE)) {
            throw new IllegalArgumentException("Apenas tickets abertos ou em triagem podem passar por triagem");
        }

        if (triagemDTO.getCategoria() != null) {
            ticket.setCategoria(triagemDTO.getCategoria());
        }
        if (StringUtils.hasText(triagemDTO.getLocalPredio())) {
            ticket.setLocalPredio(triagemDTO.getLocalPredio().trim());
        }

        ticket.setPrioridade(triagemDTO.getPrioridade());
        String decisao = triagemDTO.getDecisao().trim().toUpperCase(Locale.ROOT);
        String motivo = textOrNull(triagemDTO.getMotivo());

        switch (decisao) {
            case "ATRIBUIR" -> atribuirExecutor(ticket, triagemDTO);
            case "AGUARDAR_SOLICITANTE" -> {
                exigirMotivo(motivo, "Motivo e obrigatorio para solicitar complemento");
                ticket.setStatus(STATUS_AGUARDANDO_SOLICITANTE);
                ticket.setMotivoEncerramento(motivo);
            }
            case "CANCELAR" -> {
                exigirMotivo(motivo, "Motivo e obrigatorio para cancelar ticket");
                ticket.setStatus(STATUS_CANCELADO);
                ticket.setMotivoEncerramento(motivo);
            }
            case "ENCERRAR" -> {
                exigirMotivo(motivo, "Motivo e obrigatorio para encerrar ticket");
                ticket.setStatus(STATUS_ENCERRADO);
                ticket.setMotivoEncerramento(motivo);
            }
            default -> throw new IllegalArgumentException("Decisao de triagem invalida");
        }

        ticket.addHistorico(autor, "Triagem registrada", descricaoTriagem(ticket, motivo));
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO assumirTicket(Long id, String username) {
        Ticket ticket = buscarTicket(id);
        Usuario executor = usuarioAtual(username)
                .orElseThrow(() -> new IllegalArgumentException("Executor autenticado nao encontrado"));

        if (ticket.getExecutor() != null && !ticket.getExecutor().getId().equals(executor.getId()) && !isAdmin(executor)) {
            throw new IllegalArgumentException("Ticket atribuido para outro executor");
        }

        ticket.setExecutor(executor);
        ticket.setResponsavelNome(executor.getNome());
        ticket.setStatus(STATUS_EM_EXECUCAO);
        ticket.addHistorico(executor.getNome(), "Ticket assumido", "Executor iniciou o atendimento.");
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO adicionarComentario(Long id, TicketComentarioDTO comentarioDTO, String username) {
        Ticket ticket = buscarTicket(id);
        Usuario usuario = usuarioAtual(username).orElse(null);
        String autor = nomeDoUsuario(usuario, username, comentarioDTO.getAutor());
        String texto = comentarioDTO.getTexto().trim();

        ticket.addComentario(autor, texto);
        ticket.addHistorico(autor, "Comentario registrado", texto);
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO concluirTicket(Long id, TicketConclusaoDTO conclusaoDTO, String username) {
        Ticket ticket = buscarTicket(id);
        Usuario usuario = usuarioAtual(username).orElse(null);
        String autor = nomeDoUsuario(usuario, username, "Executor");

        if (!StringUtils.hasText(conclusaoDTO.getComentarioAtendimento())) {
            throw new IllegalArgumentException("Comentario de atendimento e obrigatorio");
        }
        if (!StringUtils.hasText(conclusaoDTO.getEvidenciaUrl())) {
            throw new IllegalArgumentException("Evidencia final e obrigatoria");
        }

        if (ticket.getExecutor() != null && usuario != null && !ticket.getExecutor().getId().equals(usuario.getId()) && !isAdmin(usuario)) {
            throw new IllegalArgumentException("Apenas o executor atribuido pode concluir o ticket");
        }

        if (!ticket.getStatus().equals(STATUS_EM_EXECUCAO)) {
            throw new IllegalArgumentException("Ticket precisa estar em execucao para ser concluido");
        }

        ticket.setComentarioConclusao(conclusaoDTO.getComentarioAtendimento().trim());
        ticket.setEvidenciaUrl(conclusaoDTO.getEvidenciaUrl().trim());
        ticket.setStatus(STATUS_CONCLUIDO);
        ticket.setConcluidoEm(LocalDateTime.now());
        ticket.addComentario(autor, conclusaoDTO.getComentarioAtendimento().trim());
        ticket.addHistorico(autor, "Ticket concluido", "Atendimento concluido com evidencia final registrada.");
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<TicketMetricaDTO> metricaTicketsPorCategoria() {
        return ticketRepository.countTicketsByCategoria();
    }

    private Ticket buscarTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket nao encontrado: " + id));
    }

    private Optional<Usuario> usuarioAtual(String username) {
        if (!StringUtils.hasText(username)) {
            return Optional.empty();
        }
        return usuarioRepository.findByEmail(username);
    }

    private String nomeDoUsuario(Usuario usuario, String username, String fallback) {
        if (usuario != null && StringUtils.hasText(usuario.getNome())) {
            return usuario.getNome();
        }
        if (StringUtils.hasText(fallback)) {
            return fallback.trim();
        }
        return StringUtils.hasText(username) ? username : "Sistema";
    }

    private void atribuirExecutor(Ticket ticket, TicketTriagemDTO triagemDTO) {
        Usuario executor = null;
        if (triagemDTO.getExecutorId() != null) {
            executor = usuarioRepository.findById(triagemDTO.getExecutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Executor nao encontrado: " + triagemDTO.getExecutorId()));
        }

        String responsavel = executor != null ? executor.getNome() : textOrNull(triagemDTO.getResponsavelNome());
        if (!StringUtils.hasText(responsavel)) {
            throw new IllegalArgumentException("Responsavel e obrigatorio para atribuir ticket");
        }

        ticket.setExecutor(executor);
        ticket.setResponsavelNome(responsavel);
        ticket.setStatus(STATUS_EM_EXECUCAO);
        ticket.setMotivoEncerramento(null);
    }

    private String descricaoTriagem(Ticket ticket, String motivo) {
        String descricao = "Status: " + ticket.getStatus() + "; prioridade: " + ticket.getPrioridade();
        if (StringUtils.hasText(ticket.getResponsavelNome())) {
            descricao += "; responsavel: " + ticket.getResponsavelNome();
        }
        if (StringUtils.hasText(motivo)) {
            descricao += "; motivo: " + motivo;
        }
        return descricao + ".";
    }

    private void exigirMotivo(String motivo, String mensagem) {
        if (!StringUtils.hasText(motivo)) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    private boolean isAdmin(Usuario usuario) {
        return usuario != null && usuario.hasRole("ROLE_ADMIN");
    }

    private String textOrNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String normalizarStatus(String status) {
        if (!StringUtils.hasText(status)) {
            throw new IllegalArgumentException("Status e obrigatorio");
        }

        return switch (status.trim().toUpperCase(Locale.ROOT)) {
            case "ABERTO" -> STATUS_ABERTO;
            case "EM_TRIAGEM", "EM TRIAGEM" -> STATUS_EM_TRIAGEM;
            case "AGUARDANDO_SOLICITANTE", "AGUARDANDO SOLICITANTE" -> STATUS_AGUARDANDO_SOLICITANTE;
            case "EM_EXECUCAO", "EM EXECUCAO", "EM EXECUÇÃO", "EM ANDAMENTO" -> STATUS_EM_EXECUCAO;
            case "CONCLUIDO", "CONCLUÍDO" -> STATUS_CONCLUIDO;
            case "CANCELADO" -> STATUS_CANCELADO;
            case "ENCERRADO" -> STATUS_ENCERRADO;
            default -> throw new IllegalArgumentException("Status invalido: " + status);
        };
    }
}
