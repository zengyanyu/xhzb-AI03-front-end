<template>
  <div class="chat-layout">
    <!-- 左侧会话列表 -->
    <div class="history-panel">
      <button class="add-chat-btn" @click="addChat">+ 新增对话</button>
      <div class="history-title">会话列表</div>
      <ul class="history-list">
        <li
          v-for="item in conversationList"
          :key="item.id"
          class="history-item"
          :class="{ active: item.id === currentConversationId }"
          @click="selectConversation(item)"
          style="display: flex; align-items: center; justify-content: space-between;"
        >
          <div style="flex: 1; overflow: hidden;">
            <div style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">{{ item.name }}</div>
            <div class="history-time">{{ formatTime(item.updatedAt) }}</div>
          </div>
          <span
            v-if="!item.isNew"
            @click.stop="handleDelete(item)"
            class="delete-btn"
            title="删除会话"
          >
            <svg width="14" height="14" viewBox="0 0 1024 1024" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M320 896c0 35.2 28.8 64 64 64h256c35.2 0 64-28.8 64-64V320H320v576zm576-704h-192l-32-64H352l-32-64H128v64h768v-64z" fill="#f56c6c"/>
            </svg>
          </span>
        </li>
      </ul>
    </div>

    <!-- 右侧聊天区 -->
    <div class="ai-chat-simple">
      <div class="chat-main">
        <div class="messages" ref="messagesRef">
          <div v-if="messages.length === 0" class="empty-tip">
            您好，我是 dify 小智，请问有什么可以帮您？
          </div>
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.role]"
          >
            <span class="role">
              <template v-if="message.role === 'user'"></template>
              <template v-else>
                <img src="@/assets/logo/ai_log.png" alt="AI Logo" class="ai-logo" />
              </template>
            </span>
            <div class="content" v-if="message.role === 'user'">{{ message.content }}</div>
            <div class="content" v-else>
              <span v-html="renderMarkdown(message.content)"></span>
              <span v-if="message.streaming" class="cursor-blink">▍</span>
            </div>
          </div>
        </div>
        <div class="input-area">
          <textarea
            v-model="userInput"
            @keydown="handleKeydown"
            placeholder="请输入您的问题... (Enter发送, Shift+Enter换行)"
            rows="1"
            ref="inputRef"
          ></textarea>
          <button class="send-button" @click="sendMessage" :disabled="isStreaming || !userInput.trim()">
            {{ isStreaming ? '回答中...' : '发送' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  sendDifyChatMessage,
  getDifyConversations,
  getDifyMessages,
  deleteDifyConversation
} from '@/api/nursing/dify'
import { marked } from 'marked'

const messagesRef = ref(null)
const inputRef = ref(null)
const userInput = ref('')
const isStreaming = ref(false)
const messages = ref([])
/** 当前会话ID, 空字符串表示新会话(Dify会在第一条消息响应中返回新会话ID) */
const currentConversationId = ref('')
/** 会话列表 [{ id, name, updatedAt, isNew }] */
const conversationList = ref([])

// ==================== 会话列表 ====================

/** 加载会话列表 */
const loadConversations = async () => {
  try {
    console.log('[dify] 开始加载会话列表')
    const res = await getDifyConversations({ limit: 100 })
    const list = res.data && res.data.data ? res.data.data : []
    console.log('[dify] 会话列表加载成功, 数量:', list.length, list)
    conversationList.value = list.map(item => ({
      id: item.id,
      name: item.name || '未命名会话',
      updatedAt: item.updated_at,
      isNew: false
    }))
  } catch (e) {
    console.error('[dify] 加载会话列表失败:', e)
    ElMessage.error('加载会话列表失败: ' + (e.message || e))
  }
}

/** 新增对话 */
const addChat = () => {
  if (isStreaming.value) {
    ElMessage.warning('请等待当前回答完成')
    return
  }
  console.log('[dify] 新增对话')
  currentConversationId.value = ''
  messages.value = []
  // 在列表头部插入一个占位的新会话项
  if (!conversationList.value.some(item => item.isNew)) {
    conversationList.value.unshift({ id: '__new__', name: '新会话', updatedAt: null, isNew: true })
  }
}

/** 切换会话并加载历史消息 */
const selectConversation = async (item) => {
  if (isStreaming.value) {
    ElMessage.warning('请等待当前回答完成')
    return
  }
  if (item.isNew) {
    currentConversationId.value = ''
    messages.value = []
    return
  }
  console.log('[dify] 切换会话:', item.id, item.name)
  currentConversationId.value = item.id
  messages.value = []
  await loadMessages(item.id)
}

/** 加载指定会话的历史消息 */
const loadMessages = async (conversationId) => {
  try {
    console.log('[dify] 加载历史消息, conversationId=', conversationId)
    const res = await getDifyMessages(conversationId, { limit: 100 })
    const list = res.data && res.data.data ? res.data.data : []
    console.log('[dify] 历史消息加载成功, 数量:', list.length, list)
    // Dify 每条记录包含 query(用户问题) 和 answer(助手回答), 展开为两条消息
    const result = []
    for (const item of list) {
      if (item.query) {
        result.push({ role: 'user', content: item.query })
      }
      if (item.answer) {
        result.push({ role: 'assistant', content: item.answer })
      }
    }
    messages.value = result
    await scrollToBottom()
  } catch (e) {
    console.error('[dify] 加载历史消息失败:', e)
    ElMessage.error('加载历史消息失败: ' + (e.message || e))
  }
}

/** 删除会话 */
const handleDelete = async (item) => {
  try {
    await ElMessageBox.confirm('确定删除会话「' + item.name + '」吗？删除后聊天记录不可恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return // 用户取消
  }
  try {
    console.log('[dify] 删除会话:', item.id)
    const res = await deleteDifyConversation(item.id)
    console.log('[dify] 删除会话结果:', res)
    ElMessage.success('删除成功')
    // 若删除的是当前会话, 重置为空的新会话
    if (item.id === currentConversationId.value) {
      currentConversationId.value = ''
      messages.value = []
    }
    await loadConversations()
    // 删除后如果当前没有选中会话且存在历史会话, 自动加载第一个
    if (!currentConversationId.value && conversationList.value.length > 0) {
      const first = conversationList.value[0]
      currentConversationId.value = first.id
      await loadMessages(first.id)
    } else if (conversationList.value.length === 0) {
      addChat()
    }
  } catch (e) {
    console.error('[dify] 删除会话失败:', e)
    ElMessage.error('删除会话失败: ' + (e.message || e))
  }
}

// ==================== 流式聊天 ====================

const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

/** 发送消息(流式) */
const sendMessage = async () => {
  if (isStreaming.value) return
  if (!userInput.value.trim()) return
  const query = userInput.value.trim()
  console.log('[dify] 发送消息: query=', query, ', conversationId=', currentConversationId.value)
  messages.value.push({ role: 'user', content: query })
  userInput.value = ''
  await scrollToBottom()

  const aiMsg = { role: 'assistant', content: '', streaming: true }
  messages.value.push(aiMsg)
  isStreaming.value = true

  try {
    const response = await sendDifyChatMessage({
      query,
      conversationId: currentConversationId.value
    })
    if (!response.ok) {
      const errText = await response.text()
      console.error('[dify] 聊天接口HTTP错误: status=', response.status, ', body=', errText)
      throw new Error('HTTP ' + response.status + ': ' + errText)
    }
    if (!response.body) {
      throw new Error('无流式响应体')
    }

    // ====== 解析 SSE 流 ======
    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''
    let eventCount = 0
    let conversationIdReceived = ''

    while (true) {
      const { value, done } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })

      // SSE 帧以空行(\n\n)分隔
      let sepIndex
      while ((sepIndex = buffer.indexOf('\n\n')) !== -1) {
        const frame = buffer.slice(0, sepIndex)
        buffer = buffer.slice(sepIndex + 2)
        const parsed = parseSseFrame(frame)
        if (!parsed) continue
        eventCount++
        // 处理每一个 Dify 事件
        handleDifyEvent(parsed.eventName, parsed.data, aiMsg, (cid) => { conversationIdReceived = cid })
        await scrollToBottom()
      }
    }
    // 处理残余缓冲
    if (buffer.trim()) {
      const parsed = parseSseFrame(buffer)
      if (parsed) {
        handleDifyEvent(parsed.eventName, parsed.data, aiMsg, (cid) => { conversationIdReceived = cid })
      }
    }
    console.log('[dify] 流式读取完毕, 共收到事件数:', eventCount, ', conversationId=', conversationIdReceived)
    if (!aiMsg.content) {
      aiMsg.content = '(未收到回答内容)'
    }
  } catch (e) {
    console.error('[dify] 流式聊天失败:', e)
    aiMsg.content = '抱歉，发生了错误，请稍后重试。' + (e.message ? ('\n(' + e.message + ')') : '')
    ElMessage.error('聊天失败: ' + (e.message || e))
  } finally {
    aiMsg.streaming = false
    isStreaming.value = false
    await scrollToBottom()
    // 刷新会话列表(新会话结束后列表中会出现新记录)
    await loadConversations()
  }
}

