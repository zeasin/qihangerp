import request from '@/utils/request'

// 查询打包发货列表
export function listShipOrder(query) {
  return request({
    url: '/shipping/ship_order',
    method: 'get',
    params: query
  })
}
