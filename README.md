# SpringSecurityOauth2 SSO

项目:

oauth_sso_server

oauth_sso_client 

oauth_sso_client2

两个client只是端口不一样

### 项目构建

### 测试步骤
1 .
```http request
http://localhost:8998/dflkfsd
```


```
默认直接登录

### end
*待完善*

1. 单点退出
```http request
http://localhost:9999/logout?redirect_uri=http://localhost:xxxx/logout

```
 把数据库的token删掉
 
[SpringSecurity的过滤链是如何工作的](https://editor.csdn.net/md/?articleId=119991773)