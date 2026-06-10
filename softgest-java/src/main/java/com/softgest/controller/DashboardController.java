package com.softgest.controller;

import com.softgest.model.Usuario;
import com.softgest.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    public DashboardController(PedidoService ps, ProductoService prs, ClienteService cs, UsuarioService us) {
        this.pedidoService = ps; this.productoService = prs; this.clienteService = cs; this.usuarioService = us;
    }

    @GetMapping("/")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("usuario", usuarioService.buscarPorCorreo(user.getUsername()).orElse(null));
        model.addAttribute("pedidosHoy", pedidoService.pedidosHoy());
        model.addAttribute("ventasHoy", pedidoService.ventasHoy());
        model.addAttribute("pedidosPendientes", pedidoService.pedidosPendientes());
        model.addAttribute("pedidosDespachados", pedidoService.pedidosDespachados());
        model.addAttribute("totalProductos", productoService.contarActivos());
        model.addAttribute("totalClientes", clienteService.listarActivos().size());
        model.addAttribute("productosCriticos", productoService.productosCriticos());
        return "dashboard";
    }
}
