import request from '@/utils/request'

// 查询淘宝退款订单列表
export function listTaoRefund(query) {
  return request({
    url: '/tao-api/refund/list',
    method: 'get',
    params: query
  })
}

// 查询淘宝退款订单详细
export function getTaoRefund(id) {
  return request({
    url: '/tao-api/refund/' + id,
    method: 'get'
  })
}

export function pullRefund(data) {
  return request({
    url: '/tao-api/refund/pull_refund',
    method: 'post',
    data: data
  })
}

export function pullRefundDetail(data) {
  return request({
    url: '/tao-api/refund/pull_refund_detail',
    method: 'post',
    data: data
  })
}

