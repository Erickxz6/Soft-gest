package com.softgest.controller;

import com.softgest.model.*;
import com.softgest.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }

    @GetMapping
    public String listar(Model model) { model.addAttribute("usuarios", service.listarTodos()); return "usuarios/lista"; }

    @GetMapping("/nuevo")
    public String nuevo(Model model) { model.addAttribute("usuario", new Usuario()); model.addAttribute("roles", Rol.values()); return "usuarios/form"; }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.buscarPorId(id).orElseThrow());
        model.addAttribute("roles", Rol.values());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) { model.addAttribute("roles", Rol.values()); return "usuarios/form"; }
        service.guardar(usuario);
        flash.addFlashAttribute("success", "Usuario guardado exitosamente");
        return "redirect:/usuarios";
    }
}
