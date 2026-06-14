<template>
  <div class="chat-layout">
    <div class="history-panel">
      <button class="add-chat-btn" @click="addChat">+ 新增对话</button>
      <!-- 这里放历史记录内容 -->
      <div class="history-title">历史记录</div>
      <!-- 可用 v-for 渲染历史会话列表 -->
      <ul class="history-list">
        <li
          v-for="item in historyList"
          :key="item.id"
          class="history-item"
          :class="{ active: item.id === currentChatId }"
          @click="selectChat(item)"
          style="display: flex; align-items: center; justify-content: space-between;"
        >
          <span style="flex:1;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">{{ item.name }}</span>
          <span v-if="!isTempId(item.id)" @click.stop="handleDelete(item.id)" style="margin-left:8px;cursor:pointer;display:flex;align-items:center;">
            <svg width="14" height="14" viewBox="0 0 1024 1024" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M320 896c0 35.2 28.8 64 64 64h256c35.2 0 64-28.8 64-64V320H320v576zm576-704h-192l-32-64H352l-32 64H128v64h768v-64z" fill="#f56c6c"/>
            </svg>
          </span>
        </li>
      </ul>
    </div>
    <div class="ai-chat-simple" :class="{ 'dark': isDark }">
      <!-- 原有聊天内容不变 -->
      <div class="chat-main">
        <div class="messages" ref="messagesRef">
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
            <div class="content" v-else v-html="renderMarkdown(message.content)"></div>
          </div>
        </div>
        <div class="input-area">
          <textarea
            v-model="userInput"
            @keydown="handleKeydown"
            placeholder="请输入您的问题..."
            rows="1"
            ref="inputRef"
          ></textarea>
          <button class="send-button" @click="sendMessage" :disabled="isStreaming || !userInput.trim()">
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useDark } from '@vueuse/core'
import { sendChatMessage, getChatHistoryIds, getChatHistoryDetail, deleteChatHistory } from '@/api/nursing/ai'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

// 配置 marked 使用 highlight.js
marked.setOptions({
  highlight: function (code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value
    }
    return hljs.highlightAuto(code).value
  }
})

const isDark = useDark()
const messagesRef = ref(null)
const inputRef = ref(null)
const userInput = ref('')
const isStreaming = ref(false)
const messages = ref([])
const currentChatId = ref(null) // 新对话时为null
const isNewChat = ref(true) // 标识是否为新对话

const historyList = ref([])

// 生成唯一ID（临时ID，用于新对话）- 使用时间戳数值
const generateTempId = () => {
  return Date.now()
}

// 数据转换函数 - 适配后端返回的 {role, content} 格式
const transformHistoryData = (historyData) => {
  if (!historyData || !Array.isArray(historyData)) return []
  // 后端已经返回 {role, content} 格式，直接返回
  return historyData.map(item => ({
    role: item.role,
    content: item.content
  }))
}

// 处理历史记录数据 - 后端返回的是ID数组 ["id1", id2, ...]
const processHistoryData = (data) => {
  if (!data || !Array.isArray(data)) return []
  return data.map(id => {
    const idStr = String(id)
    const shortId = idStr.slice(-6) // 取后6位
    return {
      id: id,
      name: '会话 ' + shortId
    }
  })
}

onMounted(async () => {
  const response = await getChatHistoryIds()
  if (response.data.length === 0) {
    // 没有历史记录，创建新对话
    const tempId = generateTempId()
    historyList.value = [{ id: tempId, name: '新会话' }]
    currentChatId.value = tempId
    isNewChat.value = true
  } else {
    // 有历史记录，默认选择第一个历史对话
    historyList.value = processHistoryData(response.data)
    currentChatId.value = historyList.value[0]?.id
    isNewChat.value = false
    // 自动加载第一个会话内容
    const detail = await getChatHistoryDetail(historyList.value[0]?.id)
    messages.value = transformHistoryData(detail)
  }
})

// 新增对话
const addChat = () => {
  // 新对话生成临时ID
  currentChatId.value = generateTempId()
  isNewChat.value = true
  historyList.value.unshift({ id: currentChatId.value, name: '新会话' })
  messages.value = []
}

// 判断是否为临时ID（新对话）
const isTempId = (id) => {
  return typeof id === 'number' && id > Date.now() - 3600000 // 1小时内的临时ID
}

// 切换对话
const selectChat = async (item) => {
  currentChatId.value = item.id
  isNewChat.value = isTempId(item.id)
  messages.value = []
  // 只有历史会话才加载内容
  if (!isTempId(item.id)) {
    const detail = await getChatHistoryDetail(item.id)
    messages.value = transformHistoryData(detail)
  }
}

const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

