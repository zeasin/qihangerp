import request from '@/utils/request'

// 查询拼多多订单退款列表
export function listPddRefund(query) {
  return request({
    url: '/pdd-api/afterSale/list',
    method: 'get',
    params: query
  })
}

// 查询拼多多订单退款详细
export function getPddRefund(id) {
  return request({
    url: '/pdd-api/afterSale/' + id,
    method: 'get'
  })
}

export function pullRefund(data) {
  return request({
    url: '/pdd-api/afterSale/pull_list',
    method: 'post',
    data: data
  })
}

export function pushOms(data) {
  return request({
    url: '/pdd-api/afterSale/push_oms',
    method: 'post',
    data: data
  })
}
