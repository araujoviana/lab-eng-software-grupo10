<template>
  <aside class="sidebar">
    <div class="brand-block">
      <div class="brand-mark">ZD</div>

      <div class="brand-copy">
        <h2>Zelodesk</h2>
        <p>Gestao de zeladoria</p>
      </div>
    </div>

    <button type="button" class="primary-button sidebar-button" @click="s.modal = true">
      Novo ticket
    </button>

    <nav class="nav-list">
      <button
        v-for="item in items"
        :key="item.key"
        type="button"
        class="nav-item"
        :class="{ 'is-active': s.page === item.key }"
        @click="go(item.key)"
      >
        <span class="nav-label">{{ item.label }}</span>
        <span v-if="item.badge !== null" class="nav-badge">{{ item.badge }}</span>
      </button>
    </nav>

    <div class="sidebar-card">
      <div class="mini-metric">
        <span>Tickets ativos</span>
        <strong>{{ ativos }}</strong>
      </div>

      <div class="mini-metric">
        <span>Alta prioridade</span>
        <strong>{{ altaPrioridade }}</strong>
      </div>

      <div class="mini-metric">
        <span>Concluidos</span>
        <strong>{{ concluidos }}</strong>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { store as s } from '../store'

const ativos = computed(() => s.tickets.filter((ticket) => ticket.status !== 'Concluído').length)
const altaPrioridade = computed(() => s.tickets.filter((ticket) => ticket.prioridade === 'Alta').length)
const concluidos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Concluído').length)

const items = computed(() => [
  { key: 'dashboard', label: 'Visao geral', badge: null },
  { key: 'tickets', label: 'Tickets', badge: s.tickets.length },
  { key: 'kanban', label: 'Kanban', badge: ativos.value }
])

const go = (page) => {
  s.page = page
}
</script>
