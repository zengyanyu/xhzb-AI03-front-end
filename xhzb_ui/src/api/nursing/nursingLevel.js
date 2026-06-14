import request from '@/utils/request'

//查询所有护理等级
export function getNursingLevelAll() {
  return request({
    url: '/nursing/nursingLevel/listAll',
    method: 'get'
  })
}

// 查询护理等级列表
export function listNursingLevel(query) {
  return request({
    url: '/nursing/nursingLevel/list',
    method: 'get',
    params: query
  })
}

// 查询护理等级详细
export function getNursingLevel(id) {
  return request({
    url: '/nursing/nursingLevel/' + id,
    method: 'get'
  })
}

// 新增护理等级
export function addNursingLevel(data) {
  return request({
    url: '/nursing/nursingLevel',
    method: 'post',
    data: data
  })
}

// 修改护理等级
export function updateNursingLevel(data) {
  return request({
    url: '/nursing/nursingLevel',
    method: 'put',
    data: data
  })
}

// 删除护理等级
export function delNursingLevel(id) {
  return request({
    url: '/nursing/nursingLevel/' + id,
    method: 'delete'
  })
}
