# SpringSecurityOauth2 SSO

项目:

oauth_sso_server

oauth_sso_client 

oauth_sso_client2

两个client只是端口不一样

### 项目构建

**数据库**

导入oauth_sso_server\src\main\resources\data.sql


### 测试步骤
1 .
```http request
http://localhost:8998/dflkfsd
```

2 .
用户名 aa 密码 aa

3 .
```http request
http://localhost:8999/dflkfsd
```
默认直接登录

### end
*待完善*

1. 单点退出
```http request
http://localhost:9999/logout?redirect_uri=http://localhost:xxxx/logout

```
 把数据库的token删掉