package snn.soluciones.com.controllers;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
  @GetMapping({"/login"})
  public String login(@RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "logout", required = false) String logout,
      Model model, Principal principal, RedirectAttributes flash) {
    if (principal != null) {
      flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
      return "redirect:/";
    }
    model.addAttribute("titulo", "Iniciar sesión");
    if (error != null)
      model.addAttribute("error", "Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
    if (logout != null)
      model.addAttribute("info", "Has cerrado sesión correctamente!");

    return "login/login";  // Esto buscará en templates/login/login.html
  }
}