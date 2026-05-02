<template>
  <div class="modal-backdrop" @click.self="close">
    <section class="modal-card" aria-labelledby="new-ticket-title">
      <div class="modal-header">
        <div>
          <p class="eyebrow">UC-01</p>
          <h2 id="new-ticket-title" class="panel-title">Abrir ticket de zeladoria</h2>
        </div>
        <button type="button" class="icon-button" @click="close" aria-label="Fechar modal">Fechar</button>
      </div>

      <form class="form-grid" @submit.prevent="submit">
        <label class="form-field form-field-full">
          <span>Titulo</span>
          <input
            v-model="form.titulo"
            class="input"
            type="text"
            placeholder="Ex: Vazamento no bloco B"
            required
          />
        </label>

        <label class="form-field">
          <span>Categoria</span>
          <select v-model="form.categoria" class="input" required>
            <option v-for="categoria in categorias" :key="categoria.value" :value="categoria.value">
              {{ categoria.label }}
            </option>
          </select>
        </label>

        <label class="form-field">
          <span>Urgencia</span>
          <select v-model="form.prioridade" class="input" required>
            <option v-for="prioridade in prioridades" :key="prioridade.value" :value="prioridade.value">
              {{ prioridade.label }}
            </option>
          </select>
        </label>

        <label class="form-field form-field-full">
          <span>Localizacao</span>
          <input
            v-model="form.localPredio"
            class="input"
            type="text"
            placeholder="Ex: Bloco A - 2o andar"
            required
          />
        </label>

        <label class="form-field form-field-full">
          <span>Foto ou anexo (URL opcional)</span>
          <input v-model="form.anexoUrl" class="input" type="url" placeholder="https://..." />
        </label>

        <label class="form-field form-field-full">
          <span>Descricao</span>
          <textarea
            v-model="form.descricao"
            class="textarea"
            placeholder="Descreva o problema com detalhes suficientes para a triagem"
            required
          ></textarea>
        </label>

        <div class="form-field form-field-full modal-actions">
          <button type="button" class="secondary-button" @click="close">Cancelar</button>
          <button type="submit" class="primary-button" :disabled="s.saving">
            {{ s.saving ? 'Criando...' : 'Criar ticket' }}
          </button>
        </div>
      </form>
    </section>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { categorias, createTicket, prioridades, store as s } from '../store'

const initialForm = () => ({
  titulo: '',
  categoria: 'HIDRAULICA',
  prioridade: 'MEDIA',
  localPredio: '',
  anexoUrl: '',
  descricao: ''
})

const form = reactive(initialForm())

const reset = () => Object.assign(form, initialForm())

const close = () => {
  s.modal = false
  reset()
}

const submit = async () => {
  await createTicket({
    titulo: form.titulo.trim(),
    descricao: form.descricao.trim(),
    categoria: form.categoria,
    localPredio: form.localPredio.trim(),
    prioridade: form.prioridade,
    solicitador: s.user?.nome || s.user?.email,
    anexoUrl: form.anexoUrl.trim() || null
  })
  s.page = 'tickets'
  close()
}
</script>
