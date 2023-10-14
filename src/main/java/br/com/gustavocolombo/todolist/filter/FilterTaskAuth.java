package br.com.gustavocolombo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gustavocolombo.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
    private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var servletPath = request.getServletPath();

    if(servletPath.startsWith("/tasks/")){
      var authorization = request.getHeader("Authorization");
      var encodedPass = authorization.substring("Basic".length()).trim();

      byte[] decodedPass = Base64.getDecoder().decode(encodedPass);

      var authString = new String(decodedPass);
      
      String[] credentials = authString.split(":");
      String username = credentials[0];
      String password = credentials[1];

      var findUser = this.userRepository.findByUsername(username);

      if(findUser == null){
        response.sendError(401, "User unauthorized");
      } else {
        var comparePass = BCrypt.verifyer().verify(password.toCharArray(), findUser.getPassword());

        if(comparePass.verified){
          request.setAttribute("idUser", findUser.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401, "User unauthorized");
        }
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
