const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const CLIENT_ID = import.meta.env.VITE_CLIENT_ID || 'zelodesk-client'
const CLIENT_SECRET = import.meta.env.VITE_CLIENT_SECRET || 'zelodesk-secret'

const toUrl = (path) => `${API_BASE_URL}${path}`

const readError = async (response) => {
  try {
    const data = await response.json()
    return data.message || data.error || 'Nao foi possivel concluir a operacao.'
  } catch {
    return 'Nao foi possivel concluir a operacao.'
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
