<template>
  <div class="page-stack">
    <section class="stats-grid" aria-label="Indicadores">
      <article v-for="item in indicadores" :key="item.label" class="stat-card">
        <span class="stat-label">{{ item.label }}</span>
        <strong class="stat-value">{{ item.value }}</strong>
      </article>
    </section>

    <section class="content-grid">
      <article class="panel">
        <div class="panel-header panel-header--tight">
          <div>
            <h2 class="panel-title">Chamados recentes</h2>
          </div>
          <button type="button" class="ghost-button" @click="s.page = 'tickets'">Ver todos</button>
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
                <span class="status-pill" :class="statusClass(ticket.status)">{{ statusLabel(ticket.status) }}</span>
              </div>
              <p class="ticket-row-meta-line">
                {{ ticketCode(ticket) }} · {{ categoryLabel(ticket.categoria) }} · {{ ticket.localPredio }}
              </p>
            </div>
            <span class="priority-pill" :class="priorityClass(ticket.prioridade)">
              {{ priorityLabel(ticket.prioridade) }}
            </span>
          </button>
        </div>

        <div v-else class="empty-state">
          <h3>Nenhum ticket ainda</h3>
          <p>Crie o primeiro chamado para iniciar a operação.</p>
        </div>
      </article>

      <article class="panel">
        <div class="panel-header panel-header--tight">
          <div>
            <h2 class="panel-title">Distribuição por status</h2>
          </div>
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
import {
  categoryLabel,
  priorityClass,
  priorityLabel,
  statusClass,
  statusLabel,
  store as s,
  ticketCode
} from '../store'

const total = computed(() => s.tickets.length)
const abertos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Aberto').length)
const triagem = computed(() => s.tickets.filter((ticket) => ['Em triagem', 'Aguardando solicitante'].includes(ticket.status)).length)
const execucao = computed(() => s.tickets.filter((ticket) => ticket.status === 'Em execucao').length)
const concluidos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Concluido').length)
const altaPrioridade = computed(() => s.tickets.filter((ticket) => ticket.prioridade === 'ALTA').length)
const recentes = computed(() => s.tickets.slice(0, 5))

const indicadores = computed(() => [
  { label: 'Total', value: total.value },
  { label: 'Abertos', value: abertos.value },
  { label: 'Em execução', value: execucao.value },
  { label: 'Alta prioridade', value: altaPrioridade.value }
])

const distribuicao = computed(() => [
  { label: 'Abertos', value: abertos.value, className: 'is-open' },
  { label: 'Triagem', value: triagem.value, className: 'is-triage' },
  { label: 'Execução', value: execucao.value, className: 'is-progress' },
  { label: 'Concluídos', value: concluidos.value, className: 'is-done' }
])

const ratio = (value) => {
  if (!total.value) return '0%'
  return `${Math.max(8, Math.round((value / total.value) * 100))}%`
}

const openTicket = (ticket) => {
  s.selectedId = ticket.id
  s.page = 'tickets'
}
</script>
