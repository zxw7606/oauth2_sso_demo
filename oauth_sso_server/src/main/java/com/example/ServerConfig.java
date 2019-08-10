package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;


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


    /**
     * 配置client的储存信息 JdbcClientDetailsService
     */

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置数据源
        clients.jdbc(dataSource);
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
        JdbcAuthorizationCodeServices codeServices = new JdbcAuthorizationCodeServices(dataSource);
        return codeServices;
    }

    @Bean
    public TokenStore tokenStore() {
        JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);
        return tokenStore;
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
                .authenticationManager(new OAuth2AuthenticationManager())
                .userDetailsService(userDetailsService) //查看用户的地方
                .authorizationCodeServices(authorizationCodeServices()) // 储存code
//                .tokenServices(tokenServices())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

    }
}
