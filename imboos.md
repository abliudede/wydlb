# IMBOOS

> 我要当老板，给出文档的接口在本地完成调试之后再编写API文档的。

| 文档版本 | 版本日期   | 修改者 | 说明                                 |
| -------- | ---------- | ------ | ------------------------------------ |
| v0.1.0   | 2020-09-14 | 胡邹   | 先定义部分基础规则                   |
| v0.1.1   | 2020-09-14 | 胡邹   | 定义部分接口文档                     |
| v0.1.2   | 2020-09-15 | 胡邹   | 文档增补                             |
| v0.1.3   | 2020-09-24 | 胡邹   | 文档较大版本更新，主要接口已完成定义 |
| v0.1.5   | 2020-09-24 | 胡邹   | 文档增补                             |
| v0.1.6   | 2020-09-29 | 胡邹   | 接口基本完成定义                     |
| v0.1.7   | 2020-10-13 | 胡邹   | 下一局游戏接口补全                   |
| v0.1.8   | 2020-12-24 | 胡邹   | 修改注册接口，注册直接登录           |



### 环境配置

全局配置，API = HOST + 路由，组成完成的请求地址。

| 环境     | HOST                 | 备注                             |
| -------- | -------------------- | -------------------------------- |
| 测试环境 | https://www.onela.cn | 测试host临时转发，方便调试       |
| 生产环境 |                      | 暂未部署，达到上线条件再部署即可 |



## 游戏API

API基本规则定义

~~~json
// 错误返回
{
    "code": "1001",						// code大于1 表示错误
    "message": "请输入招聘人员数量",		  // 错误描述
    "module": "app",				// 业务模块
    "data": null						// 接口报文，JSON对象，错误返回时data为null
}
~~~





###  用户注册

微信小游戏首次用户授权获取用户信息，调用用户注册API，传入授权之后获取到的用户信息。

| 路由           | `/api/user/register/app` |
| -------------- | ------------------------ |
| Authentication | false                    |

**入参**

~~~ json
// 微信授权之后拿到的json结构
{
    // 以下是获取微信授权之后拿到结构，原封不动的作为注册参数传入
    "openId": "OPENID",
    "nickName": "NICKNAME",
    "gender": GENDER,
    "city": "CITY",
    "province": "PROVINCE",
    "country": "COUNTRY",
    "avatarUrl": "AVATARURL",
    "unionId": "UNIONID",
    "watermark": {
        "appid":"APPID",
        "timestamp":TIMESTAMP
    },
    "referralCode": "0001"			// 【N】邀请码，这个是自定义添加的参数
}
~~~

**出参**

~~~json
// 注册成功自动完成登录，减少一次调用login接口的次数
{
    "code": "0",
    "message": "注册并登录成功",
    "module": "minigame",
    "data": {
        "id": 7,
        "referralCode": "0007",			// 邀请码：当前注册用户的邀请码（不是邀请人的邀请码）
        "account": "18815544605",
        "nickname": "",
        "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50SWQiOjcsIm5pY2tuYW1lIjoiIiwic2V4IjowLCJleHAiOjE2MTAzODg5NTE3NzR9.2kUu-OC5hTgdA8Q7WVXp5Qc-UMFtf1pAdamK_mATJUn9Xr8U1CulEJrxHeFKG4OmLyhXtxRdzb-YOqz-QPXqmg"
    }
}
~~~



###  用户登录

微信小游戏非首次授权，可以直接调用此api进行授权登录，返回的token值作为业务API请求头（headers）的中的鉴权信息传入。

| 路由           | `/api/user/login/app` |
| -------------- | --------------------- |
| authentication | false                 |

**入参**

~~~ json
{
    // 以下是获取微信授权之后拿到结构，原封不动的作为注册参数传入
    "openId": "OPENID",
    "nickName": "NICKNAME",
    "gender": GENDER,
    "city": "CITY",
    "province": "PROVINCE",
    "country": "COUNTRY",
    "avatarUrl": "AVATARURL",
    "unionId": "UNIONID",
    "watermark": {
        "appid":"APPID",
        "timestamp":TIMESTAMP
    }
}
~~~

