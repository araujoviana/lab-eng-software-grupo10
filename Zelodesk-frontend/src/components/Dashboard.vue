<template>
  <div class="page-stack">
    <section class="stats-grid">
      <article v-for="item in indicadores" :key="item.label" class="stat-card">
        <span class="stat-label">{{ item.label }}</span>
        <strong class="stat-value">{{ item.value }}</strong>
      </article>
    </section>

    <section class="content-grid">
      <article class="panel">
        <div class="panel-header panel-header--tight">
          <h2 class="panel-title">Fila recente</h2>
          <button type="button" class="ghost-button" @click="goToTickets">Ver lista</button>
        </div>

        <div v-if="recentes.length" class="ticket-list">
          <button
            v-for="ticket in recentes"
            :key="ticket.id"
            type="button"
            class="ticket-row ticket-row-button"
            @click="openTicket(ticket)"
          >
            <div class="ticket-row-main">
              <div class="ticket-row-head">
                <h3 class="ticket-row-title">{{ ticket.titulo }}</h3>
                <span class="status-pill" :class="statusClass(ticket.status)">{{ ticket.status }}</span>
              </div>

              <p class="ticket-row-meta-line">#{{ ticket.id }} · {{ ticket.setor }} · {{ ticket.responsavel }}</p>
            </div>

            <span class="priority-pill" :class="priorityClass(ticket.prioridade)">
              {{ ticket.prioridade }}
            </span>
          </button>
        </div>

        <div v-else class="empty-state">
          <h3>Nenhum ticket ainda</h3>
          <p>Crie o primeiro chamado para iniciar a operacao.</p>
        </div>
      </article>

      <article class="panel">
        <div class="panel-header panel-header--tight">
          <h2 class="panel-title">Status</h2>
        </div>

        <div class="progress-list">
          <div v-for="item in distribuicao" :key="item.label" class="progress-item">
            <div class="progress-label">
              <span>{{ item.label }}</span>

              <strong>{{ item.value }}</strong>
            </div>

            <div class="progress-track">
              <div class="progress-fill" :class="item.className" :style="{ width: ratio(item.value) }"></div>
            </div>
          </div>
        </div>
      </article>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { store as s } from '../store'

const total = computed(() => s.tickets.length)
const abertos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Aberto').length)
const andamento = computed(() => s.tickets.filter((ticket) => ticket.status === 'Em Andamento').length)
const concluidos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Concluído').length)
const prioridadeAlta = computed(() => s.tickets.filter((ticket) => ticket.prioridade === 'Alta').length)
const recentes = computed(() => s.tickets.slice(0, 5))

const indicadores = computed(() => [
  { label: 'Total', value: total.value },
  { label: 'Abertos', value: abertos.value },
  { label: 'Em atendimento', value: andamento.value },
  { label: 'Alta prioridade', value: prioridadeAlta.value }
])

const distribuicao = computed(() => [
  { label: 'Abertos', value: abertos.value, className: 'is-open' },
  { label: 'Em andamento', value: andamento.value, className: 'is-progress' },
  { label: 'Concluidos', value: concluidos.value, className: 'is-done' }
])

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

const ratio = (value) => {
  if (!total.value) {
    return '0%'
  }

  return `${Math.round((value / total.value) * 100)}%`
}

const goToTickets = () => {
  s.page = 'tickets'
}

const openTicket = (ticket) => {
  s.selected = ticket
  s.page = 'tickets'
}
</script>
