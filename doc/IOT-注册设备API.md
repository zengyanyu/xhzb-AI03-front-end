 ### 注册设备

**接口地址**:`/nursing/device/register`

**请求方式**:`POST`

**请求示例**:

- 新增**随身设备**-智能报警手表

```JSON
{
  "deviceName":"手表1号",//设备名称
  "bindingLocation": "540",//由于绑定的是老人，这个值是老人id
  "locationType": 0,//位置类型 0：随身设备 1：固定设备
  "nodeId": "watch01",//设备节点id
  "physicalLocationType": -1,//如果是随身设备，传-1
  "productKey": "6625d23371d845632a067bdf",//产品id
  "productName": "watch",//产品名称
  "remark": "张小丽", //由于绑定的是老人，这个值是老人的姓名
  "deviceDescription": "1"//由于绑定的是老人，这个值是老人id
}
```

- 新增**固定设备**-烟雾报警器（房间）

```JSON
{
  "deviceName":"烟雾01",//设备名称
  "bindingLocation": "79",//由于绑定的是房间，这个值是房间id
  "locationType": 1,//位置类型 0：随身设备 1：固定设备
  "nodeId": "yw01",//设备节点名称
  "physicalLocationType": 1,//物理位置类型 0楼层 1房间 2床位
  "productKey": "6625d6d171d845632a067c9f",
  "productName": "烟雾报警器",
  "remark": "1楼,101",//对于房间的描述
  "deviceDescription": "1,1"//绑定的是房间,这个是楼层id和房间id，方便数据回显使用
}
```

- 新增固定设备-睡眠检测带（床位）

```JSON
{
  "deviceName": "睡眠检测带01",
  "bindingLocation": "1",//由于绑定的是床位，这个值是床位id
  "locationType": 1,//位置类型 0：随身设备 1：固定设备
  "nodeId": "sm01",
  "physicalLocationType":2,//物理位置类型 0楼层 1房间 2床位
  "productKey": "6625d6d171d845632a067c9f",
  "productName": "睡眠检测带",
  "remark": "1楼,101,101-1", //对于床位的描述
  "deviceDescription": "1,1,5"//绑定的是床位,这个是楼层id、房间id、床位id，方便数据回显使用
}
```

**响应示例**:

```JSON
{"msg":"操作成功","code":200}
```
