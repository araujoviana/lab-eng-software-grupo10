<template>
  <aside class="sidebar">
    <div class="brand-block">
      <div class="brand-mark">ZD</div>
      <div class="brand-copy">
        <h2>Zelo Desk</h2>
        <p>Tickets para condominios</p>
      </div>
    </div>

    <nav class="nav-list" aria-label="Navegacao principal">
      <button
        v-for="item in items"
        :key="item.key"
        type="button"
        class="nav-item"
        :class="{ 'is-active': s.page === item.key }"
        @click="s.page = item.key"
      >
        <span>
          <strong>{{ item.label }}</strong>
          <small>{{ item.description }}</small>
        </span>
        <span v-if="item.badge !== null" class="nav-badge">{{ item.badge }}</span>
      </button>
    </nav>

    <section class="sidebar-card" aria-label="Resumo dos tickets">
      <div class="mini-metric">
        <span>Abertos</span>
        <strong>{{ abertos }}</strong>
      </div>
      <div class="mini-metric">
        <span>Em execucao</span>
        <strong>{{ execucao }}</strong>
      </div>
      <div class="mini-metric">
        <span>Alta prioridade</span>
        <strong>{{ altaPrioridade }}</strong>
      </div>
    </section>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { store as s } from '../store'

const abertos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Aberto').length)
const execucao = computed(() => s.tickets.filter((ticket) => ticket.status === 'Em execucao').length)
const altaPrioridade = computed(() => s.tickets.filter((ticket) => ticket.prioridade === 'ALTA').length)

const items = computed(() => [
  { key: 'dashboard', label: 'Visao geral', description: 'Indicadores do dia', badge: null },
  { key: 'tickets', label: 'Tickets', description: 'Lista e detalhes', badge: s.tickets.length },
  { key: 'kanban', label: 'Kanban', description: 'Fluxo por etapa', badge: s.tickets.filter((ticket) => ticket.status !== 'Concluido').length }
])
</script>
