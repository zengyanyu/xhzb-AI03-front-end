import request from '@/utils/request'
import { getToken } from '@/utils/auth'

/**
 * Dify 智能体 API 封装
 * 1. sendDifyChatMessage  流式聊天(fetch + SSE 解析)
 * 2. getDifyConversations 会话列表
 * 3. getDifyMessages      会话历史消息
 * 4. deleteDifyConversation 删除会话
 */

/**
 * 流式聊天(POST /dify/chat, SSE 流式响应)
 * @param {Object} params { query: 用户问题, conversationId: 会话ID(新会话传空字符串) }
 * @returns {Promise<Response>} fetch 原始 Response, 由调用方读取流
 */
export function sendDifyChatMessage({ query, conversationId }) {
  const baseURL = import.meta.env.VITE_APP_BASE_API || ''
  console.log('[dify] 发送流式聊天请求: query=', query, ', conversationId=', conversationId)
  return fetch(baseURL + '/dify/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': getToken() ? 'Bearer ' + getToken() : undefined
    },
    body: JSON.stringify({
      query: query,
      conversationId: conversationId || ''
    })
  })
}

/**
 * 会话列表
 * @param {Object} params { limit: 数量上限, lastId: 分页游标 }
 * @returns {Promise<Object>} Dify 原始响应 { data: [...], has_more, total }
 */
export function getDifyConversations(params) {
  console.log('[dify] 查询会话列表, params=', params)
  return request({
    url: '/dify/conversations',
    method: 'get',
    params: params || {}
  })
}

/**
 * 会话历史消息
 * @param {String} conversationId 会话ID
 * @param {Object} params { limit: 数量上限 }
 * @returns {Promise<Object>} Dify 原始响应 { data: [...] }
 */
export function getDifyMessages(conversationId, params) {
  console.log('[dify] 查询会话历史消息, conversationId=', conversationId)
  return request({
    url: '/dify/messages',
    method: 'get',
    params: { conversationId: conversationId, ...(params || {}) }
  })
}

/**
 * 删除会话
 * @param {String} conversationId 会话ID
 */
export function deleteDifyConversation(conversationId) {
  console.log('[dify] 删除会话, conversationId=', conversationId)
  return request({
    url: '/dify/conversations/' + conversationId,
    method: 'delete'
  })
}