const sendMessage = async () => {
  if (isStreaming.value) return
  if (!userInput.value.trim()) return
  const prompt = userInput.value.trim()
  messages.value.push({ role: 'user', content: prompt })
  userInput.value = ''
  await scrollToBottom()
  const aiMsg = { role: 'assistant', content: '' }
  messages.value.push(aiMsg)
  isStreaming.value = true
  try {
    // 新对话使用临时ID，历史对话使用已有ID
    const chatId = currentChatId.value
    const response = await sendChatMessage({ prompt, chatId })
    if (!response.body) throw new Error('无流式响应')
    
    // 检查响应头中是否包含新的会话ID（新对话时）
    if (isNewChat.value) {
      // 调试：打印所有响应头
      console.log('响应头信息:', response.headers)
      const newChatId = response.headers.get('X-Chat-Id') || response.headers.get('chat-id') || response.headers.get('Chat-Id')
      console.log('尝试获取的会话ID:', newChatId)
      if (newChatId) {
        currentChatId.value = newChatId
        // 更新历史列表中对应的项
        const newChatItem = historyList.value.find(item => item.id === null)
        if (newChatItem) {
          newChatItem.id = newChatId
        }
        console.log('成功设置会话ID:', newChatId)
      } else {
        console.log('未在响应头中找到会话ID')
      }
    }
    
    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let accumulated = ''

    while (true) {
      try {
        const { value, done } = await reader.read()
        if (done) break
        
        // 累积新内容
        accumulated += decoder.decode(value)  // 追加新内容
        
        await nextTick(() => {
          // 更新消息，保留role并更新content
          const lastIndex = messages.value.length - 1
          messages.value[lastIndex] = {
            ...messages.value[lastIndex],
            content: accumulated
          }
        })
        await scrollToBottom()
      } catch (readError) {
        console.error('读取流错误:', readError)
        break
      }
    }

  } catch (e) {
    aiMsg.content = '抱歉，发生了错误，请稍后重试。'
  } finally {
    isStreaming.value = false
    await scrollToBottom()
    
    // 会话结束后，刷新历史列表（异步执行，不阻塞UI）
    refreshHistoryList()
  }
}

// 处理输入框回车和换行
const handleKeydown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
  // Shift+Enter 默认行为为换行，无需处理
}

// 渲染markdown为html
const renderMarkdown = (text) => {
  return marked.parse(text || '')
}

// 刷新历史列表
const refreshHistoryList = async () => {
  try {
    const response = await getChatHistoryIds()
    console.log('刷新历史列表响应:', response)
    
    if (response.data && response.data.length > 0) {
      // 更新历史列表
      historyList.value = processHistoryData(response.data)
      
      // 如果是新对话，尝试匹配最新的历史记录
      if (isNewChat.value && messages.value.length > 0) {
        // 找到第一个用户消息作为对话名称
        const firstUserMessage = messages.value.find(msg => msg.role === 'user')
        if (firstUserMessage) {
          // 尝试通过消息内容匹配找到对应的历史记录
          // 查找名称包含第一个用户消息的历史记录
          const matchingItem = response.data.find(item => 
            item.name && item.name.includes(firstUserMessage.content.substring(0, 10))
          )
          
          if (matchingItem) {
            // 找到匹配的历史记录，更新当前对话ID
            currentChatId.value = matchingItem.id
            isNewChat.value = false
            console.log('通过消息内容匹配到历史记录:', matchingItem)
          } else {
            // 如果没有找到匹配的，可能是最新的记录
            // 检查是否有新的记录（通过时间戳或其他标识）
            const latestItem = response.data[0] // 假设最新的记录在第一位
            if (latestItem && !latestItem.name.includes('新会话')) {
              // 如果最新的记录不是"新会话"，可能是我们刚创建的
              currentChatId.value = latestItem.id
              isNewChat.value = false
              console.log('使用最新的历史记录:', latestItem)
            }
          }
          
          // 更新对话名称
          const currentHistoryItem = historyList.value.find(item => item.id === currentChatId.value)
          if (currentHistoryItem) {
            currentHistoryItem.name = firstUserMessage.content.length > 20 
              ? firstUserMessage.content.substring(0, 20) + '...' 
              : firstUserMessage.content
          }
        }
      }
      
      // 如果不是新对话，调用当前对话的历史详情接口更新消息记录
      if (!isNewChat.value && currentChatId.value) {
        try {
          const detail = await getChatHistoryDetail(currentChatId.value)
          messages.value = transformHistoryData(detail)
        } catch (detailError) {
          console.error('获取当前对话历史详情失败:', detailError)
        }
      }
    }
  } catch (error) {
    console.error('刷新历史列表失败:', error)
  }
}

