### 查询设备详细数据

**接口地址**:`/nursing/device/{iotId}`

**请求方式**:GET

**请求示例**:

路径参数：iotId  设备id

**响应示例**:

```JSON
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "id": 4,
        "iotId": "67b08da5bab900244b1fc6f0_yw01",
        "deviceName": "烟雾01",
        "nodeId": "yw01",
        "secret": "23abb22b43fe4d33b6322a198c58e22d",
        "productKey": null,
        "productName": "烟雾报警器",
        "locationType": 1,
        "bindingLocation": 2,
        "remark": "1楼,101",
        "deviceStatus": "ONLINE",
        "activeTime": "2025-02-15 22:16:42",
        "createTime": "2025-02-15 20:55:15",
        "createBy": 1,
        "creator": null
    }
}
```