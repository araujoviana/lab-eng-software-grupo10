<template>
  <div class="page-stack">
    <section class="toolbar panel" aria-label="Filtros de tickets">
      <label class="form-field compact-field">
        <span>Buscar</span>
        <input v-model="s.filters.busca" class="input" type="search" placeholder="Título, local ou descrição" />
      </label>

      <label class="form-field compact-field">
        <span>Status</span>
        <select v-model="s.filters.status" class="input" @change="loadTickets">
          <option value="">Todos</option>
          <option v-for="status in statusList" :key="status" :value="status">{{ statusLabel(status) }}</option>
        </select>
      </label>

      <label class="form-field compact-field">
        <span>Categoria</span>
        <select v-model="s.filters.categoria" class="input" @change="loadTickets">
          <option value="">Todas</option>
          <option v-for="categoria in categorias" :key="categoria.value" :value="categoria.value">
            {{ categoria.label }}
          </option>
        </select>
      </label>
    </section>

    <div v-if="filteredTickets.length" class="tickets-layout">
      <section class="ticket-grid" aria-label="Lista de tickets">
        <button
          v-for="ticket in filteredTickets"
          :key="ticket.id"
          type="button"
          class="ticket-card"
          :class="{ 'is-active': selected?.id === ticket.id }"
          @click="s.selectedId = ticket.id"
        >
          <div class="ticket-card-header">
            <span class="ticket-id">{{ ticketCode(ticket) }}</span>
            <span class="status-pill" :class="statusClass(ticket.status)">{{ statusLabel(ticket.status) }}</span>
          </div>

          <h3>{{ ticket.titulo }}</h3>
          <p class="ticket-card-copy">{{ ticket.descricao }}</p>

          <div class="ticket-card-meta">
            <span class="meta-tag">{{ categoryLabel(ticket.categoria) }}</span>
            <span class="meta-tag">{{ ticket.localPredio }}</span>
            <span class="priority-pill" :class="priorityClass(ticket.prioridade)">
              {{ priorityLabel(ticket.prioridade) }}
            </span>
          </div>
        </button>
      </section>

      <aside v-if="selected" class="panel detail-panel">
        <div class="ticket-detail-header">
          <div>
            <p class="ticket-id">{{ ticketCode(selected) }}</p>
            <h2 class="panel-title">{{ selected.titulo }}</h2>
          </div>
          <span class="status-pill" :class="statusClass(selected.status)">{{ statusLabel(selected.status) }}</span>
        </div>

        <p class="panel-copy">{{ selected.descricao }}</p>

        <div class="detail-grid">
          <div class="detail-cell">
            <span>Categoria</span>
            <strong>{{ categoryLabel(selected.categoria) }}</strong>
          </div>
          <div class="detail-cell">
            <span>Localização</span>
            <strong>{{ selected.localPredio }}</strong>
          </div>
          <div class="detail-cell">
            <span>Prioridade</span>
            <strong>{{ priorityLabel(selected.prioridade) }}</strong>
          </div>
          <div class="detail-cell">
            <span>Responsável</span>
            <strong>{{ selected.responsavelNome || selected.executorNome || 'Não atribuído' }}</strong>
          </div>
        </div>

        <section v-if="canTriage() && canTicketBeTriaged" class="workflow-card">
          <h3>Triagem</h3>
          <form class="form-grid" @submit.prevent="submitTriagem">
            <label class="form-field">
              <span>Prioridade</span>
              <select v-model="triagem.prioridade" class="input" required>
                <option v-for="item in prioridades" :key="item.value" :value="item.value">{{ item.label }}</option>
              </select>
            </label>

            <label class="form-field">
              <span>Decisão</span>
              <select v-model="triagem.decisao" class="input" required>
                <option value="ATRIBUIR">Atribuir executor</option>
                <option value="AGUARDAR_SOLICITANTE">Pedir complemento</option>
                <option value="CANCELAR">Cancelar</option>
                <option value="ENCERRAR">Encerrar administrativamente</option>
              </select>
            </label>

            <label v-if="triagem.decisao === 'ATRIBUIR'" class="form-field form-field-full">
              <span>Executor</span>
              <select v-model="triagem.executorId" class="input">
                <option value="">Informar responsável manualmente</option>
                <option v-for="executor in s.executores" :key="executor.id" :value="executor.id">
                  {{ executor.nome }}
                </option>
              </select>
            </label>

            <label v-if="triagem.decisao === 'ATRIBUIR' && !triagem.executorId" class="form-field form-field-full">
              <span>Responsável manual</span>
              <input v-model="triagem.responsavelNome" class="input" type="text" placeholder="Ex: Equipe Predial" />
            </label>

            <label v-if="triagem.decisao !== 'ATRIBUIR'" class="form-field form-field-full">
              <span>Motivo</span>
              <textarea v-model="triagem.motivo" class="textarea compact-textarea" required></textarea>
            </label>

            <button type="submit" class="primary-button form-field-full" :disabled="s.saving">Registrar triagem</button>
          </form>
        </section>

        <section v-if="canExecute() && selected.status === 'Em execucao'" class="workflow-card">
          <h3>Execução</h3>
          <button type="button" class="secondary-button" @click="assume" :disabled="s.saving">
            Assumir ticket
          </button>

          <form class="form-grid execution-form" @submit.prevent="submitConclusao">
            <label class="form-field form-field-full">
              <span>Comentário de atendimento</span>
              <textarea v-model="conclusao.comentarioAtendimento" class="textarea" required></textarea>
            </label>

            <label class="form-field form-field-full">
              <span>Evidência final (URL)</span>
              <input v-model="conclusao.evidenciaUrl" class="input" type="url" placeholder="https://..." required />
            </label>

            <button type="submit" class="primary-button form-field-full" :disabled="s.saving">
              Concluir ticket
            </button>
          </form>
        </section>

        <section class="workflow-card">
          <h3>Comentário</h3>
          <form class="comment-form" @submit.prevent="submitComentario">
            <textarea v-model="comentario" class="textarea compact-textarea" placeholder="Registre uma atualização" required></textarea>
            <button type="submit" class="secondary-button" :disabled="s.saving">Adicionar</button>
          </form>
        </section>

        <section class="timeline-section">
          <h3>Histórico</h3>
          <ol class="timeline-list">
            <li v-for="item in selected.historico" :key="item.id || item.criadoEm">
              <strong>{{ item.acao }}</strong>
              <span>{{ item.autor }} · {{ formatDate(item.criadoEm) }}</span>
              <p>{{ item.descricao }}</p>
            </li>
          </ol>
        </section>

        <section v-if="selected.comentarios?.length" class="timeline-section">
          <h3>Comentários</h3>
          <ol class="timeline-list">
            <li v-for="item in selected.comentarios" :key="item.id || item.criadoEm">
              <strong>{{ item.autor }}</strong>
              <span>{{ formatDate(item.criadoEm) }}</span>
              <p>{{ item.texto }}</p>
            </li>
          </ol>
        </section>
      </aside>
    </div>

    <section v-else class="empty-state">
      <h3>Nenhum ticket encontrado</h3>
      <p>Ajuste os filtros ou abra um novo ticket.</p>
    </section>
  </div>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import {
  addComment,
  assumeTicket,
  canExecute,
  canTriage,
  categorias,
  categoryLabel,
  concludeTicket,
  loadTickets,
  prioridades,
  priorityClass,
  priorityLabel,
  selectedTicket,
  statusClass,
  statusLabel,
  statusList,
  store as s,
  ticketCode,
  triageTicket
} from '../store'

