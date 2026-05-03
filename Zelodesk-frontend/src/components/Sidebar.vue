<template>
  <aside class="sidebar">
    <div class="brand-block">
      <div class="brand-mark">ZD</div>
      <div class="brand-copy">
        <h2>Zelo Desk</h2>
        <p>Tickets para condomínios</p>
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
        <strong>{{ item.label }}</strong>
        <span v-if="item.badge !== null" class="nav-badge">{{ item.badge }}</span>
      </button>
    </nav>

    <section class="sidebar-card" aria-label="Resumo dos tickets">
      <div class="mini-metric">
        <span>Abertos</span>
        <strong>{{ abertos }}</strong>
      </div>
      <div class="mini-metric">
        <span>Em execução</span>
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
  { key: 'dashboard', label: 'Visão geral', badge: null },
  { key: 'tickets', label: 'Tickets', badge: s.tickets.length },
  { key: 'kanban', label: 'Kanban', badge: s.tickets.filter((ticket) => ticket.status !== 'Concluido').length }
])
</script>