**出参**

~~~json
{
    "code": "0",
    "message": "登录成功",
    "module": "app",
    "data": {
        "id": 2,					// 用户id
        "referralCode": "0002",		// 邀请码
        "account": "ceshi",			// 用户账户（必要情况下可以脱敏处理）
        "nickname": "CESHI",		// 用户昵称
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOjIsImV4cCI6MTU5OTQxNzg0NTU3OX0.IbXQ-Db-A3kIOocdSJJ33rFpB-CcwnjiYxeoaDSFTNo"
    }
}
~~~



### 鉴权

以下接口需要在headers中传入鉴权信息token信息：

`token:eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50SWQiOjIsImV4cCI6MTYwMDA0OTY2MDkyMX0.FiX2yKlMf7wDACUw-p3479Odx2Zij2q5ub3_HbEdL_T2Z_rvR-6jH9Q5T0BVoOr5k-kFukgaGKlYiWt3Ibf5PA1`

注意：token值是动态获取，登录之后返回的值填入即可。



### 获取游戏基础信息/初始化游戏信息

在开始游戏之前需要调用此api

| 路由           | `/api/game/getGameInfo/app` |
| -------------- | --------------------------- |
| authentication | true                        |

**入参**

~~~ json
// 无body参数，输入鉴权authentication信息即可。
~~~

**出参**

~~~json
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": {
        "id": "0ba1d690f69f11ea852c77187044765f",		// 游戏进度id
        "company_size": 1,			// 公司规模，详细见数据字典
        "employee_count": 10,		// 企业员工
        "leisure_count": 10,		// 闲置员工
        "funds": 1000000,			// 企业资金
        "games_number": 1,			// 第几局
        "maximum_rounds": 60,		// 最大回合数
        "current_rounds": 1,		// 第几回
        "investment_month": "2020-09-14T15:29:14.440Z",	// investment_month
        "games_status": "active",	// 状态：active - 正在进行的有、finish - 完成的游戏、close - 主动关闭的游戏、fail - 破产失败的游戏
        "superposition_ratio": 1,	// 收益叠加系数，暂时不用
        "buy_rounds": 0,			// 购买延期，暂时不用
        "investments": [
            {
                "id": "0bb13fe0f69f11ea852c77187044765f",	// 投资项目id
                "project_name": "农业",		// 投资项目名称
                "project_code": "NY",			// 项目标识
                "project_max": 2,			// 项目事件值
                "risk_level": 1,			// 项目风险等级
                "profit_amount": 0,			// 当前收益（人/月）
                "people_count": 0,			// 当前人员
                "investment_total": 0,		// 当前项目贡献收益
                "investment_month": "2020-09-14T15:29:14.440Z",		// 投资时间，在上一层有这个值
                "prediction_amount": 1,		// 预测下一轮收益
                "investment_status": "active"		// 预测收益是否隐藏显示
            },
            {
                "id": "0bb13fe1f69f11ea852c77187044765f",
                "project_name": "餐饮",
                "project_code": "CY",
                "project_max": 2,
                "risk_level": 1,
                "profit_amount": 0,
                "people_count": 0,
                "investment_total": 0,
                "investment_month": "2020-09-14T15:29:14.440Z",
                "prediction_amount": 1,
                "investment_status": "active"
            },
            // 其他投资项目 ……
        ],
        "skills": [
            {
                "id": "0bb648f0f69f11ea852c77187044765f",		// 技能id
                "skill_code":"WIN",				// 技能标识
                "skill_name": "失败乃成功之母",		// 技能名称
                "skill_type": "通用",			// 技能类型：通用、专项
                "skill_level": 0,				// 技能等级，0表示还未获得，1-3表示等级
                "skill_introduce": "",			// 技能介绍	
                "skill_icon": "",		// 技能图标，这块可直接用前端配置好，还没想好
                "bonus_bonus": 0,		// 加成奖励
                "loss_bonus": 0,			// 降损奖励
                "skill_note": []		// 技能升级记录，详细字段定义后续补充
            },
            // 其他技能 ……
            {
                "id": "f6995447f6a211eabf9c0bfc19d7ec94",
                "skill_name": "区块链操盘手",
                "skill_code": "QKL",
                "skill_type": "专项",
                "skill_level": 0,
                "skill_introduce": "",
                "skill_icon": "",
                "bonus_bonus": 0,
                "loss_bonus": 0,
                "skill_note": []
            }
            
        ]
    }
}


