<template>
  <div class="page-stack">
    <section class="summary-inline">
      <div v-for="item in resumo" :key="item.label" class="summary-chip compact">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </div>
    </section>

    <div v-if="s.tickets.length" class="tickets-layout">
      <section class="ticket-grid">
        <article
          v-for="ticket in s.tickets"
          :key="ticket.id"
          class="ticket-card"
          :class="{ 'is-active': activeTicketId === ticket.id }"
          @click="select(ticket)"
        >
          <div class="ticket-card-header">
            <span class="ticket-id">#{{ ticket.id }}</span>
            <span class="status-pill" :class="statusClass(ticket.status)">{{ ticket.status }}</span>
          </div>

          <h3>{{ ticket.titulo }}</h3>
          <p class="ticket-card-copy">{{ ticket.descricao }}</p>

          <div class="ticket-card-meta">
            <span class="meta-tag">{{ ticket.setor }}</span>
            <span class="priority-pill" :class="priorityClass(ticket.prioridade)">
              {{ ticket.prioridade }}
            </span>
          </div>
        </article>
      </section>

      <aside v-if="selectedTicket" class="panel detail-panel">
        <div class="ticket-detail-header">
          <div>
            <p class="ticket-id">#{{ selectedTicket.id }}</p>
            <h2 class="panel-title">{{ selectedTicket.titulo }}</h2>
          </div>

          <span class="status-pill" :class="statusClass(selectedTicket.status)">
            {{ selectedTicket.status }}
          </span>
        </div>

        <div class="ticket-detail-badges">
          <span class="meta-tag">{{ selectedTicket.setor }}</span>
          <span class="priority-pill" :class="priorityClass(selectedTicket.prioridade)">
            {{ selectedTicket.prioridade }}
          </span>
        </div>

        <p class="panel-copy">{{ selectedTicket.descricao }}</p>

        <div class="detail-grid">
          <div class="detail-cell">
            <span>Responsavel</span>
            <strong>{{ selectedTicket.responsavel }}</strong>
          </div>

          <div class="detail-cell">
            <span>Status atual</span>
            <strong>{{ selectedTicket.status }}</strong>
          </div>

          <div class="detail-cell">
            <span>Prioridade</span>
            <strong>{{ selectedTicket.prioridade }}</strong>
          </div>

          <div class="detail-cell">
            <span>Setor</span>
            <strong>{{ selectedTicket.setor }}</strong>
          </div>
        </div>

        <div class="detail-actions">
          <button type="button" class="secondary-button" @click="openKanban">Ver kanban</button>
          <button type="button" class="primary-button" @click="open">Novo ticket</button>
        </div>
      </aside>
    </div>

    <section v-else class="empty-state">
      <h3>Nenhum ticket cadastrado</h3>
      <p>Abra um ticket para começar.</p>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { store as s } from '../store'

const resumo = computed(() => [
  { label: 'Total', value: s.tickets.length },
  { label: 'Alta prioridade', value: s.tickets.filter((ticket) => ticket.prioridade === 'Alta').length },
  { label: 'Em andamento', value: s.tickets.filter((ticket) => ticket.status === 'Em Andamento').length }
])

const activeTicketId = computed(() => s.selected?.id ?? s.tickets[0]?.id ?? null)
const selectedTicket = computed(() => {
  return s.tickets.find((ticket) => ticket.id === activeTicketId.value) ?? null
})

const statusClass = (status) => {
  if (status === 'Aberto') {
    return 'is-open'
  }

  if (status === 'Em Andamento') {
    return 'is-progress'
  }

  return 'is-done'
}

const priorityClass = (priority) => {
  if (priority === 'Alta') {
    return 'is-high'
  }

  if (priority === 'Baixa') {
    return 'is-low'
  }

  return 'is-medium'
}

const open = () => {
  s.modal = true
}

const select = (ticket) => {
  s.selected = ticket
}

const openKanban = () => {
  s.page = 'kanban'
}
</script>
