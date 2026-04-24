<template>
  <div class="page-stack">
    <section class="kanban-grid">
      <article v-for="column in columns" :key="column.key" class="kanban-column">
        <div class="kanban-column-header">
          <h2 class="panel-title">{{ column.label }}</h2>

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
              <span class="ticket-id">#{{ ticket.id }}</span>
              <span class="priority-pill" :class="priorityClass(ticket.prioridade)">
                {{ ticket.prioridade }}
              </span>
            </div>

            <h3 class="kanban-card-title">{{ ticket.titulo }}</h3>

            <div class="ticket-card-meta">
              <span class="meta-tag">{{ ticket.setor }}</span>
              <span class="meta-tag">{{ ticket.responsavel }}</span>
            </div>
          </button>
        </div>

        <div v-else class="kanban-empty">
          Nenhuma demanda nessa etapa.
        </div>
      </article>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { store as s } from '../store'

const columns = [
  {
    key: 'Aberto',
    label: 'Aberto'
  },
  {
    key: 'Em Andamento',
    label: 'Em andamento'
  },
  {
    key: 'Concluído',
    label: 'Concluido'
  }
]

const grouped = computed(() => {
  return columns.reduce((accumulator, column) => {
    accumulator[column.key] = s.tickets.filter((ticket) => ticket.status === column.key)
    return accumulator
  }, {})
})

const priorityClass = (priority) => {
  if (priority === 'Alta') {
    return 'is-high'
  }

  if (priority === 'Baixa') {
    return 'is-low'
  }

  return 'is-medium'
}

const openTicket = (ticket) => {
  s.selected = ticket
  s.page = 'tickets'
}
</script>
