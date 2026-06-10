package com.softgest.controller;

import com.softgest.model.Producto;
import com.softgest.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }

    @GetMapping
    public String listar(@RequestParam(required = false) String q, @RequestParam(required = false) Boolean criticos, Model model) {
        if (Boolean.TRUE.equals(criticos)) { model.addAttribute("productos", service.productosCriticos()); model.addAttribute("criticos", true); }
        else { model.addAttribute("productos", service.buscar(q)); }
        model.addAttribute("q", q);
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) { model.addAttribute("producto", new Producto()); model.addAttribute("categorias", service.listarCategorias()); return "productos/form"; }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", service.buscarPorId(id).orElseThrow());
        model.addAttribute("categorias", service.listarCategorias());
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Producto producto, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) { model.addAttribute("categorias", service.listarCategorias()); return "productos/form"; }
        service.guardar(producto);
        flash.addFlashAttribute("success", "Producto guardado exitosamente");
        return "redirect:/productos";
    }
}
