import { reactive, watch } from 'vue'

const demoTickets = [
  {
    id: 1,
    titulo: 'Vazamento no bloco A',
    status: 'Aberto',
    prioridade: 'Alta',
    setor: 'Hidraulica',
    responsavel: 'Equipe Azul',
    descricao: 'Infiltracao identificada proxima ao corredor tecnico do segundo pavimento.'
  },
  {
    id: 2,
    titulo: 'Troca de luminarias do patio',
    status: 'Em Andamento',
    prioridade: 'Media',
    setor: 'Eletrica',
    responsavel: 'Equipe Noturna',
    descricao: 'Substituicao programada para melhorar a iluminacao de acesso e seguranca.'
  },
  {
    id: 3,
    titulo: 'Ajuste no ar-condicionado da recepcao',
    status: 'Concluído',
    prioridade: 'Media',
    setor: 'Climatizacao',
    responsavel: 'Tec. Marcos',
    descricao: 'Unidade revisada e calibrada apos queda de rendimento durante o expediente.'
  },
  {
    id: 4,
    titulo: 'Porta corta-fogo sem fechamento',
    status: 'Aberto',
    prioridade: 'Alta',
    setor: 'Seguranca',
    responsavel: 'Equipe Predial',
    descricao: 'Necessario ajustar mola e verificar a vedacao antes da vistoria interna.'
  },
  {
    id: 5,
    titulo: 'Pintura de faixa de circulacao',
    status: 'Concluído',
    prioridade: 'Baixa',
    setor: 'Infraestrutura',
    responsavel: 'Equipe Civil',
    descricao: 'Sinalizacao horizontal renovada na area de carga para organizar o fluxo.'
  },
  {
    id: 6,
    titulo: 'Limpeza tecnica da casa de bombas',
    status: 'Em Andamento',
    prioridade: 'Alta',
    setor: 'Operacao',
    responsavel: 'Equipe de Campo',
    descricao: 'Intervencao preventiva em andamento para evitar falhas no sistema principal.'
  }
]

const normalizeTicket = (ticket, index) => ({
  id: ticket.id ?? Date.now() + index,
  titulo: ticket.titulo || `Ticket ${index + 1}`,
  status: ticket.status || 'Aberto',
  prioridade: ticket.prioridade || 'Media',
  setor: ticket.setor || 'Operacao',
  responsavel: ticket.responsavel || 'Equipe local',
  descricao: ticket.descricao || 'Solicitacao cadastrada para acompanhamento da equipe.'
})

const loadTickets = () => {
  const saved = localStorage.getItem('tickets')

  if (!saved) {
    return demoTickets.map(normalizeTicket)
  }

  try {
    const parsed = JSON.parse(saved)
    return Array.isArray(parsed) ? parsed.map(normalizeTicket) : demoTickets.map(normalizeTicket)
  } catch {
    return demoTickets.map(normalizeTicket)
  }
}

const initialTickets = loadTickets()

export const store = reactive({
  page: 'dashboard',
  modal: false,
  selected: initialTickets[0] ?? null,
  tickets: initialTickets
})

watch(() => store.tickets, (tickets) => {
  localStorage.setItem('tickets', JSON.stringify(tickets))
}, { deep: true })
