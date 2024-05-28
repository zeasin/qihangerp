import request from '@/utils/request'

// 查询拼多多订单列表
export function listOrder(query) {
  return request({
    url: '/pdd-api/order/list',
    method: 'get',
    params: query
  })
}

// 查询拼多多订单详细
export function getOrder(id) {
  return request({
    url: '/pdd-api/order/' + id,
    method: 'get'
  })
}



// 确认拼多多订单
export function confirmOrder(data) {
  return request({
    url: '/pdd-api/order/confirm',
    method: 'post',
    data: data
  })
}


// 接口拉取订单
export function pullOrder(data) {
  return request({
    url: '/pdd-api/order/pull_order',
    method: 'post',
    data: data
  })
}
