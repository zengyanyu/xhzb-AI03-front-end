import { getToken } from '@/utils/auth'

export function sendChatMessage({ prompt, chatId }) {
  const baseURL = import.meta.env.VITE_APP_BASE_API || ''
  return fetch(baseURL + '/ai/chat?prompt=' + prompt + '&chatId=' + chatId, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken() ? 'Bearer ' + getToken() : undefined
    }
  })
}

export function getChatHistoryIds() {
  const baseURL = import.meta.env.VITE_APP_BASE_API || ''
  return fetch(baseURL + '/ai/history', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken() ? 'Bearer ' + getToken() : undefined
    }
  })
    .then(res => res.json())
    .then(res => {
      if (res.code === 200) {
        return { data: res.data || [] }
      } else {
        throw new Error(res.msg || '获取历史记录失败')
      }
    })
}

export function getChatHistoryDetail(chatId) {
  const baseURL = import.meta.env.VITE_APP_BASE_API || ''
  return fetch(baseURL + '/ai/history/' + chatId, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken() ? 'Bearer ' + getToken() : undefined
    }
  })
    .then(res => res.json())
    .then(res => {
      if (res.code === 200) {
        return res.data || []
      } else {
        throw new Error(res.msg || '获取历史对话详情失败')
      }
    })
}

export function deleteChatHistory(chatId) {
  const baseURL = import.meta.env.VITE_APP_BASE_API || ''
  return fetch(baseURL + '/ai/history/' + chatId, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken() ? 'Bearer ' + getToken() : undefined
    }
  }).then(res => res.json())
}