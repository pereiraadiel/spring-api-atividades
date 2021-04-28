package adiel.atividades.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import adiel.atividades.services.AtividadeDetailsService;
import adiel.atividades.services.MyUserDetailService;

public class SecurityConfigurer extends WebSecurityConfigurerAdapter{
  @Autowired 
  private AtividadeDetailsService atividadeDetailsService;
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    
    // auth.userDetailsService(atividadeDetailsService);
    MyUserDetailService myUserDetailService = new MyUserDetailService();
    auth.userDetailsService(myUserDetailService);
  }
}
