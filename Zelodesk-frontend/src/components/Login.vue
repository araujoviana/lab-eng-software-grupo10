<template>
  <main class="login-page">
    <section class="login-card" aria-labelledby="login-title">
      <div class="login-brand">
        <span class="brand-mark">ZD</span>
        <div>
          <p class="eyebrow">Zelo Desk</p>
          <h1 id="login-title">Acesse o painel de zeladoria</h1>
        </div>
      </div>

      <p class="login-copy">
        Use um perfil de teste para abrir chamados, fazer triagem ou concluir atendimentos.
      </p>

      <div v-if="s.error" class="feedback is-error" role="alert">{{ s.error }}</div>

      <form class="login-form" @submit.prevent="submit">
        <label class="form-field">
          <span>Email</span>
          <input v-model="form.email" class="input" type="email" autocomplete="email" required />
        </label>

        <label class="form-field">
          <span>Senha</span>
          <input v-model="form.password" class="input" type="password" autocomplete="current-password" required />
        </label>

        <button type="submit" class="primary-button" :disabled="s.loading">
          {{ s.loading ? 'Entrando...' : 'Entrar' }}
        </button>
      </form>

      <div class="profile-list" aria-label="Perfis de teste">
        <button
          v-for="profile in profiles"
          :key="profile.email"
          type="button"
          class="profile-card"
          @click="fill(profile.email)"
        >
          <strong>{{ profile.label }}</strong>
          <span>{{ profile.email }}</span>
        </button>
      </div>
    </section>
  </main>
</template>

<script setup>
import { reactive } from 'vue'
import { login, store as s } from '../store'

const form = reactive({
  email: 'solicitante@zelodesk.com',
  password: '123456'
})

const profiles = [
  { label: 'Solicitante', email: 'solicitante@zelodesk.com' },
  { label: 'Triagem', email: 'triagem@zelodesk.com' },
  { label: 'Executor', email: 'executor@zelodesk.com' },
  { label: 'Admin', email: 'admin@zelodesk.com' }
]

const fill = (email) => {
  form.email = email
  form.password = '123456'
}

const submit = () => login(form.email, form.password)
</script>