/** 解析单个 SSE 帧, 返回 { eventName, data } */
const parseSseFrame = (frame) => {
  const lines = frame.split('\n')
  let eventName = 'message'
  let dataLines = []
  for (const line of lines) {
    if (line.startsWith('event:')) {
      eventName = line.slice(6).trim()
    } else if (line.startsWith('data:')) {
      dataLines.push(line.slice(5).trim())
    }
  }
  if (dataLines.length === 0) {
    return null
  }
  let data = dataLines.join('\n')
  // data 为 JSON 字符串
  try {
    data = JSON.parse(data)
  } catch (e) {
    console.warn('[dify] 事件数据非JSON, 原样返回:', data)
  }
  return { eventName, data }
}

/** 处理 Dify SSE 事件 */
const handleDifyEvent = (eventName, data, aiMsg, setConversationId) => {
  switch (eventName) {
    case 'message':
    case 'agent_message':
      // 增量回答
      if (data && data.answer) {
        aiMsg.content += data.answer
        // Dify 会随事件返回 conversation_id
        if (data.conversation_id) {
          setConversationId(data.conversation_id)
          if (currentConversationId.value !== data.conversation_id) {
            console.log('[dify] 更新当前会话ID为:', data.conversation_id)
            currentConversationId.value = data.conversation_id
          }
        }
      }
      break
    case 'message_end':
      // 回答结束
      console.log('[dify] message_end 事件, 元数据:', data)
      if (data && data.conversation_id) {
        setConversationId(data.conversation_id)
        currentConversationId.value = data.conversation_id
      }
      break
    case 'error':
      // Dify 或后端返回的错误事件
      console.error('[dify] 收到 error 事件:', data)
      aiMsg.content += '\n\n[错误] ' + ((data && (data.message || data.code)) || '未知错误')
      break
    case 'ping':
      console.debug('[dify] ping 心跳')
      break
    default:
      // workflow_started / node_started / node_finished / workflow_finished / agent_thought / message_file 等
      console.log('[dify] 忽略事件:', eventName, data)
      break
  }
}

