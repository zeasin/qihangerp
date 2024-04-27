import request from '@/utils/request'

// 查询财务管理-应付款-代发账单列表
export function listAgentShip(query) {
  return request({
    url: '/fms/agentShip/list',
    method: 'get',
    params: query
  })
}

// 查询财务管理-应付款-代发账单详细
export function getAgentShip(id) {
  return request({
    url: '/fms/agentShip/' + id,
    method: 'get'
  })
}


