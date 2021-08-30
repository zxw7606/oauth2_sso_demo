package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;


@Configuration
@EnableAuthorizationServer
public class ServerConfig extends AuthorizationServerConfigurerAdapter {


    /**
     * 对终点 路径进行处理
     * 比如添加 WWW-Authenticate 的值
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()");
        security.checkTokenAccess("permitAll()");
//        super.configure(security);
    }


//    /**
//     * 配置client的储存信息 JdbcClientDetailsService
//     */
//
//    @Autowired
//    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("ben1")// client_id
                .secret("123456")// client_secret
                .authorizedGrantTypes("authorization_code", "refresh_token")// 该client允许的授权类型
                .scopes("all")// 允许的授权范围
                .autoApprove(false)
                //加上验证回调地址
                .redirectUris("http://localhost:8999/login")
                .and()
                .withClient("ben2")
                .secret("123456")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .autoApprove(false)
                .redirectUris("http://localhost:8998/login");
    }


    /**
     * 配置 TokenService {存取Token的业务}
     * 配置 TokenStore { 持久化Token}
     * 配置 TokenEnhancer { 对Token 进一步处理}
     * 配置 AuthenticationManager { Oauth2 的授权处理 ,先去tokenService查看token是否有效, 有效再通过 ClientDetailService 查看 client的权限}
     */


    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * coke用来获取token
     * 配置code储存
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public ConsumerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
//                .authenticationManager(new OAuth2AuthenticationManager())
                .userDetailsService(userDetailsService) //查看用户的地方
                .authorizationCodeServices(authorizationCodeServices()) // 储存code
//                .tokenServices(tokenServices())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

    }


}
