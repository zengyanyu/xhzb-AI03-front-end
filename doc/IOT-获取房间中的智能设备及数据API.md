### **获取房间中的智能设备及数据**

**接口地址**:`/elder/room/getRoomsWithDeviceByFloorId/{floorId}`

**请求方式**:`GET`

**请求参数**:

| 参数名称 | 参数说明 | 数据类型 |
| -------- | -------- | -------- |
| floorId  | 楼层ID   | long     |

**响应示例**:

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        { //房间
            "id": 1,
            "floorName": null,
            "floorId": "1",
            "roomId": null,
            "code": "101",
            "price": null,
            "bedVoList": [//床位
                {
                    "id": 170,
                    "bedNumber": "101-2",
                    "bedStatus": 1,
                    "roomId": null,
                    "ename": "老李",
                    "elderId": 327,
                    "userVos": null,
                    "deviceVos": []
                },
                {
                    "id": 1,
                    "bedNumber": "101-1",
                    "bedStatus": 1,
                    "roomId": null,
                    "ename": "老李头儿",
                    "elderId": 328,
                    "userVos": null,
                    "deviceVos": []
                }
            ],
            "deviceVos": [//房间的设备
                {
                    "id": 1,
                    "iotId": "kJ2mrhmpY0Z2HeCZmmcNj0rk00",
                    "deviceName": "yw01",
                    "productKey": "j0rkuqwMpYq",
                    "productName": "烟雾报警器",
                    "deviceDataVos": [
                        {
                            "id": 1058486,
                            "createTime": "2024-09-24 00:12:55",
                            "updateTime": null,
                            "remark": null,
                            "iotId": "kJ2mrhmpY0Z2HeCZmmcNj0rk00",
                            "deviceName": "yw01",
                            "nickname": "烟雾01",
                            "productKey": "j0rkuqwMpYq",
                            "productName": "烟雾报警器",
                            "functionId": "CurrentHumidity",
                            "accessLocation": "1楼,101",
                            "locationType": 1,
                            "physicalLocationType": 1,
                            "deviceDescription": "1,1",
                            "dataValue": "66",
                            "alarmTime": "2024-09-24T00:12:54.877"
                        },
                        {
                            "id": 1058487,
                            "createTime": "2024-09-24 00:12:55",
                            "updateTime": null,
                            "remark": null,
                            "iotId": "kJ2mrhmpY0Z2HeCZmmcNj0rk00",
                            "deviceName": "yw01",
                            "nickname": "烟雾01",
                            "productKey": "j0rkuqwMpYq",
                            "productName": "烟雾报警器",
                            "functionId": "SmokeSensorState",
                            "accessLocation": "1楼,101",
                            "locationType": 1,
                            "physicalLocationType": 1,
                            "deviceDescription": "1,1",
                            "dataValue": "0",
                            "alarmTime": "2024-09-24T00:12:54.877"
                        },
                        {
                            "id": 1058488,
                            "createTime": "2024-09-24 00:12:55",
                            "updateTime": null,
                            "remark": null,
                            "iotId": "kJ2mrhmpY0Z2HeCZmmcNj0rk00",
                            "deviceName": "yw01",
                            "nickname": "烟雾01",
                            "productKey": "j0rkuqwMpYq",
                            "productName": "烟雾报警器",
                            "functionId": "IndoorTemperature",
                            "accessLocation": "1楼,101",
                            "locationType": 1,
                            "physicalLocationType": 1,
                            "deviceDescription": "1,1",
                            "dataValue": "20",
                            "alarmTime": "2024-09-24T00:12:54.877"
                        }
                    ]
                }
            ]
        },
        {  //房间
            "id": 3,
            "floorName": null,
            "floorId": "1",
            "roomId": null,
            "code": "103",
            "price": null,
            "bedVoList": [//床位
                {
                    "id": 4,
                    "bedNumber": "103-1",
                    "bedStatus": 1,
                    "roomId": null,
                    "ename": "李大",
                    "elderId": 330,
                    "userVos": null,
                    "deviceVos": [ 
                        { //床位的设备
                            "id": 2,
                            "iotId": "PqLvznGw8Y7If8KFQhc3j0rk00",
                            "deviceName": "sm_01",
                            "productKey": "j0rkcHVJrcL",
                            "productName": "睡眠检测带",
                            "deviceDataVos": [  //床位的设备数据
                                {
                                    "id": 1058489,
                                    "createTime": "2024-09-24 00:12:57",
                                    "updateTime": null,
                                    "remark": null,
                                    "iotId": "PqLvznGw8Y7If8KFQhc3j0rk00",
                                    "deviceName": "sm_01",
                                    "nickname": "睡眠01",
                                    "productKey": "j0rkcHVJrcL",
                                    "productName": "睡眠检测带",
                                    "functionId": "HeartRate",
                                    "accessLocation": "1楼,103,103-1",
                                    "locationType": 1,
                                    "physicalLocationType": 2,
                                    "deviceDescription": "1,3,4",
                                    "dataValue": "30",
                                    "alarmTime": "2024-09-24T00:12:56.677"
                                },
                                {
                                    "id": 1058490,
                                    "createTime": "2024-09-24 00:12:57",
                                    "updateTime": null,
                                    "remark": null,
                                    "iotId": "PqLvznGw8Y7If8KFQhc3j0rk00",
                                    "deviceName": "sm_01",
                                    "nickname": "睡眠01",
                                    "productKey": "j0rkcHVJrcL",
                                    "productName": "睡眠检测带",
                                    "functionId": "shuimianzhuangtai",
                                    "accessLocation": "1楼,103,103-1",
                                    "locationType": 1,
                                    "physicalLocationType": 2,
                                    "deviceDescription": "1,3,4",
                                    "dataValue": "1",
                                    "alarmTime": "2024-09-24T00:12:56.677"
                                },
                                {
                                    "id": 1058491,
                                    "createTime": "2024-09-24 00:12:57",
                                    "updateTime": null,
                                    "remark": null,
                                    "iotId": "PqLvznGw8Y7If8KFQhc3j0rk00",
                                    "deviceName": "sm_01",
                                    "nickname": "睡眠01",
                                    "productKey": "j0rkcHVJrcL",
                                    "productName": "睡眠检测带",
                                    "functionId": "RespiratoryRate",
                                    "accessLocation": "1楼,103,103-1",
                                    "locationType": 1,
                                    "physicalLocationType": 2,
                                    "deviceDescription": "1,3,4",
                                    "dataValue": "96",
                                    "alarmTime": "2024-09-24T00:12:56.677"
                                },
                                {
                                    "id": 1058492,
                                    "createTime": "2024-09-24 00:12:57",
                                    "updateTime": null,
                                    "remark": null,
                                    "iotId": "PqLvznGw8Y7If8KFQhc3j0rk00",
                                    "deviceName": "sm_01",
                                    "nickname": "睡眠01",
                                    "productKey": "j0rkcHVJrcL",
                                    "productName": "睡眠检测带",
                                    "functionId": "lichuangcishu",
                                    "accessLocation": "1楼,103,103-1",
                                    "locationType": 1,
                                    "physicalLocationType": 2,
                                    "deviceDescription": "1,3,4",
                                    "dataValue": "7",
                                    "alarmTime": "2024-09-24T00:12:56.677"
                                }
                            ]
                        }
                    ]
                }
            ],
            "deviceVos": []
        }
    ]
}
```