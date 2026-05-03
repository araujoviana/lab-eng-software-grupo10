const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const CLIENT_ID = import.meta.env.VITE_CLIENT_ID || 'zelodesk-client'
const CLIENT_SECRET = import.meta.env.VITE_CLIENT_SECRET || 'zelodesk-secret'

const toUrl = (path) => `${API_BASE_URL}${path}`

const fieldLabels = {
  descricao: 'Descrição',
  titulo: 'Título',
  categoria: 'Categoria',
  localPredio: 'Localização',
  prioridade: 'Prioridade',
  comentarioAtendimento: 'Comentário de atendimento',
  evidenciaUrl: 'Evidência final',
  texto: 'Comentário'
}

const prettifyMessage = (message) => {
  if (!message) {
    return 'Não foi possível concluir a operação.'
  }

  if (message === 'invalid_grant' || message === 'Bad credentials') {
    return 'E-mail ou senha inválidos.'
  }

  const translate = (text) => text
    .replaceAll('_', ' ')
    .replaceAll('Descricao', 'Descrição')
    .replaceAll('Titulo', 'Título')
    .replaceAll('Localizacao', 'Localização')
    .replaceAll('Evidencia', 'Evidência')
    .replaceAll('Comentario', 'Comentário')
    .replaceAll('Decisao', 'Decisão')
    .replaceAll('obrigatorio', 'obrigatório')
    .replaceAll('obrigatoria', 'obrigatória')
    .replaceAll('operacao', 'operação')
    .replaceAll('invalida', 'inválida')
    .replaceAll('nao', 'não')
    .replaceAll(' e obrigatório', ' é obrigatório')
    .replaceAll(' e obrigatória', ' é obrigatória')

  return message
    .split(';')
    .map((part) => part.trim())
    .filter(Boolean)
    .map((part) => {
      const [field, ...rest] = part.split(':')
      if (!rest.length) return translate(part)

      const label = fieldLabels[field.trim()] || field.trim()
      const text = translate(rest.join(':').trim())

      return `${label}: ${text}`
    })
    .join('\n')
}

const readError = async (response) => {
  try {
    const data = await response.json()
    return prettifyMessage(data.message || data.error)
  } catch {
    return 'Não foi possível concluir a operação.'
  }
}

export const loginRequest = async (email, password) => {
  const body = new URLSearchParams()
  body.set('grant_type', 'password')
  body.set('username', email)
  body.set('password', password)
  body.set('scope', 'read write')

  const response = await fetch(toUrl('/oauth2/token'), {
    method: 'POST',
    headers: {
      Authorization: `Basic ${btoa(`${CLIENT_ID}:${CLIENT_SECRET}`)}`,
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body
  })

  if (!response.ok) {
    throw new Error(await readError(response))
  }

  return response.json()
}

export const apiRequest = async (path, { method = 'GET', body, token } = {}) => {
  const headers = {
    Accept: 'application/json'
  }

  if (body !== undefined) {
    headers['Content-Type'] = 'application/json'
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(toUrl(path), {
    method,
    headers,
    body: body === undefined ? undefined : JSON.stringify(body)
  })

  if (!response.ok) {
    throw new Error(await readError(response))
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}
