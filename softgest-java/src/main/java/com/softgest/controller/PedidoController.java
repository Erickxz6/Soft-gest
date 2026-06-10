package com.softgest.controller;

import com.softgest.model.*;
import com.softgest.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public PedidoController(PedidoService ps, ClienteService cs, ProductoService prs, UsuarioService us) {
        this.pedidoService = ps; this.clienteService = cs; this.productoService = prs; this.usuarioService = us;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String estado, Model model) {
        if (estado != null && !estado.isBlank()) { model.addAttribute("pedidos", pedidoService.listarPorEstado(EstadoPedido.valueOf(estado))); model.addAttribute("estadoFiltro", estado); }
        else { model.addAttribute("pedidos", pedidoService.listarTodos()); }
        model.addAttribute("estados", EstadoPedido.values());
        return "pedidos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("productos", productoService.listarActivos());
        return "pedidos/nuevo";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam Long clienteId, @RequestParam List<Long> productoIds,
                        @RequestParam List<Integer> cantidades, @RequestParam(required = false) String observaciones,
                        @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes flash) {
        try {
            Usuario vendedor = usuarioService.buscarPorCorreo(userDetails.getUsername()).orElseThrow();
            Pedido pedido = pedidoService.crearPedido(clienteId, productoIds, cantidades, observaciones, vendedor);
            flash.addFlashAttribute("success", "Pedido " + pedido.getCodigo() + " creado exitosamente");
            return "redirect:/pedidos/" + pedido.getId();
        } catch (RuntimeException e) { flash.addFlashAttribute("error", e.getMessage()); return "redirect:/pedidos/nuevo"; }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id).orElseThrow());
        model.addAttribute("estadosPosibles", EstadoPedido.values());
        return "pedidos/detalle";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado, RedirectAttributes flash) {
        try { pedidoService.cambiarEstado(id, EstadoPedido.valueOf(estado)); flash.addFlashAttribute("success", "Estado actualizado"); }
        catch (Exception e) { flash.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/pedidos/" + id;
    }
}
