import request from '@/utils/request'

// 查询知识库列表
export function listKnowledgeBase(query) {
  return request({
    url: '/nursing/knowledgeBase/list',
    method: 'get',
    params: query
  })
}

// 查询知识库详细
export function getKnowledgeBase(id) {
  return request({
    url: '/nursing/knowledgeBase/' + id,
    method: 'get'
  })
}

// 新增知识库
export function addKnowledgeBase(data) {
  return request({
    url: '/nursing/knowledgeBase',
    method: 'post',
    data: data
  })
}

// 修改知识库
export function updateKnowledgeBase(data) {
  return request({
    url: '/nursing/knowledgeBase',
    method: 'put',
    data: data
  })
}

// 删除知识库
export function delKnowledgeBase(id) {
  return request({
    url: '/nursing/knowledgeBase/' + id,
    method: 'delete'
  })
}
