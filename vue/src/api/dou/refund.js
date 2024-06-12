import request from '@/utils/request'

// 查询抖店订单退款列表
export function listRefund(query) {
  return request({
    url: '/dou-api/refund/list',
    method: 'get',
    params: query
  })
}

// 查询抖店订单退款详细
export function getRefund(id) {
  return request({
    url: '/dou-api/refund/' + id,
    method: 'get'
  })
}

export function pullRefund(data) {
  return request({
    url: '/dou-api/refund/pull_refund',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/dou-api/refund/push_oms',
    method: 'post',
    data: data
  })
}

