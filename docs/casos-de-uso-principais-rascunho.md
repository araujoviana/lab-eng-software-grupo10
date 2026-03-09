# Casos de Uso Principais - Rascunho

Este arquivo segue a orientacao da disciplina para texto de casos de uso:
- foco no objetivo do ator;
- descricao em estilo essencial e caixa-preta;
- cenario de sucesso principal;
- cenarios alternativos;
- pre-condicoes e garantias de resultado (pos-condicoes).

Escopo deste rascunho: somente os 3 casos de uso principais do MVP do Sistema de Tickets para Condominios.

## UC-01 - Abrir ticket de zeladoria

- **Ator principal:** Solicitante (Morador/Portaria)
- **Objetivo:** registrar uma ocorrencia para iniciar o atendimento.
- **Pre-condicoes:** usuario autenticado e com permissao de solicitante.
- **Pos-condicoes (sucesso):** ticket criado com status `Aberto`, dados salvos e historico inicial registrado.
- **Pos-condicoes (falha):** ticket nao e criado e o solicitante recebe mensagem de erro.

### Cenario de sucesso principal
1. O solicitante seleciona a opcao de abrir ticket.
2. O sistema apresenta formulario de abertura.
3. O solicitante informa titulo, descricao, categoria, localizacao e urgencia.
4. O solicitante anexa foto, quando necessario.
5. O solicitante confirma o envio.
6. O sistema valida os campos obrigatorios.
7. O sistema cria o ticket com status `Aberto`.
8. O sistema registra historico e exibe confirmacao com identificador do ticket.

### Cenarios alternativos
- **A1 - Campos obrigatorios ausentes:** o sistema informa os campos faltantes e retorna ao formulario.
- **A2 - Anexo invalido:** o sistema rejeita o arquivo e solicita novo envio.
- **A3 - Falha de persistencia:** o sistema informa indisponibilidade temporaria e nao cria o ticket.

### Regras de negocio associadas
- RN-01: todo ticket deve ter categoria, localizacao e descricao minima.
- RN-02: anexo e opcional na abertura, mas recomendado para triagem mais rapida.

## UC-02 - Realizar triagem e atribuir responsavel

- **Ator principal:** Triagem (Sindico/Administradora)
- **Objetivo:** classificar a demanda e encaminhar para execucao.
- **Pre-condicoes:** ticket existente com status `Aberto` ou `Em triagem`.
- **Pos-condicoes (sucesso):** ticket classificado com prioridade e responsavel definido.
- **Pos-condicoes (falha):** ticket permanece em fila de triagem, com justificativa registrada.

### Cenario de sucesso principal
1. A triagem acessa a fila de tickets pendentes.
2. A triagem seleciona um ticket para analise.
3. O sistema exibe detalhes, historico e anexos do ticket.
4. A triagem ajusta dados do ticket, se necessario.
5. A triagem define prioridade.
6. A triagem atribui um executor responsavel.
7. A triagem confirma a operacao.
8. O sistema salva as alteracoes, atualiza status para `Em execucao` e registra auditoria.

### Cenarios alternativos
- **B1 - Informacao insuficiente:** triagem solicita complemento ao solicitante e marca como `Aguardando solicitante`.
- **B2 - Sem executor disponivel:** ticket permanece em `Em triagem` ate nova atribuicao.
- **B3 - Ticket improcedente/duplicado:** triagem cancela ticket com motivo registrado.

### Regras de negocio associadas
- RN-03: apenas perfil de triagem pode definir prioridade e atribuir responsavel.
- RN-04: toda alteracao de status e atribuicao deve gerar registro de auditoria.

## UC-03 - Executar atendimento e concluir ticket

- **Ator principal:** Executor (Zelador/Manutencao/Prestador)
- **Objetivo:** executar o servico e concluir o ticket com evidencia.
- **Pre-condicoes:** ticket atribuido ao executor e apto para execucao.
- **Pos-condicoes (sucesso):** ticket marcado como `Concluido`, com historico e evidencia final.
- **Pos-condicoes (falha):** ticket retorna para triagem ou permanece em execucao com justificativa.

### Cenario de sucesso principal
1. O executor acessa seus tickets atribuidos.
2. O executor assume o ticket.
3. O executor atualiza o status para `Em execucao`.
4. O executor registra comentarios de andamento.
5. O executor realiza o atendimento da demanda.
6. O executor anexa evidencia final (ex.: foto do servico realizado).
7. O executor marca o ticket como `Concluido`.
8. O sistema registra historico, auditoria e notifica solicitante e triagem.

### Cenarios alternativos
- **C1 - Atendimento impossivel no local:** executor devolve para triagem com justificativa.
- **C2 - Sem evidencia final:** sistema bloqueia conclusao ate anexar evidencia.
- **C3 - Reabertura pelo solicitante (janela valida):** ticket sai de `Concluido` e volta para fluxo de triagem.

### Regras de negocio associadas
- RN-05: apenas executor atribuido pode concluir o ticket.
- RN-06: conclusao exige registro minimo de atendimento e evidencia final.

## Priorizacao para os proximos diagramas

1. **Primeiro foco:** UC-02 (triagem e atribuicao), por ser o ponto de decisao do fluxo.
2. **Segundo foco:** UC-03 (execucao e conclusao), por concentrar atualizacao de status e evidencias.
3. **Terceiro foco:** UC-01 (abertura), por iniciar o ciclo e validar qualidade dos dados de entrada.
