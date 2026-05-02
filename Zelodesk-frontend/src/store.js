import { reactive } from 'vue'
import { apiRequest, loginRequest } from './api'

const TOKEN_KEY = 'zelodesk_token'
const USER_KEY = 'zelodesk_user'

export const categorias = [
  { value: 'HIDRAULICA', label: 'Hidraulica' },
  { value: 'ELETRICA', label: 'Eletrica' },
  { value: 'LIMPEZA', label: 'Limpeza' },
  { value: 'MANUTENCAO', label: 'Manutencao' },
  { value: 'SEGURANCA', label: 'Seguranca' }
]

export const prioridades = [
  { value: 'ALTA', label: 'Alta' },
  { value: 'MEDIA', label: 'Media' },
  { value: 'BAIXA', label: 'Baixa' }
]

export const statusList = [
  'Aberto',
  'Em triagem',
  'Aguardando solicitante',
  'Em execucao',
  'Concluido',
  'Cancelado',
  'Encerrado'
]

const decodeJwt = (token) => {
  try {
    const payload = token.split('.')[1]
    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/')
    const padded = normalized.padEnd(normalized.length + ((4 - normalized.length % 4) % 4), '=')
    return JSON.parse(atob(padded))
  } catch {
    return {}
  }
}

const loadUser = () => {
  const saved = localStorage.getItem(USER_KEY)
  return saved ? JSON.parse(saved) : null
}

export const store = reactive({
  page: 'dashboard',
  modal: false,
  token: localStorage.getItem(TOKEN_KEY),
  user: loadUser(),
  tickets: [],
  selectedId: null,
  executores: [],
  loading: false,
  saving: false,
  error: '',
  message: '',
  filters: {
    status: '',
    categoria: '',
    busca: ''
  }
})

export const hasRole = (role) => Boolean(store.user?.roles?.includes(role))

export const hasAnyRole = (roles) => roles.some((role) => hasRole(role))

export const canCreateTickets = () => hasAnyRole(['ROLE_SOLICITANTE', 'ROLE_USUARIO', 'ROLE_ADMIN'])

export const canTriage = () => hasAnyRole(['ROLE_TRIAGEM', 'ROLE_ADMIN'])

export const canExecute = () => hasAnyRole(['ROLE_EXECUTOR', 'ROLE_ADMIN'])

export const priorityLabel = (value) => prioridades.find((item) => item.value === value)?.label || value || 'Sem prioridade'

export const categoryLabel = (value) => categorias.find((item) => item.value === value)?.label || value || 'Sem categoria'

export const statusClass = (status) => {
  if (status === 'Aberto') return 'is-open'
  if (status === 'Em triagem' || status === 'Aguardando solicitante') return 'is-triage'
  if (status === 'Em execucao') return 'is-progress'
  if (status === 'Concluido') return 'is-done'
  return 'is-muted'
}

export const priorityClass = (priority) => {
  if (priority === 'ALTA') return 'is-high'
  if (priority === 'BAIXA') return 'is-low'
  return 'is-medium'
}

export const ticketCode = (ticket) => ticket?.ticketCode || `TKT-${String(ticket?.id || 0).padStart(3, '0')}`

export const setMessage = (message) => {
  store.message = message
  store.error = ''
}

export const setError = (error) => {
  store.error = error
  store.message = ''
}

const persistSession = (token, user) => {
  localStorage.setItem(TOKEN_KEY, token)
  localStorage.setItem(USER_KEY, JSON.stringify(user))
  store.token = token
  store.user = user
}

const replaceTicket = (ticket) => {
  const index = store.tickets.findIndex((item) => item.id === ticket.id)
  if (index >= 0) {
    store.tickets.splice(index, 1, ticket)
  } else {
    store.tickets.unshift(ticket)
  }
  store.selectedId = ticket.id
}

export const selectedTicket = () => store.tickets.find((ticket) => ticket.id === store.selectedId) || store.tickets[0] || null