const comentario = ref('')

const triagem = reactive({
  prioridade: 'MEDIA',
  decisao: 'ATRIBUIR',
  executorId: '',
  responsavelNome: '',
  motivo: ''
})

const conclusao = reactive({
  comentarioAtendimento: '',
  evidenciaUrl: ''
})

const selected = computed(() => selectedTicket())

const filteredTickets = computed(() => {
  const busca = s.filters.busca.trim().toLowerCase()
  if (!busca) return s.tickets

  return s.tickets.filter((ticket) => {
    return [ticket.titulo, ticket.descricao, ticket.localPredio, ticket.solicitador]
      .filter(Boolean)
      .some((value) => value.toLowerCase().includes(busca))
  })
})

const canTicketBeTriaged = computed(() => {
  return ['Aberto', 'Em triagem', 'Aguardando solicitante'].includes(selected.value?.status)
})

watch(selected, (ticket) => {
  if (!ticket) return
  triagem.prioridade = ticket.prioridade || 'MEDIA'
  triagem.executorId = ticket.executorId || ''
  triagem.responsavelNome = ticket.responsavelNome || ''
  triagem.motivo = ''
  conclusao.comentarioAtendimento = ''
  conclusao.evidenciaUrl = ticket.evidenciaUrl || ''
}, { immediate: true })

const submitTriagem = async () => {
  if (!selected.value) return
  await triageTicket(selected.value.id, {
    prioridade: triagem.prioridade,
    decisao: triagem.decisao,
    executorId: triagem.executorId ? Number(triagem.executorId) : null,
    responsavelNome: triagem.responsavelNome,
    motivo: triagem.motivo
  })
}

const assume = async () => {
  if (!selected.value) return
  await assumeTicket(selected.value.id)
}

const submitConclusao = async () => {
  if (!selected.value) return
  await concludeTicket(selected.value.id, { ...conclusao })
}

const submitComentario = async () => {
  if (!selected.value || !comentario.value.trim()) return
  await addComment(selected.value.id, comentario.value.trim())
  comentario.value = ''
}

const formatDate = (value) => {
  if (!value) return 'Agora'
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(value))
}
</script>
