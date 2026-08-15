### 查看设备上报的数据

**接口地址**: `/nursing/device/queryServiceProperties/{iotId}`

**请求方式**:`GET`

**请求示例**:

路径参数：iotId  设备id

**响应示例**:

```JSON
{
    "msg": "操作成功",
    "code": 200,
    "data": [
        {
            "functionId": "HeartRate",
            "eventTime": "2025-02-19T15:04:24",
            "value": 60
        },
        {
            "functionId": "BedTime",
            "eventTime": "2025-02-19T15:04:24",
            "value": 1739948665934
        },
        {
            "functionId": "SleepPhaseState",
            "eventTime": "2025-02-19T15:04:24",
            "value": 2
        },
        {
            "functionId": "RespiratoryRate",
            "eventTime": "2025-02-19T15:04:24",
            "value": 77
        },
        {
            "functionId": "BedExitCount",
            "eventTime": "2025-02-19T15:04:24",
            "value": 3
        }
    ]
}
```