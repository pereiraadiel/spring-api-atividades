package adiel.atividades.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import adiel.atividades.services.JWTService;
import adiel.atividades.services.MyUserDetailService;
import io.jsonwebtoken.Claims;

public class JWTAuthFilter extends OncePerRequestFilter {
  private MyUserDetailService userService;
  private JWTService jwtService;
  public JWTAuthFilter(MyUserDetailService userService, JWTService jwtService) {
      this.userService = userService;
      this.jwtService = jwtService;
  }
  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest
                                  , HttpServletResponse httpServletResponse
                                , FilterChain filterChain) throws ServletException, IOException {
      String authorization = httpServletRequest.getHeader("Authorization");
      if(authorization != null && authorization.startsWith("Bearer")){
          String token = authorization.split(" ")[1];
          boolean isValid = jwtService.tokenValido(token);
          if(isValid){
              String loginUsuario =jwtService.obterLoginUsuario(token);
              UserDetails usuario = userService.loadUserByUsername(loginUsuario);
            
              Claims claims = this.jwtService.obterClaims(token);
              UsernamePasswordAuthenticationToken user =
                      new UsernamePasswordAuthenticationToken(usuario, claims, usuario.getAuthorities());
              user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

              SecurityContextHolder.getContext().setAuthentication(user);
          }
      }
      filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}