// skill_note ：技能笔记，暂定，有需求再调整
[{
     "name":"农业收益", // 名称
     "skill_level": 1, // 当前对应的技能等级
     "skill_condition":"10000000", // 达成条件描述
     "time": '2020-09-14T15:29:14.440Z',
}]
~~~





### 重新开始游戏

谨慎调用，重新开始游戏意味着当前进度会进行重置，

| 路由           | `/api/game/reloadGame/app` |
| -------------- | -------------------------- |
| authentication | true                       |

**入参**

~~~ json
// 无，不需要参数，鉴权即可
~~~

**出参**

~~~json
// 出参同【获取游戏基础信息/初始化游戏信息】接口返回参数结果一致
~~~



### 下一月（下一局）

关键接口，

| 路由           | `/api/game/nextRounds/app` |
| -------------- | -------------------------- |
| authentication | true                       |

**入参**

~~~ json
// 无body参数
~~~

**出参**

~~~json
// 接口比较复杂，测试之后定义详细结构
// gameInfo.games_status；状态：active - 正在进行的有、finish - 完成的游戏、fail - 破产，根据游戏状态判断业务
{
    "code": "0",
    "message": "ok",
    "module": "minigame",
    "data": {
        "gameInfo": {
            "id": "8c1ec0f0025a11ebac26b1a09d90a98b",
            "account_id": 2,
            "basics_id": "70f32ff0025a11ebac26b1a09d90a98b",
            "company_size": 1,
            "grow_date": "2020-09-29T00:00:00.000Z",
            "employee_count": 10,
            "leisure_count": 10,
            "funds": 1000000,
            "games_number": 1,
            "maximum_rounds": 60,
            "current_rounds": 3,
            "investment_month": "2020-11-29T00:00:00.000Z",
            "games_status": "active",		// 关键字段，根据此字段判断本轮游戏是否结束（创业成功、破产）
            "superposition_ratio": 1,
            "buy_rounds": 0,
            "remark": "",
            "create_time": "2020-09-29T13:49:08.607Z",
            "create_id": "2",
            "update_time": "2020-10-13T14:56:18.921Z",
            "update_id": "2",
            "valid": true
        },
        "mySkills": {
            "WIN": {
                "name": "失败乃成功之母",
                "type": "通用",
                "skill_level": 0
            },
            "EYE": {
                "name": "经验之眼",
                "type": "通用",
                "skill_level": 0
            },
            "SOCIAL": {
                "name": "社交达人",
                "type": "通用",
                "skill_level": 0
            },
            "NY": {
                "name": "农业操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "CY": {
                "name": "餐饮操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "FZ": {
                "name": "服装操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "JD": {
                "name": "酒店操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "WL": {
                "name": "物流操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "YY": {
                "name": "医药操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "FDC": {
                "name": "房地产操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "HLW": {
                "name": "互联网操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "GS": {
                "name": "股市操盘手",
                "type": "专项",
                "skill_level": 0
            },
            "QKL": {
                "name": "区块链操盘手",
                "type": "专项",
                "skill_level": 0
            }
        },
        "eventRisk": {
            "main": "1",
            "code": "CY1",
            "type": "add",
            "probability": 0.05,
            "probability_min": 10,
            "probability_max": 15,
            "multiple_min": 0.3,
            "multiple_max": 1
        },
        "monthIncome": {
            "totalNetIncome": 0,
            "investmentList": [
                {
                    "name": "农业",
                    "type": "低风险低收益",
                    "project_code": "NY",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 5425,
                    "floatingIncome": 0,
                    "netIncode": 5425,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "餐饮",
                    "type": "低风险低收益",
                    "project_code": "CY",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 5037,
                    "floatingIncome": 0,
                    "netIncode": 5037,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "服装",
                    "type": "低风险低收益",
                    "project_code": "FZ",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 6392,
                    "floatingIncome": 0,
                    "netIncode": 6392,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "酒店",
                    "type": "中风险中收益",
                    "project_code": "JD",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 4756,
                    "floatingIncome": 0,
                    "netIncode": 4756,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "物流",
                    "type": "中风险中收益",
                    "project_code": "WL",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 4947,
                    "floatingIncome": 0,
                    "netIncode": 4947,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "医药",
                    "type": "中风险中收益",
                    "project_code": "YY",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 3666,
                    "floatingIncome": 0,
                    "netIncode": 3666,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "房地产",
                    "type": "高风险高收益",
                    "project_code": "FDC",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 6403,
                    "floatingIncome": 0,
                    "netIncode": 6403,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "互联网",
                    "type": "高风险高收益",
                    "project_code": "HLW",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 5357,
                    "floatingIncome": 0,
                    "netIncode": 5357,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "股市",
                    "type": "高风险高收益",
                    "project_code": "GS",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": -163,
                    "floatingIncome": 0,
                    "netIncode": -163,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                },
                {
                    "name": "区块链",
                    "type": "特高风险特高收益",
                    "project_code": "QKL",
                    "people_count": 0,
                    "total": 0,
                    "investment_total": 0,
                    "grossIncome": 9855,
                    "floatingIncome": 0,
                    "netIncode": 9855,
                    "riskEvent": null,
                    "skill": [],
                    "prediction_amount": 0,
                    "prediction_hide": true
                }
            ]
        }
    }
}
~~~





