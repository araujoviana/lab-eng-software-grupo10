<template>
  <div class="app-shell">
    <Sidebar />

    <div class="app-main">
      <header class="topbar topbar--clean">
        <div class="page-intro">
          <h1>{{ pageMeta.title }}</h1>
          <p>{{ pageMeta.description }}</p>
        </div>

        <div class="topbar-actions">
          <div class="topbar-summary">
            <div class="summary-chip">
              <span>Abertos</span>
              <strong>{{ abertos }}</strong>
            </div>

            <div class="summary-chip">
              <span>Em atendimento</span>
              <strong>{{ andamento }}</strong>
            </div>

            <div class="summary-chip">
              <span>Concluidos</span>
              <strong>{{ concluidos }}</strong>
            </div>
          </div>

          <button type="button" class="primary-button" @click="s.modal = true">Novo ticket</button>
        </div>
      </header>

      <main>
        <Dashboard v-if="s.page === 'dashboard'" />
        <Tickets v-else-if="s.page === 'tickets'" />
        <Kanban v-else-if="s.page === 'kanban'" />
      </main>
    </div>

    <Modal v-if="s.modal" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { store as s } from './store'
import Sidebar from './components/Sidebar.vue'
import Dashboard from './components/Dashboard.vue'
import Tickets from './components/Tickets.vue'
import Kanban from './components/Kanban.vue'
import Modal from './components/Modal.vue'

const pageMap = {
  dashboard: {
    title: 'Visao geral',
    description: 'Resumo rapido da operacao de zeladoria.'
  },
  tickets: {
    title: 'Tickets',
    description: 'Fila principal de chamados e prioridades.'
  },
  kanban: {
    title: 'Fluxo',
    description: 'Acompanhamento dos tickets por etapa.'
  }
}

const pageMeta = computed(() => pageMap[s.page] ?? pageMap.dashboard)
const abertos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Aberto').length)
const andamento = computed(() => s.tickets.filter((ticket) => ticket.status === 'Em Andamento').length)
const concluidos = computed(() => s.tickets.filter((ticket) => ticket.status === 'Concluído').length)
</script>