// 删除时刷新逻辑
const handleDelete = async (chatId) => {
  // 只有后端的历史记录才调用删除接口
  if (!isTempId(chatId)) {
    await deleteChatHistory(chatId)
  }
  // 重新获取历史会话id
  const response = await getChatHistoryIds()
  if (response.data.length === 0) {
    // 没有历史会话，创建新对话
    const tempId = generateTempId()
    historyList.value = [{ id: tempId, name: '新会话' }]
    currentChatId.value = tempId
    isNewChat.value = true
    messages.value = []
  } else {
    historyList.value = processHistoryData(response.data)
    // 如果当前删除的是选中的会话，则切到第一个
    if (currentChatId.value === chatId) {
      currentChatId.value = historyList.value[0]?.id
      isNewChat.value = false
      messages.value = []
    }
  }
}
</script>

<style scoped>
.chat-layout {
  display: flex;
  /* width: 100vw; */
  height:calc( 100vh - 85px);
  background: var(--bg-color, #f5f6fa);
}

.history-panel {
  width: 200px;
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
.history-item.active {
  background: #e6f0fd;
  font-weight: bold;
}

.history-title {
  font-weight: bold;
  font-size: 1.1em;
  margin-bottom: 1.5rem;
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
.ai-chat-simple {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-start;
  background: var(--bg-color, #f5f6fa);
  box-sizing: border-box;
  padding: 30px;
  font-size: 1.05rem;
}
.chat-main {
  width: 100%;
  /* max-width: 1110px; */
  min-width:800px;
  margin: 0;
  background: #fff;
  display: flex;
  flex-direction: column;
  height: 90vh; /* 放大高度 */
  min-height: 605px;
  overflow: hidden;
  border-radius:8px;
}
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem 1rem; /* 放大内边距 */
  box-sizing: border-box;
}
.message {
  width: 100%;
  display: flex;
  margin-bottom: 1rem; /* 放大间距 */
}
.message.user {
  justify-content: flex-end;
}
.message .content {
  /* 内容区域样式 */
}
.message.user .content {
  background: #e6f0fd;
  color: #222;
  border-radius: 1.0em 0.2em 1.0em 1.25em; /* 放大圆角 */
  padding: 0.50em 1.2em; /* 放大气泡内边距 */
  max-width: 90%; /* 放大最大宽度 */
  word-break: break-all;
  display: block;
  text-align: left;
  margin-left: 3rem;
  margin-right: 0;
  font-size: 16px;
}
.message.user .role {
  color: #007CF0;
  font-weight: bold;
  margin-left: 0.5em;
  margin-right: 0;
}
.message.assistant {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}
.message.assistant .content {
  color: #222;
  border-radius: 0.2em 1.25em 1.25em 1.25em; /* 放大圆角 */
  padding: 0.50em 1.2em; /* 放大气泡内边距 */
  max-width: 90%; /* 放大最大宽度 */
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
.input-area {
  display: flex;
  gap: 0.5rem; /* 放大间距 */
  align-items: flex-end;
  background: #fafbfc;
  padding: 1.5rem 2rem; /* 放大内边距 */
  border-top: 1px solid #eee;
}
textarea {
  flex: 1;
  resize: none;
  border: none;
  background: transparent;
  padding: 1rem;
  font-size: 16px; /* 放大输入字体 */
  line-height: 24px;
  max-height: 180px;
  border-radius: 0.75rem;
  outline: none;
  min-height: 1.5rem; /* 输入框更高 */
}
.send-button {
  min-width: 80px;
  height: 3rem;
  border: none;
  border-radius: 0.75rem;
  background: #007CF0;
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
.dark .chat-main {
  /* background: #222; */
  color: #333;
  font-size:16px;
  line-height:26px;
}
.dark .input-area {
  /* background: #181818;
  border-top: 1px solid #333; */
}
.dark textarea {
  background: #f3f3f3;
  color: #000;
}
.dark textarea::placeholder{
  color:#ccc;
}
.ai-logo {
  width: 24px;
  height: 24px;
  vertical-align: top;
  border-radius: 6px;
  background: #fff;
}
.content p {
font-size:16px;
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
.preview-area {
  background: #f6f8fa;
  border-top: 1px solid #e0e0e0;
  padding: 1.2em 2em 1.2em 2em;
  margin: 0;
}
.preview-title {
  font-weight: bold;
  margin-bottom: 0.5em;
  color: #007cf0;
}
.preview-content {
  font-size: 1em;
}
</style>
<style>
@import 'highlight.js/styles/github.css';
.preview-content pre {
  background: #f6f8fa;
  padding: 1em;
  border-radius: 6px;
  overflow-x: auto;
}
.preview-content code {
  font-family: 'Fira Mono', 'Consolas', 'Menlo', monospace;
  font-size: 1em;
}
</style>