### 招聘员工

注意：招聘、解雇其实是一个接口，入参结构和出参结构都一样。路由倒数第二位表示参数，`recruitment`表示招聘，`dismissal`表示解雇。组织参数区分路由以及招聘/解雇人员的绝对值，即可区分对应的动作。另外项目人员投入/撤走与此类似。

招聘员工，每招聘一名员工的费用式5000元。

| 路由           | `/api/game/recruitment/app` |
| -------------- | --------------------------- |
| authentication | true                        |

**入参**

~~~ json
// 招聘、解雇其实是一个接口，入参结构和出参结构都一样。
{
    "num":1			// 招聘员工数量，取绝对值
}
~~~

**出参**

~~~json
{
    "code": "0",			// code等于0表示请求成功，判断code即可
    "message": "ok",
    "module": "app",
    "data": {
        "num": 1,				// 新增招聘人数
        "expenditure": 5000,	// 招聘支出费用
        "employee_count": 12,	// 当前企业员工数量
        "leisure_count": 12,	// 当前企业闲置员工数量
        "funds": 990000			// 当前企业资金（元）
    }
}
~~~



### 解雇员工

只能从闲置员工人数中解雇，如果需要从项目中解雇员工，先从投资项目中撤走人员（闲置），然后从闲置员工中解雇即可。

| 路由           | `/api/game/dismissal/app` |
| -------------- | ------------------------- |
| authentication | true                      |

**入参**

~~~ json
// 招聘、解雇其实是一个接口，入参结构和出参结构都一样。
{
    "num":1			// 解雇员工数量
}
~~~

**出参**

~~~json
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": {
        "num": 1,				// 此次解雇员工数量
        "expenditure": 5000,	// 解雇员工费用
        "employee_count": 11,	// 当前企业员工数量
        "leisure_count": 11,	// 当前企业闲置员工数量
        "funds": 985000			// 当前企业资金（元）
    }
}
~~~



### 项目投入人员

给指定的项目投入项目人员

| 路由           | `/api/game/additional/app` |
| -------------- | -------------------------- |
| authentication | true                       |

