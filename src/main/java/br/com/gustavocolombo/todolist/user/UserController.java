package br.com.gustavocolombo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;
  
  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel){
    var findUser = this.userRepository.findByUsername(userModel.getName());

    if(findUser != null){
      return ResponseEntity.status(400).body("User already exists");  
    }

    var hashedPassword = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

    userModel.setPassword(hashedPassword);

    var userCreated = this.userRepository.save(userModel);

    return ResponseEntity.status(201).body(userCreated);
  }
}
