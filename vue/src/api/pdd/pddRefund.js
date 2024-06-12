import request from '@/utils/request'

// 查询拼多多订单退款列表
export function listPddRefund(query) {
  return request({
    url: '/pdd-api/refund/list',
    method: 'get',
    params: query
  })
}

// 查询拼多多订单退款详细
export function getPddRefund(id) {
  return request({
    url: '/pdd-api/refund/' + id,
    method: 'get'
  })
}

export function pullRefund(data) {
  return request({
    url: '/pdd-api/refund/pull_refund',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/pdd-api/refund/push_oms',
    method: 'post',
    data: data
  })
}