**入参**

~~~ json
{
    "investment_id":"fcd7d330f6f311eaad14298b3921cab8",		// 投资项目id
    "num":1			// 项目投入人数
}
// 项目id必须属于当前用户
// 项目投入人数必须小于当前用户闲置员工数
~~~

**出参**

~~~json
// 正确实例
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": {
        "status": "SUCCESS",				// SUCCESS：表示操作成功，FAIL：表示操作失败
        "action": "ADD",					// ADD表示投入操作
        "num": 1,							// 表示人数
        "note": "操作成功"					  // 提示文案
    }
}

// 错误示例
{
    "code": "1001",
    "message": "项目投入人数不能超出闲置人数。",
    "module": "app",
    "data": null
}
~~~



### 项目撤走人员



| 路由           | `/api/game/divestment/app` |
| -------------- | -------------------------- |
| authentication | true                       |

**入参**

~~~ json
{
    "investment_id":"fcd7d330f6f311eaad14298b3921cab8",		// 投资项目id
    "num":1			// 项目撤走人数
}

// 项目id必须属于当前用户
// 撤走人员数量必须小于等于当前项目已经投入人员总数
~~~

**出参**

~~~json
// 正确示例
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": {
        "status": "SUCCESS",
        "action": "POP",
        "num": 1,
        "note": "操作成功"
    }
}

// 错误示例
{
    "code": "1001",
    "message": "撤走人员数量不能大于项目人员数量。",
    "module": "app",
    "data": null
}
~~~



### 查询技能墙信息

在获取游戏信息API里面已经带出来了技能信息，单页面使用这个接口只查询技能表，减少查询量。

| 路由           | `/api/game/mySkills/app` |
| -------------- | ------------------------ |
| authentication | true                     |

**入参**

~~~ json
// 无参数
~~~

**出参**

~~~json
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": [
        {
            "id": "fcde62e0f6f311eaad14298b3921cab8",
            "skill_name": "失败乃成功之母",
            "skill_code": "WIN",
            "skill_type": "通用",
            "skill_level": 1,
            "skill_introduce": "",
            "skill_icon": "",
            "bonus_bonus": 0.1,
            "loss_bonus": 0.1
        },
        // …… 省略中间JSON结构，结构都一样，方便文档阅读。
        {
            "id": "fcde62ecf6f311eaad14298b3921cab8",
            "skill_name": "区块链操盘手",
            "skill_code": "QKL",
            "skill_type": "专项",
            "skill_level": 1,
            "skill_introduce": "",
            "skill_icon": "",
            "bonus_bonus": 0.1,
            "loss_bonus": 0.1
        }
    ]
}
~~~





### 查询排行榜单（世界榜、个人榜、个人世界榜单）

世界榜单和个人榜单使用同一个API，通过参数调节输出。

该接口还未完全完成，参数会进行调整。

| 路由           | `/api/game/getLeaderboard/app` |
| -------------- | ------------------------------ |
| authentication | true                           |

**入参**

~~~ json
{
    "list_type":"WORLD",			// 榜单类型：WORLD - 世界榜单； SELF - 个人榜单；MYWORLD - 我在世界榜单的位置；
}

// MYWORLD - 我在世界榜单的位置；暂时未实现，WORLD - 世界榜单； SELF - 个人榜单已经可以
~~~

**出参**

~~~json
// 世界榜单、个人榜单出参结构都一致。
// 显示记录序号 = 根据数组序号 +1 +（当前分页数-1）*每页记录数
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": {
        "list": [
            {
                "id": "fcc95440f6f311eaad14298b3921cab8",
                "account_id": 2,
                "basics_id": "dc64dd30f69111ea9456cf436a75b184",
                "company_size": 1,
                "employee_count": 112,
                "leisure_count": 66,
                "funds": 2601629,
                "games_number": 1,
                "maximum_rounds": 60,
                "current_rounds": 20,
                "investment_month": "2022-05-15T00:00:00.000Z",
                "games_status": "active",
                "superposition_ratio": 1,
                "buy_rounds": 0,
                "remark": "",
                "create_time": "2020-09-15T01:37:16.676Z",
                "create_id": "2",
                "update_time": "2020-09-24T15:18:29.094Z",
                "update_id": "2",
                "valid": true
            }
        ],
        "pageIndex": 1,			// 当前分页数
        "pageSize": 10,			// 每页记录数
        "rank": 1				// 查询结果有多少条记录参与排名
    }
}
~~~



