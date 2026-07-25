# 微信小程序登录接口

- 接口路径：`/member/user/login`

- 请求方式：`POST`

- 请求参数：（已固定）

  | 参数名称  | 参数说明           | 数据类型 |
  | --------- | ------------------ | -------- |
  | code      | 临时登录凭证code   | string   |
  | nickName  | 微信用户           | string   |
  | phoneCode | 获取手机的临时code | string   |

- 响应示例

  ```js
  {
    "code": 200,
    "msg": "操作成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlpb3mn7_lvIDoirE4OTE1IiwiZXhwIjoxNDY1MjI3MTMyOCwidXNlcmlkIjoxfQ.nB6ElZbUywh-yiHDNMJS8WqUpcLWCszVdvAMfySFxIM",
      "nickName": "好柿开花8915"
    },
    "operationTime": null
  }
  ```

  