/** 输入框回车发送 */
const handleKeydown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

/** 渲染markdown */
const renderMarkdown = (text) => {
  return marked.parse(text || '')
}

/** 格式化时间 */
const formatTime = (ts) => {
  if (!ts) return ''
  const d = new Date(ts * 1000)
  const pad = (n) => String(n).padStart(2, '0')
  return (d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes())
}

// ==================== 初始化 ====================

onMounted(async () => {
  console.log('[dify] 页面初始化')
  await loadConversations()
  // 默认选中第一个历史会话
  if (conversationList.value.length > 0) {
    currentConversationId.value = conversationList.value[0].id
    await loadMessages(currentConversationId.value)
  }
})
</script>

<style scoped>
.chat-layout {
  display: flex;
  height: calc(100vh - 85px);
  background: var(--bg-color, #f5f6fa);
}

.history-panel {
  width: 220px;
  background: #f7f7fa;
  border-right: 1px solid #e0e0e0;
  padding: 2rem 1rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.add-chat-btn {
  width: 100%;
  padding: 0.75em 0;
  margin-bottom: 1em;
  background: #007cf0;
  color: #fff;
  border: none;
  border-radius: 0.5em;
  font-size: 1em;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
.add-chat-btn:hover {
  background: #005fa3;
}

.history-title {
  font-weight: bold;
  font-size: 1.1em;
  margin-bottom: 1rem;
}

.history-list {
  list-style: none;
  padding: 0;
  margin: 0;
  flex: 1;
  overflow-y: auto;
}

.history-item {
  padding: 0.75em 0.5em;
  border-radius: 0.5em;
  cursor: pointer;
  transition: background 0.2s;
}
.history-item:hover {
  background: #e6f0fd;
}
.history-item.active {
  background: #e6f0fd;
  font-weight: bold;
}
.history-time {
  font-size: 12px;
  color: #999;
  font-weight: normal;
}
.delete-btn {
  margin-left: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.ai-chat-simple {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-color, #f5f6fa);
  box-sizing: border-box;
  padding: 30px;
  font-size: 1.05rem;
}
.chat-main {
  width: 100%;
  min-width: 800px;
  margin: 0;
  background: #fff;
  display: flex;
  flex-direction: column;
  height: 90vh;
  min-height: 605px;
  overflow: hidden;
  border-radius: 8px;
}
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem 1rem;
  box-sizing: border-box;
}
.empty-tip {
  text-align: center;
  color: #999;
  margin-top: 40px;
}
.message {
  width: 100%;
  display: flex;
  margin-bottom: 1rem;
}
.message.user {
  justify-content: flex-end;
}
.message.user .content {
  background: #e6f0fd;
  color: #222;
  border-radius: 1em 0.2em 1em 1.25em;
  padding: 0.5em 1.2em;
  max-width: 90%;
  word-break: break-all;
  display: block;
  text-align: left;
  margin-left: 3rem;
  margin-right: 0;
  font-size: 16px;
}
.message.assistant {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}
.message.assistant .content {
  color: #222;
  border-radius: 0.2em 1.25em 1.25em 1.25em;
  padding: 0.5em 1.2em;
  max-width: 90%;
  word-break: break-all;
  display: block;
  text-align: left;
  margin-right: 3rem;
  margin-left: 8px;
  font-size: 16px;
  flex: 1;
}
.message.assistant .role {
  color: #ff9800;
  font-weight: bold;
  margin-right: 0;
  flex-shrink: 0;
}
.ai-logo {
  width: 24px;
  height: 24px;
  vertical-align: top;
  border-radius: 6px;
  background: #fff;
}
.cursor-blink {
  animation: blink 1s infinite;
  color: #007cf0;
}
@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}
.input-area {
  display: flex;
  gap: 0.5rem;
  align-items: flex-end;
  background: #fafbfc;
  padding: 1.5rem 2rem;
  border-top: 1px solid #eee;
}
textarea {
  flex: 1;
  resize: none;
  border: none;
  background: transparent;
  padding: 1rem;
  font-size: 16px;
  line-height: 24px;
  max-height: 180px;
  border-radius: 0.75rem;
  outline: none;
  min-height: 1.5rem;
}
.send-button {
  min-width: 80px;
  height: 3rem;
  border: none;
  border-radius: 0.75rem;
  background: #007cf0;
  color: #fff;
  font-weight: bold;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background 0.2s;
}
.send-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>

<style>
/* markdown 渲染全局样式(与旧小智页面保持一致) */
.content p {
  font-size: 16px;
}
.content table {
  border-collapse: collapse;
  width: 100%;
  margin: 1em 0;
}
.content th,
.content td {
  border: 1px solid #d0d7de;
  padding: 8px 12px;
  text-align: left;
}
.content th {
  background: #f6f8fa;
  font-weight: bold;
}
.content pre {
  background: #f6f8fa;
  padding: 1em;
  border-radius: 6px;
  overflow-x: auto;
}
.content code {
  font-family: 'Fira Mono', 'Consolas', 'Menlo', monospace;
  font-size: 1em;
}
</style>