### 查询游戏创业历程

接口不需要鉴权

| 路由           | `/api/game/queryEntrepreneurship/app` |
| -------------- | ------------------------------------- |
| authentication | false                                 |

**入参**

~~~ json
{
    "game_id":"8c1ec0f0025a11ebac26b1a09d90a98b"		// 游戏进度id，重置游戏之后新开局会生成新的游戏id。
}
~~~

**出参**

~~~json
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": [
        {
            "id": 9, // 主键id
            "event_code": "INIT",// 事件代码
            "event_name": "开始游戏",// 事件名称
            "event_description": "2020-09-29，【牛肉店店长】卖掉了自己的房子，开始走上创业的道路，获得妻儿坚定不移的支持后，他知道自己已无退路，即使跪着也只能把这条路走完！",// 事件描述
            "finality_tag": false,// 阶段终结标志
            "create_time": "2020-09-29T13:49:08.715Z"// 创建时间
        }
    ]
}
// 这是一个数组，遍历显示event_description节点即可。
~~~



### 分享好友升级技能

分享给好友之后，好友打开链接。

如果是在微信端，授权获取微信授权，拿到授权之后，获取昵称以及openid等信息，这时候再调用此接口。

不过需要主要的是：

1、同一个用户需要限制调用频率，细节待定（可以前端限制时间，或者后端限制邀请人的唯一性）

2、此API不需要鉴权即可访问

| 路由           | `/api/game/skillShareBuddy/app` |
| -------------- | ------------------------------- |
| authentication | false                           |

**入参**

~~~ json
{
    "account_id":2,				// 分享人账户id，如果需要预防作弊，id需要加盐处理暂时不处理。
    "nikename":"长沙扛把子",		// 分享对象昵称
    "openid":""				// 微信openid
}
~~~

**出参**

~~~json
{
    "code": "0",
    "message": "ok",
    "module": "app",
    "data": {
        "status": "SUCCESS",   // 标记是否执行成功
        "skill_upgrade": 0,		// 0表示技能等级没有升级，邀请记录已记录；1表示刚好在本次完成技能升级
        "skill_level": 2	     // 当前技能等级
    }
}
~~~















### 格式



| 路由           | ``   |
| -------------- | ---- |
| authentication | true |

**入参**

~~~ json

~~~

**出参**

~~~json

~~~





## 数据字典

### 公司规模

直接给出json结构，方便前端直接引用

~~~json
// 公司规则数组，可以直接通过索引取值
[
    {"level":1,"emploee":10,"money":1000000,"reward":0,"name":"默默无闻"},
    {"level":2,"emploee":100,"money":5000000,"reward":0,"name":"小有名气"},
    {"level":3,"emploee":500,"money":10000000,"reward":0,"name":"天使轮"},
    {"level":4,"emploee":1000,"money":50000000,"reward":1000,"name":"A轮"},
    {"level":5,"emploee":2000,"money":100000000,"reward":2000,"name":"B轮"},
    {"level":6,"emploee":3000,"money":300000000,"reward":3000,"name":"C轮"},
    {"level":7,"emploee":5000,"money":500000000,"reward":5000,"name":"D轮"},
    {"level":8,"emploee":10000,"money":1000000000,"reward":10000,"name":"上市企业"},
    {"level":9,"emploee":20000,"money":5000000000,"reward":20000,"name":"知名上市企业"},
    {"level":10,"emploee":50000,"money":10000000000,"reward":30000,"name":"著名上市企业"},
    {"level":11,"emploee":100000,"money":100000000000,"reward":50000,"name":"独角兽"},
    {"level":12,"emploee":1000000,"money":1000000000000,"reward":100000,"name":"商业帝国"}
]

