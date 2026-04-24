<template>
  <div class="modal-backdrop" @click.self="close">
    <div class="modal-card">
      <div class="modal-header">
        <h2 class="panel-title">Novo ticket</h2>

        <button type="button" class="icon-button" @click="close">Fechar</button>
      </div>

      <form class="form-grid" @submit.prevent="criar">
        <label class="form-field form-field-full">
          <span>Titulo</span>
          <input
            v-model="form.titulo"
            class="input"
            type="text"
            placeholder="Ex: Vazamento no bloco B"
          />
        </label>

        <label class="form-field">
          <span>Setor</span>
          <select v-model="form.setor" class="input">
            <option>Hidraulica</option>
            <option>Eletrica</option>
            <option>Climatizacao</option>
            <option>Infraestrutura</option>
            <option>Operacao</option>
            <option>Seguranca</option>
          </select>
        </label>

        <label class="form-field">
          <span>Prioridade</span>
          <select v-model="form.prioridade" class="input">
            <option>Alta</option>
            <option>Media</option>
            <option>Baixa</option>
          </select>
        </label>

        <label class="form-field form-field-full">
          <span>Responsavel</span>
          <input
            v-model="form.responsavel"
            class="input"
            type="text"
            placeholder="Equipe local"
          />
        </label>

        <label class="form-field form-field-full">
          <span>Descricao</span>
          <textarea
            v-model="form.descricao"
            class="textarea"
            placeholder="Descreva o chamado"
          ></textarea>
        </label>

        <div class="form-field form-field-full modal-actions">
          <button type="button" class="secondary-button" @click="close">Cancelar</button>
          <button type="submit" class="primary-button">Criar ticket</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { store as s } from '../store'

const createInitialForm = () => ({
  titulo: '',
  setor: 'Hidraulica',
  prioridade: 'Media',
  responsavel: 'Equipe local',
  descricao: ''
})

const form = reactive(createInitialForm())

const reset = () => {
  Object.assign(form, createInitialForm())
}

const close = () => {
  s.modal = false
  reset()
}

const criar = () => {
  const titulo = form.titulo.trim()

  if (!titulo) {
    return
  }

  const ticket = {
    id: Date.now(),
    titulo,
    status: 'Aberto',
    setor: form.setor,
    prioridade: form.prioridade,
    responsavel: form.responsavel.trim() || 'Equipe local',
    descricao: form.descricao.trim() || 'Chamado aberto para avaliacao inicial da equipe.'
  }

  s.tickets.unshift(ticket)
  s.selected = ticket
  s.page = 'tickets'
  close()
}
</script>
