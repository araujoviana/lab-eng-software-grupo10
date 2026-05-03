<template>
  <Login v-if="!s.token" />

  <div v-else class="app-shell">
    <Sidebar />

    <div class="app-main">
      <header class="topbar">
        <div class="page-intro">
          <p class="eyebrow">{{ pageMeta.eyebrow }}</p>
          <h1>{{ pageMeta.title }}</h1>
        </div>

        <div class="topbar-actions">
          <div class="user-chip">
            <strong>{{ s.user?.nome || s.user?.email }}</strong>
            <span>{{ primaryRole }}</span>
          </div>

          <button type="button" class="secondary-button" @click="refresh" :disabled="s.loading">
            Atualizar
          </button>

          <button v-if="canCreateTickets()" type="button" class="primary-button" @click="openModal">
            Novo ticket
          </button>

          <button type="button" class="ghost-button" @click="logout()">Sair</button>
        </div>
      </header>

      <div v-if="s.error" class="feedback is-error" role="alert">{{ s.error }}</div>
      <div v-else-if="s.message" class="feedback is-success" role="status">{{ s.message }}</div>

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
import { computed, onMounted } from 'vue'
import {
  canCreateTickets,
  loadExecutores,
  loadTickets,
  logout,
  store as s
} from './store'
import Sidebar from './components/Sidebar.vue'
import Dashboard from './components/Dashboard.vue'
import Tickets from './components/Tickets.vue'
import Kanban from './components/Kanban.vue'
import Modal from './components/Modal.vue'
import Login from './components/Login.vue'

const pageMap = {
  dashboard: {
    eyebrow: 'Painel operacional',
    title: 'Visão geral'
  },
  tickets: {
    eyebrow: 'Atendimento',
    title: 'Tickets'
  },
  kanban: {
    eyebrow: 'Fluxo de trabalho',
    title: 'Kanban'
  }
}

const roleLabels = {
  ROLE_ADMIN: 'Administrador',
  ROLE_TRIAGEM: 'Triagem',
  ROLE_EXECUTOR: 'Executor',
  ROLE_SOLICITANTE: 'Solicitante',
  ROLE_USUARIO: 'Usuário'
}

const pageMeta = computed(() => pageMap[s.page] || pageMap.dashboard)
const primaryRole = computed(() => roleLabels[s.user?.roles?.[0]] || 'Perfil')

const refresh = async () => {
  await loadTickets()
  await loadExecutores()
}

const openModal = () => {
  s.error = ''
  s.modal = true
}

onMounted(refresh)
</script>