// 字段解释
{
    "level":1,			// 公司等级
    "emploee":10,		// 满足条件：企业员工达到10人
    "money":1000000,	// 满足条件：企业资金达到100万
    "reward":0,			// 初始资金额外奖励
    "name":"默默无闻"	 // 公司对应阶段名称
}

~~~



###  投资项目代码

~~~json
{
  "NY": {					// 属性名称就是投资项目代码：NY表示农业投资项目
    name: "农业",			// 投资项目名称
    type: "低风险低收益",		// 项目风险类型
    wage_min: 5000,			// 收益区间：最小值
    wage_max: 5500,			// 收益区间：最大值
    project_max: 2,			// 该投资项目下面有多少个潜在风险事件
    initial: 5333,			// 初始值，没有意义，仅仅作为初始界面前端现实效果使用。
  },
  "CY": {
    name: "餐饮",
    type: "低风险低收益",
    wage_min: 4800,
    wage_max: 6000,
    project_max: 2,
    initial: 5888,
  },
  "FZ": {
    name: "服装",
    type: "低风险低收益",
    wage_min: 4500,
    wage_max: 6500,
    project_max: 2,
    initial: 6333,
  },
  "JD": {
    name: "酒店",
    type: "中风险中收益",
    wage_min: 4000,
    wage_max: 7000,
    project_max: 2,
    initial: 4444,
  },
  "WL": {
    name: "物流",
    type: "中风险中收益",
    wage_min: 3000,
    wage_max: 8000,
    project_max: 2,
    initial: 7888,
  },
  "YY": {
    name: "医药",
    type: "中风险中收益",
    wage_min: 2000,
    wage_max: 9000,
    project_max: 2,
    initial: 8888,
  },
  "FDC": {
    name: "房地产",
    type: "高风险高收益",
    wage_min: 1000,
    wage_max: 10000,
    project_max: 2,
    initial: 1888,
  },
  "HLW": {
    name: "互联网",
    type: "高风险高收益",
    wage_min: 0,
    wage_max: 12000,
    project_max: 2,
    initial: 11888,
  },
  "GS": {
    name: "股市",
    type: "高风险高收益",
    wage_min: -5000,
    wage_max: 15000,
    project_max: 3,
    initial: -288,
  },
  "QKL": {
    name: "区块链",
    type: "特高风险特高收益",
    wage_min: -10000,
    wage_max: 20000,
    project_max: 2,
    initial: 18888,
  },
}
~~~



### 技能配置

~~~json
{
    "WIN":{"name":"失败乃成功之母","type":"通用","condition":"bankruptcy","S1":1,"S2":5,"S3":10},
    "EYE":{"name":"经验之眼","type":"通用","condition":"bankruptcy","S1":1,"S2":10,"S3":30},
    "SOCIAL":{"name":"社交达人","type":"通用","condition":"invite","S1":1,"S2":3,"S3":5},
    "NY":{"name":"农业操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "CY":{"name":"餐饮操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "FZ":{"name":"服装操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "JD":{"name":"酒店操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "WL":{"name":"物流操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "YY":{"name":"医药操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "FDC":{"name":"房地产操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "HLW":{"name":"互联网操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "GS":{"name":"股市操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000},
    "QKL":{"name":"区块链操盘手","type":"专项","condition":"profit","S1":10000000,"S2":100000000,"S3":1000000000}
}

// 字段解释
{
    "name":"农业操盘手",			// 技能名称
    "type":"专项",				// 技能类型：专项、通用
    "condition":"profit",		// 升级条件：profit - 资产金额；bankruptcy - 破产次数 ；invite - 邀请人数
    "S1":10000000,				// 升级等级1需要的条件
    "S2":100000000,				// 升级等级2需要的条件
    "S3":1000000000				// 升级等级3需要的条件
}
~~~

