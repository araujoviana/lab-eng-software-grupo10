<template>
  <div class="page-stack">
    <section class="kanban-grid" aria-label="Tickets por status">
      <article v-for="column in columns" :key="column.key" class="kanban-column">
        <div class="kanban-column-header">
          <div>
            <h2 class="panel-title">{{ column.label }}</h2>
            <p class="panel-copy">{{ column.description }}</p>
          </div>
          <span class="column-counter">{{ grouped[column.key].length }}</span>
        </div>

        <div v-if="grouped[column.key].length" class="kanban-list">
          <button
            v-for="ticket in grouped[column.key]"
            :key="ticket.id"
            type="button"
            class="kanban-card"
            @click="openTicket(ticket)"
          >
            <div class="kanban-card-top">
              <span class="ticket-id">{{ ticketCode(ticket) }}</span>
              <span class="priority-pill" :class="priorityClass(ticket.prioridade)">
                {{ priorityLabel(ticket.prioridade) }}
              </span>
            </div>
            <h3 class="kanban-card-title">{{ ticket.titulo }}</h3>
            <p>{{ ticket.localPredio }}</p>
            <div class="ticket-card-meta">
              <span class="meta-tag">{{ categoryLabel(ticket.categoria) }}</span>
              <span class="meta-tag">{{ ticket.responsavelNome || 'Sem responsavel' }}</span>
            </div>
          </button>
        </div>

        <div v-else class="kanban-empty">Nenhuma demanda nesta etapa.</div>
      </article>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { categoryLabel, priorityClass, priorityLabel, store as s, ticketCode } from '../store'

const columns = [
  { key: 'abertura', label: 'Abertura', description: 'Recebidos e aguardando analise' },
  { key: 'triagem', label: 'Triagem', description: 'Complemento, decisao ou encaminhamento' },
  { key: 'execucao', label: 'Execucao', description: 'Atribuidos ao executor' },
  { key: 'finalizados', label: 'Finalizados', description: 'Concluidos, cancelados ou encerrados' }
]

const grouped = computed(() => {
  const base = columns.reduce((accumulator, column) => {
    accumulator[column.key] = []
    return accumulator
  }, {})

  s.tickets.forEach((ticket) => {
    if (ticket.status === 'Aberto') base.abertura.push(ticket)
    else if (['Em triagem', 'Aguardando solicitante'].includes(ticket.status)) base.triagem.push(ticket)
    else if (ticket.status === 'Em execucao') base.execucao.push(ticket)
    else base.finalizados.push(ticket)
  })

  return base
})

const openTicket = (ticket) => {
  s.selectedId = ticket.id
  s.page = 'tickets'
}
</script>