export const login = async (email, password) => {
  store.loading = true
  store.error = ''
  try {
    const tokenResponse = await loginRequest(email, password)
    const claims = decodeJwt(tokenResponse.access_token)
    const fallbackUser = {
      email: claims.username || email,
      nome: claims.username || email,
      roles: claims.authorities || []
    }

    persistSession(tokenResponse.access_token, fallbackUser)

    try {
      const me = await apiRequest('/usuarios/me', { token: store.token })
      persistSession(tokenResponse.access_token, me)
    } catch {
      persistSession(tokenResponse.access_token, fallbackUser)
    }

    await loadTickets()
    await loadExecutores()
    setMessage('Login realizado com sucesso.')
  } catch (error) {
    logout(false)
    setError(error.message)
  } finally {
    store.loading = false
  }
}

export const logout = (showMessage = true) => {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  store.token = null
  store.user = null
  store.tickets = []
  store.selectedId = null
  store.executores = []
  if (showMessage) {
    setMessage('Sessao encerrada.')
  }
}

export const loadTickets = async () => {
  if (!store.token) return
  store.loading = true
  try {
    const params = new URLSearchParams()
    if (store.filters.status) params.set('status', store.filters.status)
    if (store.filters.categoria) params.set('categoria', store.filters.categoria)
    const tickets = await apiRequest(`/tickets${params.toString() ? `?${params}` : ''}`, { token: store.token })
    store.tickets = tickets
    if (!store.selectedId && tickets.length) {
      store.selectedId = tickets[0].id
    }
    store.error = ''
  } catch (error) {
    setError(error.message)
  } finally {
    store.loading = false
  }
}

export const loadExecutores = async () => {
  if (!store.token || !canTriage()) return
  try {
    store.executores = await apiRequest('/usuarios/executores', { token: store.token })
  } catch {
    store.executores = []
  }
}

export const createTicket = async (payload) => {
  store.saving = true
  try {
    const ticket = await apiRequest('/tickets', {
      method: 'POST',
      token: store.token,
      body: payload
    })
    replaceTicket(ticket)
    setMessage('Ticket criado com sucesso.')
    return ticket
  } catch (error) {
    setError(error.message)
    throw error
  } finally {
    store.saving = false
  }
}

export const triageTicket = async (id, payload) => {
  store.saving = true
  try {
    const ticket = await apiRequest(`/tickets/${id}/triagem`, {
      method: 'PATCH',
      token: store.token,
      body: payload
    })
    replaceTicket(ticket)
    setMessage('Triagem registrada.')
    return ticket
  } catch (error) {
    setError(error.message)
    throw error
  } finally {
    store.saving = false
  }
}

export const assumeTicket = async (id) => {
  store.saving = true
  try {
    const ticket = await apiRequest(`/tickets/${id}/assumir`, {
      method: 'PATCH',
      token: store.token
    })
    replaceTicket(ticket)
    setMessage('Ticket assumido para execucao.')
    return ticket
  } catch (error) {
    setError(error.message)
    throw error
  } finally {
    store.saving = false
  }
}

export const concludeTicket = async (id, payload) => {
  store.saving = true
  try {
    const ticket = await apiRequest(`/tickets/${id}/concluir`, {
      method: 'PATCH',
      token: store.token,
      body: payload
    })
    replaceTicket(ticket)
    setMessage('Ticket concluido com evidencia.')
    return ticket
  } catch (error) {
    setError(error.message)
    throw error
  } finally {
    store.saving = false
  }
}

export const addComment = async (id, texto) => {
  store.saving = true
  try {
    const ticket = await apiRequest(`/tickets/${id}/comentarios`, {
      method: 'POST',
      token: store.token,
      body: { texto }
    })
    replaceTicket(ticket)
    setMessage('Comentario adicionado.')
    return ticket
  } catch (error) {
    setError(error.message)
    throw error
  } finally {
    store.saving = false
  }
}
